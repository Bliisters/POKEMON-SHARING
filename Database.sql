/* Database name: PokeputzDB */

USE PokeputzDB;

CREATE TABLE IF NOT EXISTS Farmers (
	FarmerToken VARCHAR(64) NOT NULL,
	FarmerName VARCHAR(256),
	FarmerPass VARCHAR(64),
	PRIMARY KEY (FarmerToken)
);

CREATE TABLE IF NOT EXISTS Pokeputz (
	PokeToken VARCHAR(64) NOT NULL,
	PokeName VARCHAR(256),
	PokeEvol VARCHAR(64),
	PokeType VARCHAR(64),
	PRIMARY KEY (PokeToken),
	FOREIGN KEY (PokeEvol) REFERENCES Pokeputz(PokeToken)
);

CREATE TABLE IF NOT EXISTS PokePosts (
	PostToken VARCHAR(64) NOT NULL,
	PostAuthor VARCHAR(64),
	PostPokeGiven VARCHAR(64),
	PostPokeLookedFor VARCHAR(64),
	PRIMARY KEY (PostToken),
	FOREIGN KEY (PostPokeGiven) REFERENCES Pokeputz(PokeToken),
	FOREIGN KEY (PostPokeLookedFor) REFERENCES Pokeputz(PokeToken)
);

CREATE TABLE IF NOT EXISTS PossessTable (
	PokeToken VARCHAR(64) NOT NULL,
	FarmerToken VARCHAR(64) NOT NULL,
	PRIMARY KEY (PokeToken, FarmerToken),
	FOREIGN KEY (PokeToken) REFERENCES Pokeputz(PokeToken),
	FOREIGN KEY (FarmerToken) REFERENCES Farmers(FarmerToken)
);
