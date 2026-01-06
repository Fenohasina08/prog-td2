-- Types ENUM exacts
CREATE TYPE continent_enum AS ENUM ('AFRICA', 'EUROPA', 'ASIA', 'AMERICA');
CREATE TYPE player_position_enum AS ENUM ('GK', 'DEF', 'MIDF', 'STR');

-- Table Team
CREATE TABLE Team (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      continent continent_enum NOT NULL
);

-- Table Player
CREATE TABLE Player (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(250) NOT NULL,
                        age INT NOT NULL,
                        position player_position_enum NOT NULL,
                        id_team INT REFERENCES Team(id) ON DELETE SET NULL
);