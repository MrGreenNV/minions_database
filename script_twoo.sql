SELECT name, age FROM (SELECT * FROM minions_bd.minions
	JOIN minions_bd.minions_villains
	ON id = minion_id) AS result
WHERE villain_id = 5
ORDER BY name