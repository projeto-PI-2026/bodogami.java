
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
        REFERENCES genero (id_genero),
    CONSTRAINT fk_jogogenero_jogo FOREIGN KEY (fk_jogo)
        REFERENCES jogo (id_jogo)
);

