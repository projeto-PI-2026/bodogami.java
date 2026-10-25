
CREATE TABLE genero (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(60) NOT NULL,
    descricao VARCHAR(200)
);

CREATE TABLE tipo_jogo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_tipo VARCHAR(45)  NOT NULL,
    descricao VARCHAR(200)
);


CREATE TABLE jogo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_jogo_id INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(200),
    editora VARCHAR(45),
    valor_aluguel_diaria DECIMAL(10,2) NOT NULL,
    imagem_url VARCHAR(200),
    min_jogadores INT,
    max_jogadores INT,
    idade_min INT,
    CONSTRAINT fk_tipo_jogo_jogo FOREIGN KEY (tipo_jogo_id)
        REFERENCES tipo_jogo(id)
);

CREATE TABLE exemplar (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_jogo VARCHAR(45),
    estado_conservacao VARCHAR(45),
    status VARCHAR(45),
    data_aquisicao TIMESTAMP,
    jogo_id INT NOT NULL,
    CONSTRAINT fk_jogo_exemplar FOREIGN KEY (jogo_id)
        REFERENCES jogo(id) ON DELETE CASCADE
);


CREATE TABLE jogo_genero (
    jogo_id   INT NOT NULL,
    genero_id INT NOT NULL,
    PRIMARY KEY (jogo_id, genero_id),
    CONSTRAINT fk_jogogenero_genero FOREIGN KEY (genero_id)
        REFERENCES genero (id) ON DELETE CASCADE,
    CONSTRAINT fk_jogogenero_jogo FOREIGN KEY (jogo_id)
        REFERENCES jogo (id) ON DELETE CASCADE
);

INSERT INTO genero (nome, descricao) VALUES ('RPG', 'Jogos de interpretação de papéis');
INSERT INTO genero (nome, descricao) VALUES ('Aventura', 'Jogos de exploração e missões');
INSERT INTO genero (nome, descricao) VALUES ('Terror', 'Jogos de suspense e sobrevivência');
INSERT INTO genero (nome, descricao) VALUES ('Ação', 'Jogos dinâmicos e de combate');
INSERT INTO tipo_jogo (nome_tipo, descricao) VALUES ('Tabuleiro', 'Jogos jogados sobre um tabuleiro físico');
INSERT INTO tipo_jogo (nome_tipo, descricao) VALUES ('Cartas', 'Jogos baseados em baralhos de cartas');
INSERT INTO tipo_jogo (nome_tipo, descricao) VALUES ('Dados', 'Jogos baseados em rolagem de dados');
