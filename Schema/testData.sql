
INSERT INTO `NodeType` (`nodeTypeName`, `cpu`, `slot`, `hs06PerSlot`, `memPerNode`,`version`) VALUES 
('BASELINE',NULL,NULL,10,NULL,0),
('E5-2630V2_condor',2,24,10.84,50,0),
('E5-2630V2_vac',2,22,11.86,50,0),
('E5-2630V3_condor',2,32,10.98,132,0),
('E5620_condor',2,12,10.62,25,0),
('E5620_vac',2,10,12.196,25,0),
('L5420_condor',2,8,8.83,16,0),
('L5530_vac',2,9,11.726,24,0),
('X5650_condor',2,24,8.69,50,0);

INSERT INTO `ClusterSet` (`clusterSetName`, `description`, `location`, `longitude`, `latitude`, `admin`,`version`) VALUES 
('AGGREGATE','AGGREGATE','NOWHERE',0,0,'gridteam@hep.ph.liv.ac.uk',0),
('CONDOR_C7','Condor C7 clusters','Liverpool',-2.964,53.4035,'gridteam@hep.ph.liv.ac.uk',0),
('CONDOR_SL6','Condor C7 cluster','University of Liverpool',-2.964,53.4035,'gridteam@hep.ph.liv.ac.uk',0),
('VAC','VAC clusters','Liverpool',-2.964,53.4035,'gridteam@hep.ph.liv.ac.uk',0);

INSERT INTO `Cluster` (`clusterName`, `descr`, `clusterSetName`,`version`) VALUES 
('AGG','AGG','AGGREGATE',0),
('CONDOR_C7','Centos7 Test','CONDOR_C7',0),
('CONDOR_SL6','Condor batch nodes in Hammer server room','CONDOR_SL6',0),
('VAC_SL6_LOCAL','Vac cloud nodes in the Hammer server room','VAC',0),
('VAC_SL6_REMOTE','Vac cloud nodes in the Chadwick server room','VAC',0);


INSERT INTO `NodeSet` (`nodeSetName`, `nodeTypeName`, `nodeCount`, `clusterName`,`version`) VALUES 
('comp5xx','L5530_vac',18,'VAC_SL6_REMOTE',0),
('r21_div1','E5620_condor',4,'CONDOR_SL6',0),
('r21_div2','X5650_condor',16,'CONDOR_SL6',0),
('r22','E5620_condor',20,'CONDOR_SL6',0),
('r23_div1','E5620_condor',10,'CONDOR_SL6',0),
('r23_div2','E5620_vac',10,'VAC_SL6_LOCAL',0),
('r24','E5620_vac',20,'VAC_SL6_LOCAL',0),
('r25_div1','E5-2630V2_vac',10,'VAC_SL6_LOCAL',0),
('r25_div2','E5-2630V2_condor',10,'CONDOR_SL6',0),
('r26_div1','E5-2630V2_condor',4,'CONDOR_SL6',0),
('r26_div2','E5-2630V3_condor',5,'CONDOR_SL6',0),
('r26_div3','L5420_condor',3,'CONDOR_SL6',0),
('r26_div4','L5420_condor',3,'CONDOR_C7',0);

