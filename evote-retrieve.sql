# retrieve some data from the evote database

# retrieve all election officers
select * from officer;

# retrieve all electoral districts
select * from district;

# retrieve all voters and their electoral district
select voter.id, voter.firstname, voter.lastname, district.name
from voter, district
where voter.districtID = district.ID;


# retrieve all political parties
select * from party;

# retrieve all ballots
select * from ballot;

# retrieve all ballotItems
select * from ballotItem;

# retrieve all issues
select * from issue;

# retrieve all elections
select * from election;

# retrieve all candidate
select * from candidate;

# retrieve all vote records
select * from voteRecord;
