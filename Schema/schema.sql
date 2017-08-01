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

#DROP TABLE NodeType;
CREATE TABLE NodeType(
  nodeTypeName varchar(10),
  cpu integer,
  slot integer,
  hs06PerSlot float,
  memPerNode float,
  version   bigint(20),
  PRIMARY KEY( nodeTypeName)
) TYPE = INNODB;

#DROP TABLE ClusterSet;
CREATE TABLE ClusterSet (
  clusterSetName varchar(50),
  description  varchar(50),
  location varchar(50),
  longitude float,
  latitude float,
  admin varchar(50),
  version   bigint(20),
  PRIMARY KEY( clusterSetName )
) TYPE = INNODB;

#DROP TABLE Cluster;
CREATE TABLE Cluster (
  clusterName  varchar(20),
  descr        varchar(50),
  clusterSetName varchar(50) NOT NULL,
  version   bigint(20),
  PRIMARY KEY( clusterName ),
  FOREIGN KEY (clusterSetName) REFERENCES ClusterSet(clusterSetName) 
) TYPE = INNODB;



#DROP TABLE NodeSet;
CREATE TABLE NodeSet (
  nodeSetName varchar(10),
  nodeTypeName varchar(10),
  nodeCount integer,
  cluster varchar(20),
  version   bigint(20),
  PRIMARY KEY( nodeSetName ),
  FOREIGN KEY (cluster) REFERENCES Cluster(clusterName) ,
  FOREIGN KEY (nodeTypeName) REFERENCES NodeType(nodeTypeName)
) TYPE = INNODB;

DROP TABLE NodeState;
CREATE TABLE NodeState (
  state    varchar(10),
  version   bigint(20),
  PRIMARY KEY( state    )
) TYPE = INNODB;

# Static data
INSERT INTO `NodeState` (`state`,version) VALUES
('OK',0),
('HARDWARE_FAULT',1),
('SOFTWARE_FAULT',2),
('ONLINE',3),
('OFFLINE',4);

DROP TABLE Node;
#SET FOREIGN_KEY_CHECKS = 0;
CREATE TABLE Node (
  nodeName varchar(10),
  nodeSetName varchar(10),
  description  varchar(50),
  state    varchar(10),
  version   bigint(20),
  PRIMARY KEY( nodeName )
# , FOREIGN KEY (nodeSetName) REFERENCES NodeSet(nodeSetName)
#  FOREIGN KEY (state ) REFERENCES NodeState(state)
) TYPE = INNODB;

ALTER TABLE Node ADD FOREIGN KEY (nodeSetName) REFERENCES NodeSet(nodeSetName);
ALTER TABLE Node ADD FOREIGN KEY (state) REFERENCES NodeState(state);
ALTER TABLE Node ADD FOREIGN KEY (state) REFERENCES NodeSet(nodeSetName);



#SET FOREIGN_KEY_CHECKS = 1;




