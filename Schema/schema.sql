#mysql -u root -p <return>
create database resources;
CREATE USER 'resources'@'localhost' IDENTIFIED BY 'somepw';
CREATE USER 'resources'@'hepgrid90.ph.liv.ac.uk' IDENTIFIED BY 'somepw';
CREATE USER 'resources'@'127.0.0.1' IDENTIFIED BY 'somepw';
grant ALL PRIVILEGES ON resources.* TO 'resources'@'localhost';
grant ALL PRIVILEGES ON resources.* TO 'resources'@'hepgrid90.ph.liv.ac.uk';
grant ALL PRIVILEGES ON resources.* TO 'resources'@'127.0.0.1';

#/usr/bin/mysql -u resources -p
#use resources

DROP TABLE nodeType;
CREATE TABLE nodeType(
  nodeTypeName varchar(10),
  cpu integer,
  slot integer,
  hs06PerSlot float,
  memPerNode float,
  PRIMARY KEY( nodeTypeName)
);

DROP TABLE cluster;
CREATE TABLE cluster (
  clusterName  varchar(20),
  descr        varchar(50),
  PRIMARY KEY( clusterName )
);

DROP TABLE nodeSet;
CREATE TABLE nodeSet (
  nodeSetName varchar(10),
  nodeTypeName varchar(10),
  nodeCount integer,
  cluster varchar(20),
  PRIMARY KEY( nodeSetName ),
  FOREIGN KEY (cluster) REFERENCES cluster(clusterName) ,
  FOREIGN KEY (nodeTypeName) REFERENCES nodeType(nodeTypeName)
);
