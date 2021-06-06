DROP TABLE IF EXISTS masters;
CREATE TABLE masters (
                         id VARCHAR(36) NOT NULL PRIMARY KEY,
                         name VARCHAR(36) NOT NULL,
                         age VARCHAR(4) NOT NULL
);

DROP TABLE IF EXISTS planetes;
CREATE TABLE planetes (
                          id VARCHAR(36) NOT NULL PRIMARY KEY,
                          name VARCHAR(36) NOT NULL,
                          master_id VARCHAR(36),
                          FOREIGN KEY (master_id) REFERENCES masters(id)
);