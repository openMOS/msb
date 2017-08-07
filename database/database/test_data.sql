-- TESTES DE INSERTS

-- DEVICES
INSERT INTO DeviceAdapter (name, short_description, long_description, protocol, client_id, agent_id) VALUES ("MSB", "MSB", "MSB", "NONE", 0, 0);
INSERT INTO DeviceAdapter (name, short_description, long_description, protocol, client_id, agent_id) VALUES ("Workstation", "cenas da workstation", "mais cenas", "OPC", 1, 1);
INSERT INTO DeviceAdapter (name, short_description, long_description, protocol, client_id, agent_id) VALUES ("AGV", "cenas do agv", "mais cenas", "DDS", 2, 2);
INSERT INTO DeviceAdapter (name, short_description, long_description, protocol, client_id, agent_id) VALUES ("Device_3", "cenas do d3", "mais cenas", "OPC", 3, 3);


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
INSERT INTO Recipe (aml_id, da_id, sk_id, name, endpoint) VALUES("abae31de01", 2, 10, "Recipe Task_Full_A", "address");
INSERT INTO Recipe (aml_id, da_id, sk_id, name, endpoint) VALUES("abae31de02", 4, 11, "Recipe Task_Full_C", "address");
INSERT INTO Recipe (aml_id, da_id, sk_id, name, endpoint) VALUES("abae31de03", 2, 9,  "Recipe Task_Full_B", "address");
INSERT INTO Recipe (aml_id, da_id, sk_id, name, endpoint) VALUES("abae31de04", 3, 6,  "Recipe TransportAB", "address");
INSERT INTO Recipe (aml_id, da_id, sk_id, name, endpoint) VALUES("abae31de05", 3, 7,  "Recipe TransportCB", "address");
INSERT INTO Recipe (aml_id, da_id, sk_id, name, endpoint) VALUES("abae31de06", 3, 8,  "Recipe TransportBD", "address");

-- RECIPE DOS PRODUTOS
INSERT INTO Recipe (aml_id, da_id, sk_id, name, endpoint) VALUES ("abfe31de07", 1, 12, "RECIPE_PRODUTO_A", "MSB"); 
INSERT INTO Recipe (aml_id, da_id, sk_id, name, endpoint) VALUES ("abfe31de08", 1, 13, "RECIPE_PRODUTO_B", "MSB"); 
INSERT INTO Recipe (aml_id, da_id, sk_id, name, endpoint) VALUES ("abfe31de09", 1, 14, "RECIPE_PRODUTO_C", "MSB"); 
INSERT INTO Recipe (aml_id, da_id, sk_id, name, endpoint) VALUES ("abfe31de10", 1, 14, "RECIPE_PRODUTO_C", "MSB"); 


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


-- ********************************************************************************************** 
-- TESTES DE PESQUISAS

-- Vê as skills que estão disponiveis no AGV
SELECT DeviceAdapter.id, DeviceAdapter.name, Skill.name 
FROM Skill, DeviceAdapter, DAS 
WHERE DeviceAdapter.id = DAS.da_id AND Skill.id = DAS.sk_id AND DeviceAdapter.name = "AGV";

-- Associa a skill à receita
SELECT Recipe.id, Recipe.name, Skill.name 
FROM Skill, Recipe
WHERE Recipe.sk_id = Skill.id AND Skill.name = "Task_Full_B";

-- Vê qual dos devices consegue executar determinada skill
SELECT DeviceAdapter.name, DeviceAdapter.id
FROM DeviceAdapter, DAS, Skill
WHERE DAS.da_id = DeviceAdapter.id AND Skill.id = DAS.sk_id AND Skill.name = "Task_Full_A";


-- Através do nome da Skill, vai procurar qual a receita associada (falta ver se esta activa) e lista
-- as skill requirements dessa recetia
SELECT Skill.id, Skill.name
FROM Skill, SR, Recipe
WHERE SR.r_id = Recipe.id 
AND SR.sk_id = Skill.id 
AND Recipe.sk_id = (SELECT Skill.id 
                    FROM Recipe, Skill
                    WHERE Skill.id = Recipe.sk_id 
                    AND Skill.name = "PRODUTO_A")


-- A mesma coisa da de cima mas directamente através do nome da receita
SELECT Skill.id, Skill.name
FROM Skill, SR, Recipe
WHERE SR.r_id = Recipe.id AND SR.sk_id = Skill.id AND Recipe.name = "RECIPE_PRODUTO_A"

