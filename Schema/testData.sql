INSERT INTO nodeType VALUES ("L5530"   ,  2,    7,    13.370 ,  3.4285);
INSERT INTO nodeType VALUES ("L5420"   ,  2,    8,     8.960 ,  2.000);
INSERT INTO nodeType VALUES ("E5620"   ,  2,   10,    12.050 ,  2.500);
INSERT INTO nodeType VALUES ("X5650"   ,  2,   16,    12.290 ,  3.125);
INSERT INTO nodeType VALUES ("E5-2630" ,  2,   18,    13.860 ,  2.778);
INSERT INTO nodeType VALUES ("E5-2630V3" ,  2,   32,  11.0725 ,  4.12633);
INSERT INTO nodeType VALUES ("BASELINE",  null , null,  10.000 , null);

INSERT INTO cluster VALUES ("VAC_CLOUD_CHADWICK","Vac cloud nodes in the Chadwick server room");
INSERT INTO cluster VALUES ("VAC_CLOUD_HAMMER","Vac cloud nodes in the Hammer server room");
INSERT INTO cluster VALUES ("TORQUE_BATCH_HAMMER","Torque batch nodes in Hammer server room");
INSERT INTO cluster VALUES ("CONDOR_BATCH_HAMMER","Condor batch nodes in Hammer server room");

INSERT INTO nodeSet VALUES ("comp5xx", "L5530", 18,  "VAC_CLOUD_CHADWICK");
INSERT INTO nodeSet VALUES ("21", "E5620"     ,  4,  "CONDOR_BATCH_HAMMER");
INSERT INTO nodeSet VALUES ("21X", "X5650"    , 16,  "CONDOR_BATCH_HAMMER");
INSERT INTO nodeSet VALUES ("22", "E5620"     , 20,  "CONDOR_BATCH_HAMMER");
INSERT INTO nodeSet VALUES ("23p1", "E5620"   , 10,  "CONDOR_BATCH_HAMMER");
INSERT INTO nodeSet VALUES ("23p2", "E5620"   , 10,  "VAC_CLOUD_HAMMER");
INSERT INTO nodeSet VALUES ("24", "E5620"     , 20,  "VAC_CLOUD_HAMMER");
INSERT INTO nodeSet VALUES ("25", "E5-2630"   , 20,  "TORQUE_BATCH_HAMMER");
INSERT INTO nodeSet VALUES ("26", "E5-2630"   ,  4,  "CONDOR_BATCH_HAMMER");
INSERT INTO nodeSet VALUES ("26L", "L5420"    ,  7,  "CONDOR_BATCH_HAMMER");
INSERT INTO nodeSet VALUES ("26V", "E5-2630V3"    ,  5,  "CONDOR_BATCH_HAMMER");
