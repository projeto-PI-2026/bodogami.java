


CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(60) NOT NULL,
    descricao VARCHAR(200)
);




CREATE TABLE jogo (
    id_jogo INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(200),
    editora VARCHAR(45),
    valor_aluguel_diaria DECIMAL(10,2),
    tipo_jogo INT,
    imagem_url VARCHAR(200),
    min_jogadores INT,
    max_jogadores INT,
    idade_min INT
);


CREATE TABLE jogo_categoria (
    categoria_id_categoria INT,
    jogo_id_jogo INT,
    PRIMARY KEY (categoria_id_categoria, jogo_id_jogo),
    CONSTRAINT fk_categoria FOREIGN KEY (categoria_id_categoria)
        REFERENCES categoria(id_categoria),
    CONSTRAINT fk_jogo FOREIGN KEY (jogo_id_jogo)
        REFERENCES jogo(id_jogo)
);


CREATE TABLE exemplar (
    id_exemplar INT AUTO_INCREMENT PRIMARY KEY,
    codigo_jogo VARCHAR(45),
    estado_conservacao VARCHAR(45),
    status VARCHAR(45),
    data_aquisicao TIMESTAMP,
    fk_jogo INT,
    CONSTRAINT fk_jogo_exemplar FOREIGN KEY (fk_jogo)
        REFERENCES jogo(id_jogo) ON DELETE CASCADE
);
