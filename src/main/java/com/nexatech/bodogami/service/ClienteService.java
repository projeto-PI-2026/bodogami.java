package com.nexatech.bodogami.service;

import com.nexatech.bodogami.dto.Cliente.ClienteRequestDto;
import com.nexatech.bodogami.dto.Cliente.ClienteResponseDto;
import com.nexatech.bodogami.entity.Cliente;
import com.nexatech.bodogami.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nexatech.bodogami.config.GerenciadorTokenJwt;
import com.nexatech.bodogami.dto.LoginRequestDto;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Period;

@Service
public class ClienteService {

    private final GerenciadorTokenJwt gerenciadorTokenJwt;

    private final ClienteRepository clienteRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public ClienteService(GerenciadorTokenJwt gerenciadorTokenJwt, ClienteRepository clienteRepository) {
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponseDto cadastrar(ClienteRequestDto dto) {

        String cpf = dto.getCpf() == null ? null : dto.getCpf().replaceAll("\\D", "");

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

        if (clienteRepository.existsByCpf(cpf)) {
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
        cliente.setCpf(cpf);
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

    public String login(LoginRequestDto dto) {

        Cliente cliente = clienteRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "E-mail ou senha incorretos"
                ));

        if (!passwordEncoder.matches(dto.getSenha(), cliente.getSenha())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "E-mail ou senha incorretos"
            );
        }

        return gerenciadorTokenJwt.gerarToken(
                cliente.getId(),
                cliente.getEmail(),
                "CLIENTE"
        );
    }
}