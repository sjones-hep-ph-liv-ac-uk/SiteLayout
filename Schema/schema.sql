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
CREATE TABLE Node (
  nodeName varchar(10),
  nodeSetName varchar(10),
  description  varchar(50),
  state    varchar(10),
  version   bigint(20),
  PRIMARY KEY( nodeName )
) TYPE = INNODB;

ALTER TABLE Node ADD FOREIGN KEY (nodeSetName) REFERENCES NodeSet(nodeSetName);
ALTER TABLE Node ADD FOREIGN KEY (state) REFERENCES NodeState(state);

DROP TABLE HostSystem;
CREATE TABLE HostSystem (
  hostname    varchar(50),
  cpu       bigint(20),
  mem       bigint(20),
  os        varchar(50),
  kernel    varchar(50),
  disk      bigint(20),
  comment   varchar(50),
  version   bigint(20),
  PRIMARY KEY( hostname    )
) TYPE = INNODB;

# Static data
INSERT INTO HostSystem (hostname,cpu,mem,os,kernel,comment,version) VALUES
('PHYSICAL',0 , 0,'     ',null ,null,0),
('hepvm1.ph.liv.ac.uk',24,50,'sl6.4','2.6',null,0),
('hepvm2.ph.liv.ac.uk',12, 0,'c7','2.6',null,0),
('hepgridvm1.ph.liv.ac.uk',8 ,16,'c7','2.6',null,0),
('hepvm3.ph.liv.ac.uk',24,50,'sl6.4','2.6',null,0);

DROP TABLE ServiceNode;
CREATE TABLE ServiceNode (
  hostname       varchar(50),
  hostSystemName varchar(50),
  clusterName  varchar(20),
  cpu       bigint(20),
  mem       bigint(20),
  os        varchar(50),
  kernel    varchar(50),
  service   varchar(50),
  comment   varchar(50),
  version   bigint(20),
  PRIMARY KEY( hostname )
) TYPE = INNODB;

ALTER TABLE ServiceNode ADD FOREIGN KEY (clusterName) REFERENCES Cluster (clusterName);
ALTER TABLE ServiceNode ADD FOREIGN KEY (hostSystemName) REFERENCES HostSystem(hostname);


#SET FOREIGN_KEY_CHECKS = 1;




