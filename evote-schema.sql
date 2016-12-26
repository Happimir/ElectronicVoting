#
# this SQL file creates the schema for the evote database
#

# remove the existing tables
#

DROP TABLE IF EXISTS voteRecord;
DROP TABLE IF EXISTS issue;
DROP TABLE IF EXISTS candidate;
DROP TABLE IF EXISTS election;
DROP TABLE IF EXISTS ballotItem;
DROP TABLE IF EXISTS ballot;
DROP TABLE IF EXISTS voter;
DROP TABLE IF EXISTS party;
DROP TABLE IF EXISTS district;
DROP TABLE IF EXISTS officer;

#
# Table definition for table 'officer'
#
CREATE TABLE officer (
  id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  username    VARCHAR(255) NOT NULL UNIQUE,
  userpass    VARCHAR(255) NOT NULL,
  email       VARCHAR(255) NOT NULL,
  firstname   VARCHAR(255) NOT NULL,
  lastname    VARCHAR(255) NOT NULL,
  address     VARCHAR(255) NOT NULL

) ENGINE=InnoDB;


#
# Table definition for table 'district'
#
CREATE TABLE district (
  id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(255) NOT NULL UNIQUE
) ENGINE=InnoDB;

#
# Table definition for table 'voter'
#
CREATE TABLE voter (
  id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  username    VARCHAR(255) NOT NULL UNIQUE,
  userpass    VARCHAR(255) NOT NULL,
  email       VARCHAR(255) NOT NULL,
  firstname   VARCHAR(255) NOT NULL,
  lastname    VARCHAR(255) NOT NULL,
  address     VARCHAR(255) NOT NULL,
  age         INT UNSIGNED NOT NULL,
  districtID  INT UNSIGNED NOT NULL,
  
  FOREIGN KEY (districtID) REFERENCES district(id) ON DELETE CASCADE
) ENGINE=InnoDB;

#
# Table definition for table 'ballot'
#
CREATE TABLE ballot (
  id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  startDate   DATETIME,
  endDate     DATETIME,
  approved    BOOLEAN NOT NULL DEFAULT 0,
  open		  BOOLEAN NOT NULL DEFAULT 0,
  districtID  INT UNSIGNED,
  
  FOREIGN KEY (districtID) REFERENCES district(id) ON DELETE CASCADE
) ENGINE=InnoDB;

#
# Table definition for table 'ballotItem'
#
CREATE TABLE ballotItem (
  id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  voteCount   INT UNSIGNED NOT NULL DEFAULT 0,
  ballotID    INT UNSIGNED NOT NULL,
  
  FOREIGN KEY (ballotID) REFERENCES ballot(id) ON DELETE CASCADE
) ENGINE=InnoDB;

#
# Table definition for table 'election'
#
CREATE TABLE election (
  id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(255) NOT NULL,
  isPartisan  BOOLEAN NOT NULL DEFAULT 0,
  voteCount   INT UNSIGNED NOT NULL DEFAULT 0,
  bItemID     INT UNSIGNED NOT NULL,
  
  FOREIGN KEY (bItemID) REFERENCES ballotItem(id) ON DELETE CASCADE
) ENGINE=InnoDB;

#
# Table definition for table 'party'
#
CREATE TABLE party (
  id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(255) NOT NULL UNIQUE
) ENGINE=InnoDB;

#
# Table definition for table 'candidate'
#
CREATE TABLE candidate (
  id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(255) NOT NULL UNIQUE,
  voteCount   INT UNSIGNED NOT NULL DEFAULT 0,
  electionID  INT UNSIGNED NOT NULL,
  partyID     INT UNSIGNED,

  FOREIGN KEY (electionID) REFERENCES election(id) ON DELETE CASCADE,
  FOREIGN KEY (partyID) REFERENCES party(id) ON DELETE CASCADE
) ENGINE=InnoDB;

#
# Table definition for table 'issue'
#
CREATE TABLE issue (
  id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  question    VARCHAR(255) NOT NULL,
  voteCount   INT UNSIGNED NOT NULL DEFAULT 0,
  yesCount    INT UNSIGNED NOT NULL DEFAULT 0,
  noCount     INT UNSIGNED NOT NULL DEFAULT 0,
  bItemID     INT UNSIGNED NOT NULL,
  FOREIGN KEY (bItemID) REFERENCES ballotItem(id) ON DELETE CASCADE
) ENGINE=InnoDB;

#
# Table definition for table 'voteRecord'
#
CREATE TABLE voteRecord (
  id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  voterID     INT UNSIGNED NOT NULL,
  ballotItemID    INT UNSIGNED NOT NULL,
  voteDate    DATETIME,

  FOREIGN KEY (voterID) REFERENCES voter(id) ON DELETE CASCADE,
  FOREIGN KEY (ballotItemID) REFERENCES ballotItem(id) ON DELETE CASCADE
) ENGINE=InnoDB;
