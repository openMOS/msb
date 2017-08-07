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

-- Receitas disponiveis num device
SELECT Recipe.aml_id, Recipe.name 
FROM Recipe, DeviceAdapter 
WHERE Recipe.da_id = DeviceAdapter.id AND DeviceAdapter.name = "Workstation";

SELECT Device.name
FROM Device, DeviceAdapter
WHERE Device.da = DeviceAdapter.id AND DeviceAdapter.name = "Workstation";
