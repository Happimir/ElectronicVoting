
# Inserting values into officer #
INSERT INTO user (id,username, userpass, email, firstname, lastname, address)
VALUES ('100','mikekova', 'mike', 'mike@uga.edu', 'Michael', 'Kovalsky','Athens'), 
       ('101','zaidrash', 'zaid', 'zaid@uga.edu', 'Zaid', 'Rasheed', 'Athens'),
       ('102','jugapanc', 'jugal123','jugalpanchal25@uga.edu','Jugal','Panchal','Athens'),
       ('103','hwang','wang','hongji@uga.edu','Hongji','Wang','Athens');

INSERT INTO officer (userID) VALUES ('100'),('101');

# Inserting values into district #
INSERT INTO district (id, name)
VALUES ('1001', 'Crawford'),('1002', 'Clarke');


# Inserting values into voter #
INSERT INTO voter (id, age, userID, districtID )
VALUES ('300600', '21', '102', '1001'),
       ('300601', '23', '103', '1002');



# Inserting values into ballot #
INSERT INTO ballot (id, startDate, endDate, approved, districtID)
VALUES('501', '2016-11-01 08:30:00', '2016-11-01 15:30:00',True, '1001');

INSERT INTO ballot (id, startDate, endDate, approved, districtID)
VALUES('502', '2016-11-01 08:30:00', '2016-11-01 15:30:00', True, '1002');


# Inserting values into ballotItem #
INSERT INTO ballotItem (id, voteCount, ballotID)
VALUES('251', '0', '501'),
      ('252', '0', '501'),
      ('253', '0', '501'),
      ('254', '0', '501'),
      ('255', '0', '501'),
      ('256', '0', '501'),
      ('257', '0', '502'),
      ('258', '0', '502'),
      ('259', '0', '502'),
      ('260', '0', '502'),
      ('261', '0', '502'),
      ('262', '0', '502');

# Inserting values into election
INSERT INTO election (id, name, isPartisan, voteCount, bItemID)
VALUES('101010', 'Office of Supervision', True, '1050000', '251'),
      ('101020', 'Office of SE', False, '1050000', '252'),
      ('101030', 'Office of UGA', False, '1050000','253'),
      ('101040', 'Election', True, '1050000','257'),
      ('101050', 'Office of CS', False, '1050000','258'),
      ('101060', 'Office of MATH', False, '1050000','259');


# Inserting values into party #
INSERT INTO party (id, name)
VALUES ('1', 'Demo');

INSERT INTO party (id, name)
VALUES ('2', 'Repub');


# Inserting values into candidate #
INSERT INTO candidate (id, name, voteCount, electionID, partyID)
VALUES ('11', 'Smith1', '500000', '101010', '1'),
       ('12', 'Smith2', '550000', '101010', '2'),
       ('13', 'Smith3', '500000', '101010', '1'),
       ('14', 'Smith4', '500000', '101020', NULL),
       ('15', 'Smith5', '500000', '101020', NULL),
       ('16', 'Smith6', '500000', '101020', NULL),
       ('17', 'Smith7', '500000', '101030', NULL),
       ('18', 'Smith8', '500000', '101030', NULL),
       ('19', 'Smith9', '500000', '101030', NULL),
       ('20', 'Smith10', '500000', '101040', '1'),
       ('21', 'Smith11', '500000', '101040', '2'),
       ('22', 'Smith12', '500000', '101040', '1'),
       ('23', 'Smith13', '500000', '101050', NULL),
       ('24', 'Smith14', '500000', '101050', NULL),
       ('25', 'Smith15', '500000', '101050', NULL),
       ('26', 'Smith16', '500000', '101060', NULL),
       ('27', 'Smith17', '500000', '101060', NULL),
       ('28', 'Smith18', '500000', '101060', NULL);


# Inserting values into issue #
INSERT INTO issue (id, question, voteCount, yesCount, noCount, bItemID)
VALUES('102010', 'Should conditioning be left ON?1', '50000', '25000', '25000', '254'),
      ('102020', 'Should conditioning be left ON?2', '70000', '35000', '35000', '255'),
      ('102030', 'Should conditioning be left ON?3', '70000', '35000', '35000', '256'),
      ('102040', 'Should conditioning be left ON?4', '70000', '35000', '35000', '260'),
      ('102050', 'Should conditioning be left ON?5', '70000', '35000', '35000', '261'),
      ('102060', 'Should conditioning be left ON?6', '70000', '35000', '35000', '262');


# Inserting values into voteRecord 
INSERT INTO voteRecord (id, voterID, ballotID, voteDate)
VALUES ('2500', '300600', '501', '2016-11-01 10:30:00'), ('2505', '300600', '501', '2016-11-01 11:30:00'), ('2510', '300601', '502', '2016-11-01 11:45:00'), ('2515', '300601', '502', '2016-11-01 11:55:00');



