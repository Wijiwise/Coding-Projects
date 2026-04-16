CREATE DATABASE IF NOT EXISTS basketball_app;
USE basketball_app;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS enthusiast;
DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS tournament;
DROP TABLE IF EXISTS coach;
DROP TABLE IF EXISTS engagement;
DROP TABLE IF EXISTS engagement_player;
DROP TABLE IF EXISTS engagement_coach;
DROP TABLE IF EXISTS engagement_tournament;
DROP TABLE IF EXISTS team;
DROP TABLE IF EXISTS game;

SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS enthusiast;
CREATE TABLE enthusiast (
	enthusiast_id INT NOT NULL AUTO_INCREMENT,
	username VARCHAR(25) NOT NULL UNIQUE,
	lastname VARCHAR(50) NOT NULL,
	firstname VARCHAR(50) NOT NULL,
	middlename VARCHAR(50),
	sex VARCHAR(10) NOT NULL,
	date_of_birth DATE NOT NULL,
	status BOOLEAN NOT NULL,	
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (enthusiast_id)
);

DROP TABLE IF EXISTS player;
CREATE TABLE player (
	player_id INT NOT NULL AUTO_INCREMENT,
	lastname VARCHAR(50) NOT NULL,
	firstname VARCHAR(50) NOT NULL,
	middlename VARCHAR(50),
	sex VARCHAR(10) NOT NULL,
	date_of_birth DATE NOT NULL,
   height INT,
	weight INT,
	rStatus BOOLEAN,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (player_id)
);

DROP TABLE IF EXISTS coach;
CREATE TABLE coach (
	coach_id INT NOT NULL AUTO_INCREMENT,
	lastname VARCHAR(50) NOT NULL,
	firstname VARCHAR(50) NOT NULL,
	middlename VARCHAR(50),
	sex VARCHAR(10) NOT NULL,
	date_of_birth DATE NOT NULL,
	start_year INT NOT NULL,
	end_year INT,
	status BOOLEAN,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (coach_id)
);

DROP TABLE IF EXISTS tournament;
CREATE TABLE tournament (
	tournament_id INT AUTO_INCREMENT PRIMARY KEY,	
    tournament_name VARCHAR(200) NOT NULL,
	season_year INT,
	tournament_type VARCHAR(50),
	start_date DATE,
	end_date DATE,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS engagement;
CREATE TABLE engagement (
	engagement_id INT NOT NULL AUTO_INCREMENT,
	engagement_type VARCHAR(10) NOT NULL,
	enthusiast_id INT NOT NULL,
	status BOOLEAN NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (engagement_id),
	FOREIGN KEY (enthusiast_id) REFERENCES enthusiast(enthusiast_id)
);

-- subtype engagement tables
DROP TABLE IF EXISTS engagement_player;
CREATE TABLE engagement_player (
	engagement_id INT NOT NULL,
	player_id INT NOT NULL,
	PRIMARY KEY (engagement_id),
	FOREIGN KEY (engagement_id) REFERENCES engagement(engagement_id),
	FOREIGN KEY (player_id) REFERENCES player(player_id) 
);

DROP TABLE IF EXISTS engagement_coach;
CREATE TABLE engagement_coach (
	engagement_id INT NOT NULL,
	coach_id INT NOT NULL,
	PRIMARY KEY (engagement_id),
	FOREIGN KEY (engagement_id) REFERENCES engagement(engagement_id),
	FOREIGN KEY (coach_id) REFERENCES coach(coach_id) 
);

DROP TABLE IF EXISTS engagement_tournament;
CREATE TABLE engagement_tournament (
	engagement_id INT NOT NULL,
	tournament_id INT NOT NULL,
	PRIMARY KEY (engagement_id),
	FOREIGN KEY (engagement_id) REFERENCES engagement(engagement_id),
	FOREIGN KEY (tournament_id) REFERENCES tournament(tournament_id) 
);

DROP TABLE IF EXISTS team;
CREATE TABLE team (
    team_ID INT NOT NULL AUTO_INCREMENT,
    coach_id INT, 
	team_name VARCHAR(50) NOT NULL,
	number_of_players INT,
    registry_status BOOLEAN,
    tCreated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(team_ID),
FOREIGN KEY(coach_id) REFERENCES coach(coach_id)
);

DROP TABLE IF EXISTS competing_team;
CREATE TABLE competing_team (
    competing_team_id INT NOT NULL AUTO_INCREMENT,
    coach_id INT, 
	team_name VARCHAR(50) NOT NULL,
	number_of_players INT,
    registry_status BOOLEAN,
    tCreated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(competing_team_id),
FOREIGN KEY(coach_id) REFERENCES coach(coach_id)
);

DROP TABLE IF EXISTS game;
CREATE TABLE game (
    game_id INT NOT NULL AUTO_INCREMENT,
    tournament_id INT,
    competing_teamA_id INT,
    competing_teamB_id INT,
    winning_team_id INT,
    losing_team_id INT,
    score_ratio VARCHAR(10),
    game_status VARCHAR(20),
    start_date DATE,
    end_date DATE,
    PRIMARY KEY (game_id),
    FOREIGN KEY (tournament_id) REFERENCES tournament(tournament_id),
    FOREIGN KEY (competing_teamA_id) REFERENCES competing_team(competing_team_id),
    FOREIGN KEY (competing_teamB_id) REFERENCES competing_team(competing_team_id),
    FOREIGN KEY (winning_team_id) REFERENCES team(team_id),
    FOREIGN KEY (losing_team_id) REFERENCES team(team_id)
);