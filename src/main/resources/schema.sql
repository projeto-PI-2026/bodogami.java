
CREATE TABLE genero (
    id_genero INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(60) NOT NULL,
    descricao VARCHAR(200)
);

CREATE TABLE tipo_jogo (
    id_tipo_jogo INT AUTO_INCREMENT PRIMARY KEY,
    nome_tipo VARCHAR(45)  NOT NULL,
    descricao VARCHAR(200)
);


CREATE TABLE jogo (
    id_jogo INT AUTO_INCREMENT PRIMARY KEY,
    fk_tipo_jogo INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(200),
    editora VARCHAR(45),
    valor_aluguel_diaria DECIMAL(10,2) NOT NULL,
    imagem_url VARCHAR(200),
    min_jogadores INT,
    max_jogadores INT,
    idade_min INT,
    CONSTRAINT fk_tipo_jogo_jogo FOREIGN KEY (fk_tipo_jogo)
        REFERENCES tipo_jogo(id_tipo_jogo)
);

CREATE TABLE exemplar (
    id_exemplar INT AUTO_INCREMENT PRIMARY KEY,
    codigo_jogo VARCHAR(45),
    estado_conservacao VARCHAR(45),
    status VARCHAR(45),
    data_aquisicao TIMESTAMP,
    fk_jogo INT NOT NULL,
    CONSTRAINT fk_jogo_exemplar FOREIGN KEY (fk_jogo)
        REFERENCES jogo(id_jogo) ON DELETE CASCADE
);


CREATE TABLE jogo_genero (
    fk_genero INT NOT NULL,
    fk_jogo   INT NOT NULL,
    PRIMARY KEY (fk_genero, fk_jogo),
    CONSTRAINT fk_jogogenero_genero FOREIGN KEY (fk_genero)
        REFERENCES genero (id_genero) ON DELETE CASCADE,
    CONSTRAINT fk_jogogenero_jogo FOREIGN KEY (fk_jogo)
        REFERENCES jogo (id_jogo) ON DELETE CASCADE
);

INSERT INTO genero (nome, descricao) VALUES ('RPG', 'Jogos de interpretação de papéis');
INSERT INTO genero (nome, descricao) VALUES ('Aventura', 'Jogos de exploração e missões');
INSERT INTO genero (nome, descricao) VALUES ('Terror', 'Jogos de suspense e sobrevivência');
INSERT INTO genero (nome, descricao) VALUES ('Ação', 'Jogos dinâmicos e de combate');
INSERT INTO tipo_jogo (nome_tipo, descricao) VALUES ('Tabuleiro', 'Jogos jogados sobre um tabuleiro físico');
INSERT INTO tipo_jogo (nome_tipo, descricao) VALUES ('Cartas', 'Jogos baseados em baralhos de cartas');
INSERT INTO tipo_jogo (nome_tipo, descricao) VALUES ('Dados', 'Jogos baseados em rolagem de dados');


/*Adicionando novas colunas usadas na sessão da branch feat-KAN134*/

CREATE TABLE plano_assinatura (
    id_plano INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(60) NOT NULL,
    valor_mensal DECIMAL(10,2) NOT NULL
);

CREATE TABLE assinatura (
    id_assinatura INT AUTO_INCREMENT PRIMARY KEY,
    fk_cliente INT NOT NULL,
    fk_plano INT NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_assinatura_cliente FOREIGN KEY (fk_cliente) REFERENCES cliente (id_cliente),
    CONSTRAINT fk_assinatura_plano FOREIGN KEY (fk_plano) REFERENCES plano_assinatura (id_plano)
);

CREATE TABLE aluguel (
    id_aluguel INT AUTO_INCREMENT PRIMARY KEY,
    fk_cliente INT NOT NULL,
    codigo_retirada VARCHAR(30) NOT NULL,
    data_agendamento TIMESTAMP NOT NULL,
    data_retirada TIMESTAMP,
    data_prevista_devolucao TIMESTAMP,
    data_devolucao TIMESTAMP,
    valor_total DECIMAL(10,2),
    status VARCHAR(30) NOT NULL,
    CONSTRAINT fk_aluguel_cliente FOREIGN KEY (fk_cliente) REFERENCES cliente (id_cliente)
);

CREATE TABLE item_aluguel (
    id_item_aluguel INT AUTO_INCREMENT PRIMARY KEY,
    fk_aluguel INT NOT NULL,
    fk_exemplar INT NOT NULL,
    CONSTRAINT fk_item_aluguel FOREIGN KEY (fk_aluguel) REFERENCES aluguel (id_aluguel),
    CONSTRAINT fk_item_exemplar FOREIGN KEY (fk_exemplar) REFERENCES exemplar (id_exemplar)
);

CREATE TABLE multa (
    id_multa INT AUTO_INCREMENT PRIMARY KEY,
    fk_cliente INT NOT NULL,
    fk_aluguel INT,
    tipo VARCHAR(30) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    valor_pago DECIMAL(10,2) DEFAULT 0 NOT NULL,
    data_vencimento DATE NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_multa_cliente FOREIGN KEY (fk_cliente) REFERENCES cliente (id_cliente),
    CONSTRAINT fk_multa_aluguel FOREIGN KEY (fk_aluguel) REFERENCES aluguel (id_aluguel)
);

CREATE TABLE pagamento (
    id_pagamento INT AUTO_INCREMENT PRIMARY KEY,
    fk_cliente INT NOT NULL,
    fk_usuario INT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    forma_pagamento VARCHAR(30) NOT NULL,
    numero_comprovante VARCHAR(60),
    data_pagamento TIMESTAMP NOT NULL,
    CONSTRAINT fk_pagamento_cliente FOREIGN KEY (fk_cliente) REFERENCES cliente (id_cliente),
    CONSTRAINT fk_pagamento_usuario FOREIGN KEY (fk_usuario) REFERENCES usuario (id_usuario)
);

CREATE TABLE pagamento_multa (
    fk_pagamento INT NOT NULL,
    fk_multa INT NOT NULL,
    valor_aplicado DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (fk_pagamento, fk_multa),
    CONSTRAINT fk_pgmulta_pagamento FOREIGN KEY (fk_pagamento) REFERENCES pagamento (id_pagamento),
    CONSTRAINT fk_pgmulta_multa FOREIGN KEY (fk_multa) REFERENCES multa (id_multa)
);

CREATE INDEX idx_multa_cliente_status ON multa (fk_cliente, status);
CREATE INDEX idx_aluguel_cliente ON aluguel (fk_cliente);
CREATE INDEX idx_pagamento_cliente ON pagamento (fk_cliente);