-- Usuários de teste disponíveis a cada inicialização da aplicação.
-- Senha de ambos: 12345678 (hash BCrypt)
INSERT INTO usuario (nome, email, senha, cargo, ativo)
VALUES ('Rafael', 'rafael@email.com', '$2a$10$PjgLDBpyvOh4CxBg.RcyDePwec/HwYYc1080gRStkDepra0j3Aj9.', 'FUNCIONARIO', 1);

INSERT INTO usuario (nome, email, senha, cargo, ativo)
VALUES ('Jenny', 'jenny@email.com', '$2a$10$PjgLDBpyvOh4CxBg.RcyDePwec/HwYYc1080gRStkDepra0j3Aj9.', 'FUNCIONARIO', 1);
