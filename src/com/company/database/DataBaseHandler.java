package com.company.database;

import com.company.auxiliary.ConstTable;
import com.company.essence.Minion;
import com.company.essence.Villains;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс с методами для соединения и взаимодействия с таблицами базы данных.
 */

public class DataBaseHandler extends Configs {

    Connection dbConnection = getDBConnection();

    /**
     * Подключение к БД.
     * @return - возвращает драйвер для соединения с БД.
     */
    public Connection getDBConnection() {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?" + dbTimeZone;
        try {
            dbConnection = DriverManager.getConnection(connectionString, getProps());
        } catch (SQLException e) {
            System.out.println("Не удалось подключиться к базе данных!");
            return null;
        }
        return dbConnection;
    }

    /**
     * Получение коллекции типа Villain с заданным условием.
     * @param countMinions - количество миньонов.
     * @return - возвращает коллекцию типа Villain.
     */
    public List<Villains> getVillainsList(int countMinions) {
        List<Villains> villainsList = new ArrayList<>();

        ResultSet resSet = null;

        String selectBD = "SELECT " + ConstTable.VILLAINS_TABLE + "." + ConstTable.VILLAINS_NAME + ", " + ConstTable.VILLAINS_TABLE + "." + ConstTable.VILLAINS_EVILNESS_FACTOR + ", " + "count \n"
                + "FROM " + ConstTable.MINIONS_BD + "." + ConstTable.VILLAINS_TABLE + ", (SELECT " + ConstTable.MINIONS_VILLAINS_VILLAIN_ID
                    + ", count(" + ConstTable.MINIONS_BD + "." + ConstTable.MINIONS_VILLAINS_TABLE + "." + ConstTable.MINIONS_VILLAINS_MINION_ID + ") AS count \n"
                + "FROM " + ConstTable.MINIONS_BD + "." + ConstTable.MINIONS_VILLAINS_TABLE + "\n"
                + "GROUP BY " + ConstTable.MINIONS_BD + "." + ConstTable.MINIONS_VILLAINS_TABLE + "." + ConstTable.MINIONS_VILLAINS_VILLAIN_ID + "\n"
                + "HAVING count > " + countMinions + ") AS countMinions \n"
                + "WHERE " + ConstTable.VILLAINS_TABLE + "." + ConstTable.VILLAINS_ID + " = countMinions.villain_id \n"
                + "ORDER BY count DESC";

        PreparedStatement prSt;

        try {
            prSt = dbConnection.prepareStatement(selectBD);
            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("С запросом имен злодеев что то не то!");
        }

        while (true) {
            try {
                if (!resSet.next()) {
                    break;
                }

                villainsList.add(new Villains(resSet.getString(ConstTable.VILLAINS_NAME), resSet.getString(ConstTable.VILLAINS_EVILNESS_FACTOR), resSet.getInt("count")));

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return villainsList;
    }

    /**
     * Получение имени Villain по его ID
     * @param villains_id - ID Villain
     * @return - возвращает строковое представление имени.
     */
    public String getVillainName(int villains_id) {
        String villainName = "";
        ResultSet resultSet = null;

        String selectBD = "SELECT " + ConstTable.VILLAINS_NAME + "\n" +
                "FROM " + ConstTable.MINIONS_BD + "." + ConstTable.VILLAINS_TABLE + "\n" +
                "WHERE " + ConstTable.VILLAINS_ID + " = " + villains_id;

        PreparedStatement prSt;

        try {
            prSt = dbConnection.prepareStatement(selectBD);
            resultSet = prSt.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("В запросе villains_id что то не то!");
        }

        while (true) {
            try {
                if (!resultSet.next()) {
                    break;
                }
                villainName = resultSet.getString(ConstTable.VILLAINS_NAME);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return villainName;
    }
    /**
     * Получение коллекции типа Minion с заданным условием.
     * @param villains_id - ID Villain.
     * @return - возвращает коллекцию типа Minion.
     */
    public List<Minion> getMinionsList(int villains_id) {
        List<Minion> minionList = new ArrayList<>();

        ResultSet resultSet = null;

        String selectBD = "SELECT " + ConstTable.MINIONS_NAME + ", " + ConstTable.MINIONS_AGE + "\n" +
                "FROM (SELECT * FROM " + ConstTable.MINIONS_BD + "." + ConstTable.MINIONS_TABLE + "\n" +
                "JOIN " + ConstTable.MINIONS_BD + "." + ConstTable.MINIONS_VILLAINS_TABLE + "\n" +
                "ON " + ConstTable.MINIONS_ID + " = " + ConstTable.MINIONS_VILLAINS_MINION_ID + ") AS result\n" +
                "WHERE " + ConstTable.MINIONS_VILLAINS_VILLAIN_ID + " = " + villains_id + "\n" +
                "ORDER BY " + ConstTable.MINIONS_NAME;

        PreparedStatement prSt;

        try {
            prSt = dbConnection.prepareStatement(selectBD);
            resultSet = prSt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("С запросом миньонов что то не то!");
        }

        while (true) {
            try {
                if (!resultSet.next()) {
                    System.out.println();
                    break;
                }

                minionList.add(new Minion(resultSet.getString(ConstTable.MINIONS_NAME), resultSet.getInt(ConstTable.MINIONS_AGE), null));

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return minionList;
    }

    /**
     * Проверка наличия города в БД
     * @param nameTown - наименование города
     * @return - возвращает истину в случае наличия города в БД, иначе - ложь.
     */
    public boolean isTownInBD(String nameTown) {
        List<String> nameTownsList = getNameTownsList();
        return nameTownsList.contains(nameTown);
    }

    /**
     * Получение списка наименований городов из БД
     * @return - возвращает список строк наименований городов.
     */
    private List<String> getNameTownsList() {
        List<String> nameTownsList = new ArrayList<>();
        ResultSet resultSet = null;
        String selectBD = "SELECT " + ConstTable.TOWNS_NAME + "\n" +
                "FROM " + ConstTable.MINIONS_BD + "." + ConstTable.TOWNS_TABLE;
        PreparedStatement prSt;

        try {
            prSt = dbConnection.prepareStatement(selectBD);
            resultSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                if (!resultSet.next()) {
                    break;
                }

                nameTownsList.add(resultSet.getString(ConstTable.TOWNS_NAME));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return nameTownsList;
    }

    /**
     * Проверка наличия злодея в БД
     * @param nameVillain - имя злодея
     * @return - возвращает истину в случае наличия злодея в БД, иначе - ложь.
     */
    public boolean isVillainInBD(String nameVillain) {
        List<String> nameTownsList = getNameVillainsList();
        return nameTownsList.contains(nameVillain);
    }

    /**
     * Проверка наличия злодея в БД
     * @param villainID - ID злодея
     * @return - возвращает истину в случае наличия злодея в БД, иначе - ложь.
     */
    public boolean isVillainInBD(int villainID) {
        List<Integer> nameTownsList = getIDVillainsList();
        return nameTownsList.contains(villainID);
    }

    /**
     * Получение списка имен злодеев из БД
     * @return - возвращает список строк имен городов.
     */
    private List<String> getNameVillainsList() {
        List<String> nameVillainsList = new ArrayList<>();
        ResultSet resultSet = null;
        String selectBD = "SELECT " + ConstTable.VILLAINS_NAME + "\n" +
                "FROM " + ConstTable.MINIONS_BD + "." + ConstTable.VILLAINS_TABLE;
        PreparedStatement prSt;

        try {
            prSt = dbConnection.prepareStatement(selectBD);
            resultSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                if (!resultSet.next()) {
                    break;
                }

                nameVillainsList.add(resultSet.getString(ConstTable.VILLAINS_NAME));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return nameVillainsList;
    }

    /**
     * Получение списка имен злодеев из БД
     * @return - возвращает список ID городов.
     */
    private List<Integer> getIDVillainsList() {
        List<Integer> villainsIDList = new ArrayList<>();
        ResultSet resultSet = null;
        String selectBD = "SELECT " + ConstTable.VILLAINS_ID + "\n" +
                "FROM " + ConstTable.MINIONS_BD + "." + ConstTable.VILLAINS_TABLE;
        PreparedStatement prSt;

        try {
            prSt = dbConnection.prepareStatement(selectBD);
            resultSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                if (!resultSet.next()) {
                    break;
                }

                villainsIDList.add(resultSet.getInt(ConstTable.VILLAINS_ID));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return villainsIDList;
    }

    /**
     * Добавляет города в БД.
     * @param nameTown - наименование города
     * @param country - наименование страны
     */
    public void addTownInBD(String nameTown, String country) {
        String insertBD = "INSERT INTO " + ConstTable.MINIONS_BD + "." + ConstTable.TOWNS_TABLE + "(" + ConstTable.TOWNS_NAME + ", " + ConstTable.TOWNS_COUNTRY + ")" + "\n" + "VALUES(?, ?)";

        PreparedStatement prSt;

        try {
            prSt = dbConnection.prepareStatement(insertBD);
            prSt.setString(1, nameTown);
            prSt.setString(2, country);

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавляет злодея в БД
     * @param nameVillain - имя злодея
     */
    public void addVillainInBD(String nameVillain) {
        String insertBD = "INSERT INTO " + ConstTable.MINIONS_BD + "." + ConstTable.VILLAINS_TABLE + "(" + ConstTable.VILLAINS_NAME + ", " + ConstTable.VILLAINS_EVILNESS_FACTOR + ")" + "\n" + "VALUES(?, ?)";

        PreparedStatement prSt;

        try {
            prSt = dbConnection.prepareStatement(insertBD);
            prSt.setString(1, nameVillain);
            prSt.setString(2, "evil");

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавляет миньона в БД
     * @param nameMinion - имя миньона
     * @param ageMinion - возраст миньона
     * @param nameTown - наименование города
     * @param nameVillain - имя злодея
     */
    public void addMinionInBD(String nameMinion, int ageMinion, String nameTown, String nameVillain) {

        int townID = getTownID(nameTown);
        if (townID < 0) {
            System.out.println("Ошибка при получении ID города!");
            return;
        }

        // Добавление миньона в таблицу миньонов
        String insertMinionInBD = "INSERT INTO " + ConstTable.MINIONS_BD + "." + ConstTable.MINIONS_TABLE + "(" + ConstTable.MINIONS_NAME + ", " +
                ConstTable.MINIONS_AGE + ", " + ConstTable.MINIONS_TOWN_ID + ")" + "\n" + "VALUES(?, ?, ?)";

        PreparedStatement prSt;

        try {
            prSt = dbConnection.prepareStatement(insertMinionInBD);
            prSt.setString(1, nameMinion);
            prSt.setInt(2, ageMinion);
            prSt.setInt(3, townID);

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int minionID = getMinionID(nameMinion);
        if (minionID < 0) {
            System.out.println("Ошибка при получении ID миньона!");
            return;
        }

        int villainID = getVillainID(nameVillain);
        if (villainID < 0) {
            System.out.println("Ошибка при получении ID злодея!");
            return;
        }

        // Добавление ID миньонов и ID злодеев в общую для них таблицу.
        String insertIDInBD = "INSERT INTO " + ConstTable.MINIONS_BD + "." + ConstTable.MINIONS_VILLAINS_TABLE + "(" + ConstTable.MINIONS_VILLAINS_MINION_ID + ", " +
                ConstTable.MINIONS_VILLAINS_VILLAIN_ID + ")" + "\n" + "VALUES(?, ?)";

        try {
            prSt = dbConnection.prepareStatement(insertIDInBD);
            prSt.setInt(1, minionID);
            prSt.setInt(2, villainID);

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Получение ID злодея по имени
     * @param nameVillain- наименование города
     * @return - возвращает ID города
     */
    private int getVillainID(String nameVillain) {
        int villainID = -1;

        ResultSet resultSet = null;

        String selectBD = "SELECT " + ConstTable.VILLAINS_ID + "\n" +
                "FROM " + ConstTable.MINIONS_BD + "." + ConstTable.VILLAINS_TABLE + "\n" +
                "WHERE " + ConstTable.VILLAINS_NAME + " = " + "'" + nameVillain + "'";

        PreparedStatement prSt;

        try {
            prSt = dbConnection.prepareStatement(selectBD);
            resultSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                if (!resultSet.next()) {
                    break;
                }
                villainID = resultSet.getInt(ConstTable.VILLAINS_ID);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return villainID;
    }

    /**
     * Получение ID миньона по имени
     * @param nameMinion- наименование города
     * @return - возвращает ID города
     */
    private int getMinionID(String nameMinion) {
        int minionID = -1;

        ResultSet resultSet = null;

        String selectBD = "SELECT " + ConstTable.MINIONS_ID + "\n" +
                "FROM " + ConstTable.MINIONS_BD + "." + ConstTable.MINIONS_TABLE + "\n" +
                "WHERE " + ConstTable.MINIONS_NAME + " = " + "'" + nameMinion + "'";

        PreparedStatement prSt;

        try {
            prSt = dbConnection.prepareStatement(selectBD);
            resultSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();

        }


        while (true) {
            try {
                if (!resultSet.next()) {
                    break;
                }
                minionID = resultSet.getInt(ConstTable.MINIONS_ID);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return minionID;
    }

    /**
     * Получение ID города по наименованию
     * @param nameTown - наименование города
     * @return - возвращает ID города
     */
    private int getTownID(String nameTown) {
        int townID = -1;

        ResultSet resultSet = null;

        String selectBD = "SELECT " + ConstTable.TOWNS_ID + "\n" +
                "FROM " + ConstTable.MINIONS_BD + "." + ConstTable.TOWNS_TABLE + "\n" +
                "WHERE " + ConstTable.TOWNS_NAME + " = " + "'" + nameTown + "'";

        PreparedStatement prSt;

        try {
            prSt = dbConnection.prepareStatement(selectBD);
            resultSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                if (!resultSet.next()) {
                    break;
                }
                townID = resultSet.getInt(ConstTable.TOWNS_ID);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return townID;
    }

    /**
     * Удаление злодея из БД
     * @param villainID - ID злодея.
     */
    public void deleteVillainWithBD(int villainID) {
        if (!isVillainInBD(villainID)) {
            System.out.println("Злодей с таким id не найден!");
            return;
        }

        String deleteBDFromVillainsTable = "DELETE FROM " + ConstTable.MINIONS_BD + "." + ConstTable.VILLAINS_TABLE + "\n" +
                "WHERE " + ConstTable.VILLAINS_ID + " = " + "'" + villainID + "'";

        String deleteBDFromMinionsAndVillainsTable = "DELETE FROM " + ConstTable.MINIONS_BD + "." + ConstTable.MINIONS_VILLAINS_TABLE + "\n" +
                "WHERE " + ConstTable.MINIONS_VILLAINS_VILLAIN_ID + " = " + "'" + villainID + "'";

        PreparedStatement prSt;

        try {
            prSt = dbConnection.prepareStatement(deleteBDFromMinionsAndVillainsTable);
            prSt.executeUpdate();

            prSt = dbConnection.prepareStatement(deleteBDFromVillainsTable);
            prSt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получение количество миньонов, служащих у злодея
     * @param villainID - ID злодея
     * @return - целое число миньонов.
     */
    public int getCountMinion(int villainID) {
        int countMinions = -1;

        ResultSet resultSet = null;

        String selectBD = "SELECT COUNT(" + ConstTable.MINIONS_VILLAINS_MINION_ID + ")\n" +
                "FROM " + ConstTable.MINIONS_BD + "." + ConstTable.MINIONS_VILLAINS_TABLE + "\n" +
                "GROUP BY " + ConstTable.MINIONS_VILLAINS_VILLAIN_ID + "\n" +
                "HAVING " + ConstTable.MINIONS_VILLAINS_VILLAIN_ID + " = " + villainID;

        PreparedStatement prSt;

        try {
            prSt = dbConnection.prepareStatement(selectBD);
            resultSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                if (!resultSet.next()) {
                    break;
                }
                countMinions = resultSet.getInt("COUNT(" + ConstTable.MINIONS_VILLAINS_MINION_ID + ")");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return countMinions;
    }
}
