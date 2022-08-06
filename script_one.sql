SELECT villains.name, villains.evilness_factor, count
FROM minions_bd.villains, (SELECT villain_id, count(minions_bd.minions_villains.minion_id) AS count 
FROM minions_bd.minions_villains
GROUP BY minions_bd.minions_villains.villain_id
HAVING count > 0) AS countMinions
WHERE villains.id = countMinions.villain_id
ORDER BY count DESC
/*
(SELECT villain_id, count(minions_bd.minions_villains.minion_id) AS count 
FROM minions_bd.minions_villains
GROUP BY minions_bd.minions_villains.villain_id
HAVING count > 3) AS tbl
*/