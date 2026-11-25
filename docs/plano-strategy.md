# Plano de implementação — Design Pattern Strategy no Bodogami

> Projeto: `bodogami.java` · Spring Boot 4.1.1 · Java 21 · JPA/H2
> Objetivo: aplicar Strategy de forma legítima (interface comum + múltiplas implementações + escolha em tempo de execução), sem reescrever o domínio.

---

## 1. Critério de sucesso

O projeto só está "de fato" usando Strategy quando:

1. Existe uma interface de comportamento com **2 ou mais implementações concretas**.
2. O código cliente (service/controller) **não conhece** as implementações — depende só da interface.
3. A escolha da implementação acontece **em tempo de execução**, por um parâmetro/enum, sem `if/else` ou `switch` de algoritmo no cliente.
4. Adicionar um novo algoritmo exige **criar uma classe nova** (e, no máximo, um valor no enum), sem alterar as classes existentes.

As interfaces `JogoServiceStrategy`, `AuthServiceStrategy`, `ClienteServiceStrategy` e `UsuarioServiceStrategy` criadas na etapa anterior são **fachadas de serviço** (contrato do que o controller pode chamar). Elas continuam existindo, mas **sozinhas não configuram Strategy** — o Strategy real fica nas interfaces de comportamento descritas abaixo.

---

## 2. Arquitetura-alvo

```
Controller
   │  depende da fachada (XServiceStrategy)
   ▼
Service (fachada / orquestrador)
   │  monta o pedido e delega
   ▼
Contexto (Strategy Context)
   │  escolhe a implementação por enum em um Map
   ▼
Strategy (interface de comportamento)
   ├── Implementação A  ──► Repository / regra de negócio
   ├── Implementação B  ──► Repository / regra de negócio
   └── Implementação C  ──► Repository / regra de negócio
```

Papéis:

| Papel no pattern | No projeto |
|---|---|
| `Strategy` | `AutenticacaoStrategy`, `BuscaJogoStrategy`, `CalculoAluguelStrategy` |
| `ConcreteStrategy` | `AutenticacaoClienteStrategy`, `BuscaJogoPorNomeStrategy`, `CalculoAluguelProgressivoStrategy`, etc. |
| `Context` | `ContextoAutenticacao`, `ContextoBuscaJogo`, `ContextoCalculoAluguel` (recebem `List<Strategy>` do Spring e montam um `Map<Enum, Strategy>`) |
| `Client` | `AuthService`, `JogoService` (fachadas) e controllers |

A seleção é feita por **enum + mapa**, que é a forma idiomática de Strategy com Spring: o Spring injeta todas as implementações `@Component` de uma interface e o contexto escolhe. Não há `switch` no cliente.

---

## 3. Estrutura final de arquivos

```
src/main/java/com/nexatech/bodogami/
├── controller/
│   ├── AuthController.java                  (sem mudança)
│   ├── ClienteController.java               (sem mudança)
│   ├── JogoController.java                  (+ busca tipada, + orçamento)
│   └── UsuarioController.java               (sem mudança)
├── dto/
│   ├── LoginResultadoDto.java               (já existe)
│   └── OrcamentoResponseDto.java            (novo)
├── exception/
│   ├── EstrategiaNaoEncontradaException.java (novo)
│   ├── JogoInvalidoException.java
│   └── ...
├── repository/
│   └── JogoRepository.java                  (+ findByTipoJogoId, + findByGenerosId)
└── service/
    ├── AuthService.java                     (fachada fina)
    ├── ClienteService.java                  (remove login duplicado)
    ├── JogoService.java                     (delega busca/orçamento)
    ├── UsuarioService.java
    └── strategy/
        ├── AuthServiceStrategy.java         (fachada, já existe)
        ├── ClienteServiceStrategy.java      (fachada, remove login)
        ├── JogoServiceStrategy.java         (fachada, + métodos)
        ├── UsuarioServiceStrategy.java      (fachada, já existe)
        ├── autenticacao/
        │   ├── AutenticacaoStrategy.java
        │   ├── AutenticacaoClienteStrategy.java
        │   ├── AutenticacaoUsuarioStrategy.java
        │   ├── PerfilAcesso.java
        │   └── ContextoAutenticacao.java
        ├── busca/
        │   ├── BuscaJogoStrategy.java
        │   ├── BuscaJogoPorNomeStrategy.java
        │   ├── BuscaJogoPorTipoStrategy.java
        │   ├── BuscaJogoPorGeneroStrategy.java
        │   ├── FiltroBuscaJogo.java
        │   ├── TipoBuscaJogo.java
        │   └── ContextoBuscaJogo.java
        └── aluguel/
            ├── CalculoAluguelStrategy.java
            ├── CalculoAluguelDiariaStrategy.java
            ├── CalculoAluguelProgressivoStrategy.java
            ├── ModalidadeAluguel.java
            └── ContextoCalculoAluguel.java
```

---

## 4. Strategy 1 — Autenticação (a mais clássica)

### Problema atual

`AuthService.loginCliente` e `AuthService.loginUsuario` têm o mesmo algoritmo, variando apenas o repositório e o perfil. Hoje isso é resolvido por dois métodos quase duplicados.

### Interface

```java
public enum PerfilAcesso { CLIENTE, USUARIO }

public interface AutenticacaoStrategy {
    PerfilAcesso perfil();
    LoginResultadoDto autenticar(LoginRequestDto dto);
}
```

### Implementações

| Classe | Repositório | Perfil |
|---|---|---|
| `AutenticacaoClienteStrategy` | `ClienteRepository` | `CLIENTE` |
| `AutenticacaoUsuarioStrategy` | `UsuarioRepository` | `USUARIO` |

Cada uma recebe por construtor repositório, `PasswordEncoder` e `GerenciadorTokenJwt`, e devolve `LoginResultadoDto` (token + dados). As mensagens de erro permanecem `401` como hoje.

### Contexto

```java
@Component
public class ContextoAutenticacao {

    private final Map<PerfilAcesso, AutenticacaoStrategy> estrategias;

    public ContextoAutenticacao(List<AutenticacaoStrategy> lista) {
        this.estrategias = lista.stream().collect(Collectors.toUnmodifiableMap(
                AutenticacaoStrategy::perfil, Function.identity()));
    }

    public LoginResultadoDto autenticar(PerfilAcesso perfil, LoginRequestDto dto) {
        AutenticacaoStrategy estrategia = estrategias.get(perfil);
        if (estrategia == null) {
            throw new EstrategiaNaoEncontradaException("Nenhuma estratégia para o perfil " + perfil);
        }
        return estrategia.autenticar(dto);
    }
}
```

### Fachada

`AuthService` fica com ~15 linhas, sem repositório nem regra de senha:

```java
@Service
public class AuthService implements AuthServiceStrategy {

    private final ContextoAutenticacao contexto;

    public LoginResultadoDto loginCliente(LoginRequestDto dto) {
        return contexto.autenticar(PerfilAcesso.CLIENTE, dto);
    }

    public LoginResultadoDto loginUsuario(LoginRequestDto dto) {
        return contexto.autenticar(PerfilAcesso.USUARIO, dto);
    }
}
```

`AuthController` não muda. Endpoints e payloads permanecem iguais.

**Ganho demonstrável:** criar `AutenticacaoAdminStrategy` no futuro = uma classe + um valor no enum. Nenhuma alteração no `AuthService`.

---

## 5. Strategy 2 — Busca de jogos

### Problema atual

`JogoService.buscarPorNome` só sabe buscar por nome. Qualquer novo filtro (tipo, gênero, nº de jogadores) vira um novo `if` ou um novo método no service.

### Tipos de apoio

```java
public enum TipoBuscaJogo { NOME, TIPO, GENERO }

public record FiltroBuscaJogo(String nome, Integer tipoJogoId, Integer generoId) {}
```

### Interface

```java
public interface BuscaJogoStrategy {
    TipoBuscaJogo tipo();
    List<Jogo> buscar(FiltroBuscaJogo filtro);
}
```

### Implementações

| Classe | Regra | Repositório |
|---|---|---|
| `BuscaJogoPorNomeStrategy` | nome não pode ser vazio; `findByNomeContainingIgnoreCase` | existente |
| `BuscaJogoPorTipoStrategy` | `tipoJogoId` obrigatório; `findByTipoJogoId` | novo método |
| `BuscaJogoPorGeneroStrategy` | `generoId` obrigatório; `findByGenerosId` | novo método |

Métodos novos no `JogoRepository` (derived queries, sem mudança de schema):

```java
List<Jogo> findByTipoJogoId(Integer tipoJogoId);
List<Jogo> findByGenerosId(Integer generoId);
```

Validações de filtro usam `JogoInvalidoException` (HTTP 400), como já ocorre hoje.

### Contexto e fachada

`ContextoBuscaJogo` segue o mesmo molde do contexto de autenticação.

`JogoService`:

```java
public List<Jogo> buscar(TipoBuscaJogo tipo, FiltroBuscaJogo filtro) {
    List<Jogo> jogos = contextoBusca.buscar(tipo, filtro);
    preencherQuantidade(jogos);
    return jogos;
}

public List<Jogo> buscarPorNome(String nome) {
    return buscar(TipoBuscaJogo.NOME, new FiltroBuscaJogo(nome, null, null));
}
```

O `preencherQuantidade` continua na fachada, pois é comportamento comum a todas as buscas.

### API (compatível com o front atual)

```
GET /jogos/buscar?nome=Catan                  → tipo=NOME (default)
GET /jogos/buscar?tipo=NOME&nome=Catan
GET /jogos/buscar?tipo=TIPO&tipoJogoId=1
GET /jogos/buscar?tipo=GENERO&generoId=2
```

No controller, `nome` passa a ser `required = false` (validação fica na strategy) e entra o parâmetro opcional `tipo`, com default `NOME`. Chamadas antigas continuam funcionando.

**Ganho demonstrável:** `BuscaJogoPorJogadoresStrategy` (busca por faixa de jogadores) = uma classe + um valor no enum + dois campos no filtro. Nenhum `if` novo.

---

## 6. Strategy 3 — Cálculo de aluguel

Este é o exemplo mais "puro" do pattern: regra de negócio variável, sem banco, fácil de testar e apresentar.

### Regras

| Modalidade | Regra |
|---|---|
| `PADRAO` | `valorDiaria × dias` |
| `PROGRESSIVO` | 10% de desconto a partir de 3 dias; 20% a partir de 7 dias |

### Interface e tipos

```java
public enum ModalidadeAluguel { PADRAO, PROGRESSIVO }

public interface CalculoAluguelStrategy {
    ModalidadeAluguel modalidade();
    BigDecimal calcular(Jogo jogo, int dias);
}
```

Implementações: `CalculoAluguelDiariaStrategy` e `CalculoAluguelProgressivoStrategy` (`BigDecimal`, `RoundingMode.HALF_UP`, escala 2).

### DTO e endpoint

```java
public record OrcamentoResponseDto(
        Integer jogoId,
        String nome,
        BigDecimal valorDiaria,
        int dias,
        ModalidadeAluguel modalidade,
        BigDecimal valorTotal
) {}
```

```
GET /jogos/{id}/orcamento?dias=5&modalidade=PROGRESSIVO
```

- `dias` deve ser `> 0`, senão `400`.
- `modalidade` default `PADRAO`.
- A rota já nasce pública, pois `GET /jogos/**` é liberado no `SecurityConfig`. Nenhuma alteração de segurança é necessária.

`JogoService.calcularOrcamento(id, dias, modalidade)` busca o jogo via `buscarPorId`, delega ao `ContextoCalculoAluguel` e monta o DTO.

---

## 7. Fase 0 — limpeza necessária antes das strategies

1. **Injetar `PasswordEncoder`** em `ClienteService` e `UsuarioService` em vez de `new BCryptPasswordEncoder()`. Já existe o bean em `SecurityConfig`. Isso também é Strategy (o Spring Security usa `PasswordEncoder` como strategy) e elimina três instâncias diferentes no projeto.
2. **Remover `ClienteService.login`** e o método correspondente em `ClienteServiceStrategy`. Ele é código morto: o login real é feito pelo `AuthService`/`AuthController`.
3. **Remover imports duplicados** em `Cliente.java` e `AuthService.java` (não bloqueiam build, mas sujam o código).
4. Opcional: extrair `preencherQuantidade` para um helper se mais de um service passar a usá-lo.

---

## 8. Ordem de execução e commits

| Fase | Entrega | Estimativa |
|---|---|---|
| 0 | Limpeza (encoder, login morto, imports) | 30–60 min |
| 1 | Strategy de autenticação + testes | 2–3 h |
| 2 | Strategy de busca + API + testes | 2–3 h |
| 3 | Strategy de cálculo de aluguel + endpoint + testes | 1–2 h |
| 4 | Testes de integração, README/diagrama, roteiro de apresentação | 1–2 h |

Sugestão de commits (alinhada ao histórico do repo):

```
refactor: injeta PasswordEncoder e remove login duplicado
feat(strategy): adiciona estrategias de autenticacao com contexto e enum
feat(strategy): adiciona estrategias de busca de jogos
feat(strategy): adiciona estrategias de calculo de aluguel
test(strategy): cobre implementacoes e contextos de selecao
docs: documenta arquitetura e fluxo do pattern strategy
```

Cada fase deve deixar `./mvnw test` verde antes de avançar. Neste ambiente, compilar/testar exige o JDK 21:

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk ./mvnw test
```

---

## 9. Testes

### Unitários (sem Spring)

- `CalculoAluguelDiariaStrategyTest` e `CalculoAluguelProgressivoStrategyTest`: valores nos limites (1, 2, 3, 6, 7 dias), arredondamento.
- `ContextoCalculoAluguelTest`: resolve `PADRAO`/`PROGRESSIVO` e lança exceção para modalidade sem strategy.

### Unitários com Mockito

- `AutenticacaoClienteStrategyTest`: sucesso, senha errada, e-mail inexistente.
- `AutenticacaoUsuarioStrategyTest`: idem.
- `ContextoAutenticacaoTest`: cada perfil resolve a strategy correta; perfil desconhecido falha.
- `BuscaJogoPorNomeStrategyTest`: nome vazio → `JogoInvalidoException`; nome válido → lista do repositório.
- `BuscaJogoPorTipoStrategyTest` / `BuscaJogoPorGeneroStrategyTest`: id obrigatório e chamada ao método correto do repositório.
- `ContextoBuscaJogoTest`: cada tipo resolve a strategy correta.

### Integração (MockMvc + H2)

- `POST /clientes/login` e `POST /usuarios/login`: 200 com cookie e 401 com credenciais erradas.
- `GET /jogos/buscar` nos três tipos: resultado correto e 400 quando o filtro obrigatório falta.
- `GET /jogos/{id}/orcamento`: total correto nas duas modalidades e 400 para `dias <= 0`.

---

## 10. Critérios de aceite (rubrica)

| Requisito do pattern | Evidência no projeto |
|---|---|
| Interface comum | `AutenticacaoStrategy`, `BuscaJogoStrategy`, `CalculoAluguelStrategy` |
| Múltiplas implementações | 2 de autenticação, 3 de busca, 2 de cálculo |
| Contexto delega sem conhecer a implementação | `Contexto*` com `Map<Enum, Strategy>` |
| Cliente depende da abstração | `AuthService` e `JogoService` só conhecem interface + contexto |
| Seleção em runtime | enum vindo da rota/perfil |
| Aberto para extensão, fechado para modificação | nova strategy = nova classe `@Component` + valor no enum |
| Sem condicional de algoritmo no cliente | nenhum `switch`/`if` escolhendo implementação |

### Como demonstrar na apresentação

Adicionar ao vivo uma quarta busca (`BuscaJogoPorJogadoresStrategy`): criar a classe, adicionar o valor no enum e reiniciar. A busca nova funciona sem tocar em `JogoService`, `ContextoBuscaJogo` ou controller. Isso prova o princípio Aberto/Fechado na prática.

---

## 11. Riscos e decisões

| Risco | Mitigação |
|---|---|
| Spring não encontrar strategies (`List` vazia) | contexto falha rápido com `EstrategiaNaoEncontradaException`; testes cobrem |
| Ambiguidade de beans | as fachadas continuam com uma única implementação; as strategies são coleções, não injeção única |
| Quebra do front atual | `/jogos/buscar?nome=` continua funcionando com `tipo=NOME` default |
| Excesso de engenharia | só três famílias de strategy; cadastro/validação de cliente fica como extensão opcional |
| `@ResponseStatus(reason=...)` engolir mensagens | manter o padrão atual; se quiser mensagens dinâmicas, criar `@RestControllerAdvice` em fase futura |
| H2 em memória / `data.sql` | nenhuma mudança de schema; derived queries novas não exigem migração |

### Extensões opcionais (não necessárias para a entrega)

- `ValidacaoCadastroClienteStrategy`: cada regra de cadastro (idade, CPF, e-mail, termo) vira uma strategy validada em cadeia.
- `HashSenhaStrategy`: BCrypt vs Argon2 escolhido por propriedade. Hoje o `PasswordEncoder` do Spring já cumpre esse papel como strategy nativa.

---

## 12. Resultado final esperado

- `AuthService` deixa de duplicar login e passa a delegar a dois algoritmos intercambiáveis.
- `JogoService.buscarPorNome` deixa de ser o único filtro possível; a busca passa a ser extensível por enum.
- O cálculo de aluguel nasce já como Strategy, com regras puras e testáveis.
- Controllers continuam finos e dependentes apenas de fachadas.
- O projeto passa a ter três exemplos claros do pattern, com testes e um fluxo de seleção em tempo de execução — evidência suficiente para avaliação acadêmica.
