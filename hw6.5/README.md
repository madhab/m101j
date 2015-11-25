Start the replica set servers
-----------------------------

start mongod --replSet m101 --logpath 1.log --dbpath data\rs1 --port 27015 --smallfiles --oplogSize 64
start mongod --replSet m101 --logpath 2.log --dbpath data\rs2 --port 27016 --smallfiles --oplogSize 64
start mongod --replSet m101 --logpath 3.log --dbpath data\rs3 --port 27017 --smallfiles --oplogSize 64

Connect to the shell
--------------------

mongo --port 27015

Configure and initiate Replica set
----------------------------------

config = { _id: "m101", members:[
          { _id : 0, host : "localhost:27015"},
          { _id : 1, host : "localhost:27016"},
          { _id : 2, host : "localhost:27017"} ]
};
rs.initiate(config);
rs.status();