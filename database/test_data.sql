-- TESTES DE INSERTS

-- DEVICES
INSERT INTO DeviceAdapter (name, short_description, long_description, protocol) VALUES ("MSB", "MSB", "MSB", "NONE");
INSERT INTO DeviceAdapter (name, short_description, long_description, protocol) VALUES ("Workstation", "cenas da workstation", "mais cenas", "OPC");
INSERT INTO DeviceAdapter (name, short_description, long_description, protocol) VALUES ("AGV", "cenas do agv", "mais cenas", "DDS");
INSERT INTO DeviceAdapter (name, short_description, long_description, protocol) VALUES ("Device_3", "cenas do d3", "mais cenas", "OPC");


-- SKILLS
-- skills dos devices
INSERT INTO Skill (aml_id, name, description) VALUES ("abfe31de01", "weld_1", "cenas do weld");
INSERT INTO Skill (aml_id, name, description) VALUES ("abfe31de02", "weld_2","cenas do weld");
INSERT INTO Skill (aml_id, name, description) VALUES ("abfe31de03", "Drop", "cenas do drop");
INSERT INTO Skill (aml_id, name, description) VALUES ("abfe31de04", "Pick", "cenas do pick");
INSERT INTO Skill (aml_id, name, description) VALUES ("abfe31de05", "Inspect", "Vision system");
INSERT INTO Skill (aml_id, name, description) VALUES ("abfe31de06", "TransportAB", "cenas do transport");
INSERT INTO Skill (aml_id, name, description) VALUES ("abfe31de07", "TransportCB", "cenas do transport");
INSERT INTO Skill (aml_id, name, description) VALUES ("abfe31de08", "TransportBD", "cenas do transport");

INSERT INTO Skill (aml_id, name, description) VALUES ("abfe31de09", "Task_Full_B", "pick + weld_1 + drop");
INSERT INTO Skill (aml_id, name, description) VALUES ("abfe31de10", "Task_Full_A", "pick + weld_1 + inspect + drop");
INSERT INTO Skill (aml_id, name, description) VALUES ("abfe31de11", "Task_Full_C", "pick + weld_2 + drop");

-- SKILL PRODUTO
INSERT INTO Skill (aml_id, name, description) VALUES ("abfe31de12", "PRODUTO_A", "TransportAB + Task_Full_A + TransportBD");
INSERT INTO Skill (aml_id, name, description) VALUES ("abfe31de13", "PRODUTO_B", "TransportCB + Task_Full_B + TransportBD");
INSERT INTO Skill (aml_id, name, description) VALUES ("abfe31de14", "PRODUTO_C", "TransportAB + Task_Full_C + TransportBD");


-- RECIPE DAS SKILLS DOS DA
INSERT INTO Recipe (aml_id, da_id, sk_id, valid, name) VALUES("abae31de1", 2, 10, 1, "Recipe Task_Full_A");
INSERT INTO Recipe (aml_id, da_id, sk_id, valid, name) VALUES("abae31de2", 4, 11, 1, "Recipe Task_Full_C");
INSERT INTO Recipe (aml_id, da_id, sk_id, valid, name) VALUES("abae31de3", 2, 9, 1, "Recipe Task_Full_B");
INSERT INTO Recipe (aml_id, da_id, sk_id, valid, name) VALUES("abae31de4", 3, 6, 1, "Recipe TransportAB");
INSERT INTO Recipe (aml_id, da_id, sk_id, valid, name) VALUES("abae31de5", 3, 7, 1, "Recipe TransportCB");
INSERT INTO Recipe (aml_id, da_id, sk_id, valid, name) VALUES("abae31de6", 3, 8, 1, "Recipe TransportBD");

-- RECIPE DOS PRODUTOS
INSERT INTO Recipe (aml_id, da_id, sk_id, valid, name) VALUES ("abfe31de07", 1, 12, 1, "RECIPE_PRODUTO_A"); 
INSERT INTO Recipe (aml_id, da_id, sk_id, valid, name) VALUES ("abfe31de08", 1, 13, 1, "RECIPE_PRODUTO_B"); 
INSERT INTO Recipe (aml_id, da_id, sk_id, valid, name) VALUES ("abfe31de09", 1, 14, 1, "RECIPE_PRODUTO_C"); 
INSERT INTO Recipe (aml_id, da_id, sk_id, valid, name) VALUES ("abfe31de10", 1, 14, 0, "RECIPE_PRODUTO_C"); 


-- DEVICES 
INSERT INTO Device (da_id, status, name, address) VALUES (2, 1, "PLC", "//aaa1");
INSERT INTO Device (da_id, status, name, address) VALUES (2, 1, "Robot", "//aaa2");
INSERT INTO Device (da_id, status, name, address) VALUES (2, 1, "Vision", "//aaa3");
INSERT INTO Device (da_id, status, name, address) VALUES (2, 1, "Wago", "//aaa4");


-- DeviceAdapter to SKILL
INSERT INTO DAS (da_id, sk_id) VALUES (1, 12);
INSERT INTO DAS (da_id, sk_id) VALUES (1, 13);
INSERT INTO DAS (da_id, sk_id) VALUES (1, 14);

-- Workstation A
INSERT INTO DAS (da_id, sk_id) VALUES (2, 1);
INSERT INTO DAS (da_id, sk_id) VALUES (2, 2);
INSERT INTO DAS (da_id, sk_id) VALUES (2, 3);
INSERT INTO DAS (da_id, sk_id) VALUES (2, 4);
INSERT INTO DAS (da_id, sk_id) VALUES (2, 5);
INSERT INTO DAS (da_id, sk_id) VALUES (2, 9);
INSERT INTO DAS (da_id, sk_id) VALUES (2, 10);


-- Workstation B
INSERT INTO DAS (da_id, sk_id) VALUES (4, 2);
INSERT INTO DAS (da_id, sk_id) VALUES (4, 3);
INSERT INTO DAS (da_id, sk_id) VALUES (4, 4);
INSERT INTO DAS (da_id, sk_id) VALUES (4, 11);

-- AGV
INSERT INTO DAS (da_id, sk_id) VALUES (3, 6);
INSERT INTO DAS (da_id, sk_id) VALUES (3, 7);
INSERT INTO DAS (da_id, sk_id) VALUES (3, 8);



-- Skill requirements
-- Task Full A
INSERT INTO SR (r_id, sk_id) VALUES (1, 4);
INSERT INTO SR (r_id, sk_id) VALUES (1, 1);
INSERT INTO SR (r_id, sk_id) VALUES (1, 5);
INSERT INTO SR (r_id, sk_id) VALUES (1, 3);

-- Task Full C
INSERT INTO SR (r_id, sk_id) VALUES (3, 4);
INSERT INTO SR (r_id, sk_id) VALUES (3, 1);
INSERT INTO SR (r_id, sk_id) VALUES (3, 3);

-- Transport AB
INSERT INTO SR (r_id, sk_id) VALUES (4, 6);
-- Transport CB
INSERT INTO SR (r_id, sk_id) VALUES (5, 7);

-- Produto A
INSERT INTO SR (r_id, sk_id) VALUES (7, 6);
INSERT INTO SR (r_id, sk_id) VALUES (7, 10);
INSERT INTO SR (r_id, sk_id) VALUES (7, 8);

-- Produto B
INSERT INTO SR (r_id, sk_id) VALUES (8, 7);
INSERT INTO SR (r_id, sk_id) VALUES (8, 9);
INSERT INTO SR (r_id, sk_id) VALUES (8, 8);

-- Produto C
INSERT INTO SR (r_id, sk_id) VALUES (9, 6);
INSERT INTO SR (r_id, sk_id) VALUES (9, 11);
INSERT INTO SR (r_id, sk_id) VALUES (9, 8);

-- Produto C2
INSERT INTO SR (r_id, sk_id) VALUES (10, 6);
INSERT INTO SR (r_id, sk_id) VALUES (10, 10);
INSERT INTO SR (r_id, sk_id) VALUES (10, 8);








