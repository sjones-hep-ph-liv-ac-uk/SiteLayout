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



SET TRANSACTION ISOLATION LEVEL SERIALIZABLE; 
SET autocommit = 0;

#DROP TABLE nodeType;
CREATE TABLE nodeType(
  nodeTypeName varchar(10),
  cpu integer,
  slot integer,
  hs06PerSlot float,
  memPerNode float,
  PRIMARY KEY( nodeTypeName)
) TYPE = INNODB;

#DROP TABLE site;
CREATE TABLE site (
  siteName varchar(50),
  description  varchar(50),
  location varchar(50),
  longitude float,
  latitude float,
  admin varchar(50),
  PRIMARY KEY( siteName )
) TYPE = INNODB;

#DROP TABLE cluster;
CREATE TABLE cluster (
  clusterName  varchar(20),
  descr        varchar(50),
  siteName varchar(50) NOT NULL,
  PRIMARY KEY( clusterName ),
  FOREIGN KEY (siteName) REFERENCES site(siteName) 
) TYPE = INNODB;

#DROP TABLE nodeSet;
CREATE TABLE nodeSet (
  nodeSetName varchar(10),
  nodeTypeName varchar(10),
  nodeCount integer,
  cluster varchar(20),
  PRIMARY KEY( nodeSetName ),
  FOREIGN KEY (cluster) REFERENCES cluster(clusterName) ,
  FOREIGN KEY (nodeTypeName) REFERENCES nodeType(nodeTypeName)
) TYPE = INNODB;


