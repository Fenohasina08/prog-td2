CREATE TYPE continent AS ENUM ('AFRICA', 'EUROPA', 'ASIA', 'AMERICA');
CREATE TYPE position AS ENUM ('GK', 'DEF', 'MIDF', 'STR');

CREATE TABLE Team (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      continent continent_type NOT NULL
);

CREATE TABLE Player (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(250) NOT NULL,
                        age INT NOT NULL,
                        position position_type NOT NULL,
                        id_team INT REFERENCES Team(id) ON DELETE SET NULL
);