--Dados ficticios para testar a sessão de Clientes.

INSERT INTO usuario (nome, email, senha, cargo, ativo) VALUES ('Atendente Teste', 'atendente@bodogami.com', 'SENHA_FICTICIA', 'ATENDENTE', TRUE);
INSERT INTO usuario (nome, email, senha, cargo, ativo) VALUES ('Ex-Funcionario', 'ex@bodogami.com', 'SENHA_FICTICIA', 'ATENDENTE', FALSE);

INSERT INTO cliente (nome, cpf, data_nascimento, email, telefone, senha, logradouro, bairro, cidade) VALUES ('Ana Souza', '11111111111', '1990-05-10', 'ana@email.com', '11900000001', 'SENHA_FICTICIA', 'Rua A, 10', 'Centro', 'Embu das Artes');
INSERT INTO cliente (nome, cpf, data_nascimento, email, telefone, senha, logradouro, bairro, cidade) VALUES ('Bruno Lima', '22222222222', '1985-03-20', 'bruno@email.com', '11900000002', 'SENHA_FICTICIA', 'Rua B, 20', 'Jardim', 'Taboão da Serra');
INSERT INTO cliente (nome, cpf, data_nascimento, email, telefone, senha, logradouro, bairro, cidade) VALUES ('Carla Dias', '33333333333', '1995-07-30', 'carla@email.com', '11900000003', 'SENHA_FICTICIA', 'Rua C, 30', 'Vila Nova', 'São Paulo');
INSERT INTO cliente (nome, cpf, data_nascimento, email, telefone, senha, logradouro, bairro, cidade) VALUES ('Diego Rocha', '44444444444', '1992-11-02', 'diego@email.com', '11900000004', 'SENHA_FICTICIA', 'Rua D, 40', 'Centro', 'Embu das Artes');

INSERT INTO plano_assinatura (nome, valor_mensal) VALUES ('Básico', 29.90);
INSERT INTO plano_assinatura (nome, valor_mensal) VALUES ('Premium', 59.90);

INSERT INTO assinatura (fk_cliente, fk_plano, data_inicio, status) VALUES (1, 2, '2026-01-01', 'ATIVA');
INSERT INTO assinatura (fk_cliente, fk_plano, data_inicio, status) VALUES (2, 1, '2026-02-01', 'ATIVA');
INSERT INTO assinatura (fk_cliente, fk_plano, data_inicio, status) VALUES (4, 1, '2026-03-01', 'ATIVA');

INSERT INTO jogo (fk_tipo_jogo, nome, descricao, editora, valor_aluguel_diaria, min_jogadores, max_jogadores, idade_min) VALUES (1, 'Catan', 'Colonização', 'Devir', 15.00, 3, 4, 10);
INSERT INTO jogo (fk_tipo_jogo, nome, descricao, editora, valor_aluguel_diaria, min_jogadores, max_jogadores, idade_min) VALUES (2, 'Uno', 'Cartas', 'Mattel', 5.00, 2, 10, 7);
INSERT INTO exemplar (codigo_jogo, estado_conservacao, status, data_aquisicao, fk_jogo) VALUES ('CAT-1', 'Bom', 'Disponível', CURRENT_TIMESTAMP, 1);
INSERT INTO exemplar (codigo_jogo, estado_conservacao, status, data_aquisicao, fk_jogo) VALUES ('CAT-2', 'Bom', 'Disponível', CURRENT_TIMESTAMP, 1);
INSERT INTO exemplar (codigo_jogo, estado_conservacao, status, data_aquisicao, fk_jogo) VALUES ('UNO-1', 'Novo', 'Disponível', CURRENT_TIMESTAMP, 2);

INSERT INTO aluguel (fk_cliente, codigo_retirada, data_agendamento, data_retirada, data_prevista_devolucao, data_devolucao, valor_total, status) VALUES (1, 'RET-0001', TIMESTAMP '2026-10-01 10:00:00', TIMESTAMP '2026-10-02 10:00:00', TIMESTAMP '2026-10-09 10:00:00', TIMESTAMP '2026-10-15 10:00:00', 45.00, 'DEVOLVIDO_CONFERIDO');
INSERT INTO aluguel (fk_cliente, codigo_retirada, data_agendamento, data_retirada, data_prevista_devolucao, data_devolucao, valor_total, status) VALUES (1, 'RET-0002', TIMESTAMP '2026-11-10 10:00:00', TIMESTAMP '2026-11-11 10:00:00', TIMESTAMP '2026-11-18 10:00:00', NULL, 30.00, 'ATRASADO');
INSERT INTO aluguel (fk_cliente, codigo_retirada, data_agendamento, data_retirada, data_prevista_devolucao, data_devolucao, valor_total, status) VALUES (2, 'RET-0003', TIMESTAMP '2026-09-01 10:00:00', TIMESTAMP '2026-09-02 10:00:00', TIMESTAMP '2026-09-09 10:00:00', TIMESTAMP '2026-09-12 10:00:00', 25.00, 'DEVOLVIDO_CONFERIDO');
INSERT INTO aluguel (fk_cliente, codigo_retirada, data_agendamento, data_retirada, data_prevista_devolucao, data_devolucao, valor_total, status) VALUES (4, 'RET-0004', TIMESTAMP '2026-10-20 10:00:00', TIMESTAMP '2026-10-21 10:00:00', TIMESTAMP '2026-10-28 10:00:00', TIMESTAMP '2026-10-28 10:00:00', 15.00, 'DEVOLVIDO_CONFERIDO');

INSERT INTO item_aluguel (fk_aluguel, fk_exemplar) VALUES (1, 1);
INSERT INTO item_aluguel (fk_aluguel, fk_exemplar) VALUES (1, 3);
INSERT INTO item_aluguel (fk_aluguel, fk_exemplar) VALUES (2, 2);
INSERT INTO item_aluguel (fk_aluguel, fk_exemplar) VALUES (3, 3);
INSERT INTO item_aluguel (fk_aluguel, fk_exemplar) VALUES (4, 1);

INSERT INTO multa (fk_cliente, fk_aluguel, tipo, valor, valor_pago, data_vencimento, data_criacao, status) VALUES (1, 1, 'ATRASO', 20.00, 0, '2026-10-16', TIMESTAMP '2026-10-15 10:00:00', 'PENDENTE');
INSERT INTO multa (fk_cliente, fk_aluguel, tipo, valor, valor_pago, data_vencimento, data_criacao, status) VALUES (1, 1, 'AVARIA', 35.50, 0, '2026-10-20', TIMESTAMP '2026-10-15 10:05:00', 'PENDENTE');
INSERT INTO multa (fk_cliente, fk_aluguel, tipo, valor, valor_pago, data_vencimento, data_criacao, status) VALUES (2, 3, 'ATRASO', 10.00, 10.00, '2026-09-13', TIMESTAMP '2026-09-12 10:00:00', 'PAGA');
INSERT INTO multa (fk_cliente, fk_aluguel, tipo, valor, valor_pago, data_vencimento, data_criacao, status) VALUES (4, 4, 'AVARIA', 80.00, 0, '2026-11-05', TIMESTAMP '2026-10-28 10:00:00', 'PENDENTE');

INSERT INTO pagamento (fk_cliente, fk_usuario, tipo, valor, forma_pagamento, numero_comprovante, data_pagamento) VALUES (2, 1, 'QUITACAO_MULTA', 10.00, 'DINHEIRO', 'COMP-0001', TIMESTAMP '2026-09-13 15:00:00');
INSERT INTO pagamento_multa (fk_pagamento, fk_multa, valor_aplicado) VALUES (1, 3, 10.00);