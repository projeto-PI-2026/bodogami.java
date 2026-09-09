// abrir modal
const botaoAbrir = document.querySelector('.botao-adicionar-jogo');
const modal = document.getElementById('modal-jogo');
const botaoFechar = document.querySelector('.botao-fechar');


botaoAbrir.addEventListener('click', () => {
  modal.classList.add('ativo');
});

botaoFechar.addEventListener('click', () => {
  modal.classList.remove('ativo');
});

window.addEventListener('click', (event) => {
  if (event.target === modal) {
    modal.classList.remove('ativo');
  }
});

// cadastro de jogo!! 
const botaoAdicionar = document.getElementById("adicionar-jogo");

botaoAdicionar.addEventListener("click", function() {

  const nomeJogo = document.getElementById('nome-jogo').value;
  const descricaoJogo = document.getElementById('descricao-jogo').value;
  const minimoJogadores = document.getElementById('minimo-jogadores').value;
  const maximoJogadores = document.getElementById('maximo-jogadores').value;
  const tipoJogo = document.getElementById('selecionar-tipo-jogo').value;
  const generoSelecionado = document.getElementById('selecionar-genero').value;
  const nomeEditora = document.getElementById('nome-editora').value;
  const idadeMinima = document.getElementById('idade-minima').value;
  const valorDiaria = document.getElementById('valor-diaria').value;

if (
    nomeJogo == "" || 
    descricaoJogo == "" || 
    minimoJogadores == "" || 
    maximoJogadores == "" ||
    tipoJogo == "" || 
    nomeEditora == "" ||  
    idadeMinima == "" || 
    valorDiaria == ""

  ) {
      alert("Por favor, preencha todos os campos do formulário");
      return; 
  }

    let idGenero = null;
    if (generoSelecionado != "") {
        idGenero = Number(generoSelecionado);
    }

  const novoJogo = {
    nome: nomeJogo,
    descricao: descricaoJogo,
    editora: nomeEditora,
    min_jogadores: Number(minimoJogadores),
    max_jogadores: Number(maximoJogadores),
    idade_min: Number(idadeMinima),
    valor_aluguel_diaria: Number(valorDiaria),
    fk_tipo_jogo: Number(tipoJogo),
    id_genero: idGenero
  };

const dadosParaJava = JSON.stringify(novoJogo); // transforma o obj js em uma string de texto formato json
  
// envio dos dados!!
fetch("http://localhost:8080/jogos", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: dadosParaJava
  })
  .then(function(resposta) {
    if(resposta.ok) {
        
      alert("Jogo cadastrado com sucesso");

      modal.classList.remove('ativo')

      document.getElementById('nome-jogo').value = "";
      document.getElementById('descricao-jogo').value = "";
      document.getElementById('minimo-jogadores').value = "";
      document.getElementById('maximo-jogadores').value = "";
      document.getElementById('nome-editora').value = "";
      document.getElementById('idade-minima').value = "";
      document.getElementById('valor-diaria').value = "";

      carregarJogos();
    } else {
      alert("Erro")
    }
  })
  .catch(function(erro ){
    console.error("erro de conexao com o servidor ",  erro)
  })

})
;

// buscar tipos de jogos do banco para o select dro front!!
function carregarTipos() {
    fetch("http://localhost:8080/jogos/tipos")
        .then(function(resposta) {
            return resposta.json();
        })
        .then(function(tipos) { 
            const select = document.getElementById("selecionar-tipo-jogo");
            select.innerHTML = "";

            // percorre todos os tipos vindo do banco (fiz insert no banco dos tipos)
            for (let i = 0; i < tipos.length; i++) {
                const tipo = tipos[i];
                select.innerHTML += '<option value="' + tipo.id_tipo_jogo + '">' + tipo.nome_tipo + '</option>';
            }
        })
        .catch(function(erro) {
            console.error("Erro ao carregar os tipos:", erro);
        });
}

//buscar generos 
function carregarGeneros() {
    fetch("http://localhost:8080/jogos/generos")
        .then(function(resposta) {
            return resposta.json();
        })
        .then(function(generos) {
            const select = document.getElementById("selecionar-genero");
            select.innerHTML = "";

            // percorre todos os generos vindos do banco
            for (let i = 0; i < generos.length; i++) {
                const genero = generos[i];
                select.innerHTML += '<option value="' + genero.id_genero + '">' + genero.nome + '</option>';
            }
        })
        .catch(function(erro) {
            console.error("Erro ao carregar os generos:", erro);
        });
}


// carrega os jogos!!
function carregarJogos() {
    fetch("http://localhost:8080/jogos")
        .then(function(resposta) {
            return resposta.json();
        })
        .then(function(jogos) {
          // seleciona o elemtno principal onde os card serao exibidos
            const sectionMain = document.getElementById("container-lista-jogos");
            sectionMain.innerHTML = "";

            // loop pra percorrer o array de jogos  recebido do java
            for (let i = 0; i < jogos.length; i++) {
                const jogo = jogos[i];

                sectionMain.innerHTML += `
                    <div class="container-jogo">
                      <img src="bodogami-jogo.png" alt="Capa do Jogo">

                      <div class="info-jogo">
                        <p>${jogo.nome}</p>
                        <p>Qtd: ${jogo.quantidade}</p>
                      </div>

                      <div class="botoes-jogo">
                        <button class="botao-add-qtd" data-id="${jogo.id_jogo}"> <i class="fa fa-plus" aria-hidden="true"></i> </button>
                        <button class="botao-remove-qtd" data-id="${jogo.id_jogo}"> <i class="fa fa-minus" aria-hidden="true"></i> </button>
                        <button class="botao-deleta-jogo" data-id="${jogo.id_jogo}"> Deletar Jogo </button>
                      </div>
                </div>
                `;
            }
        })
        .catch(function(erro) {
            console.error("Erro ao carregar os jogos:", erro);
        });
}


// gerenciador global pra fazer os botoes de adicionar, remover e deletar
document.addEventListener("click", function(evento) {
    const elemento = evento.target; // evento.target diz em qual elemento da tela o usuario clicou

    // adicionar exemplar!!!
    const botaoAdd = elemento.closest(".botao-add-qtd");
    if (botaoAdd) {
        const idJogo = botaoAdd.getAttribute("data-id"); // pega o id guardado no botao
        
        fetch(`http://localhost:8080/jogos/${idJogo}/adicionar`, {
            method: "POST"
        })
        .then(function(resposta) {
            if (resposta.ok) {
                carregarJogos(); 
            } else {
                alert("Erro ao adicionar exemplar.");
            }
        });
    }
 
    // remover exemplar!!! (segue mesma dinamica do adicionar)
    const botaoRemove = elemento.closest(".botao-remove-qtd");
    if (botaoRemove) {
        const idJogo = botaoRemove.getAttribute("data-id");
        
        fetch(`http://localhost:8080/jogos/${idJogo}/remover`, {
            method: "DELETE"
        })
        .then(function(resposta) {
            if (resposta.ok) {
                carregarJogos(); 
            } else if (resposta.status == 404) {
              // se o servidor retorar 404 signifca que o estoque ja esta zerado
                alert("Este jogo não tem mais exemplares para remover!");
            } else {
                alert("Erro ao remover exemplar.");
            }
        });
    }

    // deletar jogo inteiro!! 
    const botaoDeleta = elemento.closest(".botao-deleta-jogo");
    if (botaoDeleta) {
        const idJogo = botaoDeleta.getAttribute("data-id");
        
        // caixa de dialogo
        const desejaDeletar = confirm("Tem certeza que deseja deletar este jogo do estoque?");
        
        if (desejaDeletar) {
            fetch(`http://localhost:8080/jogos/${idJogo}`, {
                method: "DELETE"
            })
            .then(function(resposta) {
                if (resposta.ok || resposta.status === 204) {
                    carregarJogos();
                } else {
                    alert("Erro ao deletar o jogo.");
                }
            })
            .catch(function(erro) {
                console.error("Erro de conexão:", erro);
            });
        }
    }

    
});

// barra de pesquisa
const inputPesquisa = document.getElementById("input-pesquisa");

if (inputPesquisa) {
  // dispara a ação sempre que o user digita algo no campo de busca
    inputPesquisa.addEventListener("input", function() {
        const termoDigitado = inputPesquisa.value.trim(); // remove espaços vazios

        // e o input estiver vazio traz todos os jogos de novo
        if (termoDigitado == "") {
            carregarJogos();
            return;
        }

        // faz a requisição pro endpoint de busca passando o parâmetro 'nome' (queryparam )
        fetch(`http://localhost:8080/jogos/buscar?nome=${encodeURIComponent(termoDigitado)}`)
            .then(function(resposta) {
                if (resposta.ok) {
                    return resposta.json();
                } else {
                    return [];
                }
            })
            .then(function(jogos) {
                const sectionMain = document.getElementById("container-lista-jogos"); // pega o elemento html onde a lista de cards dos jogos vai serr exibida
                sectionMain.innerHTML = ""; 

                if (jogos.length == 0) {
                    sectionMain.innerHTML = "<p>Nenhum jogo encontrado.</p>";
                    return;
                }

                // mostra na tela apenas os cards dos jogos filtrados pela pesquisa
                for (let i = 0; i < jogos.length; i++) {
                    const jogo = jogos[i];

                    sectionMain.innerHTML += `
                        <div class="container-jogo">
                            <img src="bodogami-jogo.png" alt="Capa do Jogo">

                            <div class="info-jogo">
                                <p>${jogo.nome}</p>
                                <p>Qtd: ${jogo.quantidade}</p>
                            </div>

                            <div class="botoes-jogo">
                                <button class="botao-add-qtd" data-id="${jogo.id_jogo}"> <i class="fa fa-plus" aria-hidden="true"></i> </button>
                                <button class="botao-remove-qtd" data-id="${jogo.id_jogo}"> <i class="fa fa-minus" aria-hidden="true"></i> </button>
                                <button class="botao-deleta-jogo" data-id="${jogo.id_jogo}"> Deletar Jogo </button>
                            </div>
                        </div>
                    `;
                }
            })
            .catch(function(erro) {
                console.error("Erro na busca:", erro);
            });
    });
}

window.onload = function() {
    carregarTipos(); 
    carregarGeneros();
    carregarJogos(); 
};

