# minions_database
Interaction with the local database

1. The names of the villains.
Write a program that outputs to the console the names of all those villains and the number of minions who have more than "n" minions. The list is presented in descending order of the number of minions.

2. The names of the Minions.
Write a program that outputs to the console all the names of the minions and the age for the selected villain Id. Sort by name in alphabetical order.
If there is no villain with the specified ID, output "There is no villain with the ID <villain_id> in the database."
If the selected villain has no minions, output "(no minions)" in the second line.

3. Adding Minions.
Write a program that reads information about the minion and his villain and adds it to the database. In case the minion's city is not in the database, insert it as well. In case the villain is not in the database, add him too with the default degree of malice "evil". Finally, add a new minion to become the villain's servant. Display the appropriate messages after each operation.
Input
The input data comes in two lines:
• In the first line you get information about the minion in the format "Minion: <name> <age> <city name>"
• In the second - information about the villain in the format "Villain: <name>"
Output
After the operation is completed, you need to print one of the following messages:
• After adding a new city to the database: "City <TownName> has been added to the database."
• After adding a new villain to the database: "Villain <villain name> has been added to the database."
• Finally, after successfully adding a minion to the database and turning it into a servant of the villain: "Successfully added <MinionName> to be a minion of <VillainName>."

4. Removing the villain.
Write a program that removes the villain by id and frees his former minions from serving him. Print the name of the removed villain and the number of released minions. If the villain is not found, report it.
