package com.nexatech.bodogami.service;

import com.nexatech.bodogami.dto.ClienteRequestDto;
import com.nexatech.bodogami.dto.ClienteResponseDto;
import com.nexatech.bodogami.entity.Cliente;
import com.nexatech.bodogami.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Period;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // conta gigante pra validar o cpf
    private boolean cpfValido(String cpf) {

        if (cpf == null || !cpf.matches("\\d{11}")) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int soma = 0;

        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }

        int resto = soma % 11;
        int primeiroDigito = resto < 2 ? 0 : 11 - resto;

        if (Character.getNumericValue(cpf.charAt(9)) != primeiroDigito) {
            return false;
        }

        soma = 0;

        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }

        resto = soma % 11;
        int segundoDigito = resto < 2 ? 0 : 11 - resto;

        return Character.getNumericValue(cpf.charAt(10)) == segundoDigito;
    }

    public ClienteResponseDto cadastrar(ClienteRequestDto dto) {

        LocalDate hoje = LocalDate.now();
        LocalDate nascimento = dto.getDataNascimento();

        if (nascimento == null || nascimento.isAfter(hoje)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de nascimento inválida");
        }

        int idade = Period.between(nascimento, hoje).getYears();

        if (idade < 18) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O cliente deve ter pelo menos 18 anos"
            );
        }

        if (!cpfValido(dto.getCpf())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "CPF inválido"
            );
        }

        if (clienteRepository.existsByCpf(dto.getCpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }

        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        if (!Boolean.TRUE.equals(dto.getAceitouTermo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "É necessário aceitar o termo de locação");
        }

        Cliente cliente = new Cliente();

        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setSenha(passwordEncoder.encode(dto.getSenha()));
        cliente.setLogradouro(dto.getLogradouro());
        cliente.setBairro(dto.getBairro());
        cliente.setCidade(dto.getCidade());

        cliente.setTermoAceitoEm(LocalDateTime.now());

        Cliente clienteSalvo = clienteRepository.save(cliente);

        ClienteResponseDto resposta = new ClienteResponseDto();

        resposta.setId(clienteSalvo.getId());
        resposta.setNome(clienteSalvo.getNome());
        resposta.setCpf(clienteSalvo.getCpf());
        resposta.setDataNascimento(clienteSalvo.getDataNascimento());
        resposta.setEmail(clienteSalvo.getEmail());
        resposta.setTelefone(clienteSalvo.getTelefone());
        resposta.setLogradouro(clienteSalvo.getLogradouro());
        resposta.setBairro(clienteSalvo.getBairro());
        resposta.setCidade(clienteSalvo.getCidade());
        resposta.setTermoAceitoEm(clienteSalvo.getTermoAceitoEm());

        return resposta;
    }
}