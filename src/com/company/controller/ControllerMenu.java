package com.company.controller;

import com.company.database.DataBaseHandler;
import com.company.essence.Minion;
import com.company.essence.Villains;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Класс контроллер для работы с Меню пользователя.
 */
public class ControllerMenu {
    DataBaseHandler dbHandler = new DataBaseHandler();
    private int choiceUser;
    private boolean flag = false;

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Основной цикл при работе с Меню пользователя.
     */
    public void workWithMenu() {
        while (!flag) {

            showMainMenu();
            choiceUser = getChoiceUser();

            switch (choiceUser) {
                case 1 -> {
                    int countMinions = -1;
                    try {
                        System.out.println();
                        System.out.println("Введите количество миньонов: ");
                        System.out.print("=> ");
                        countMinions = Integer.parseInt(reader.readLine());
                        if (countMinions >= 0) {
                            showNamesVillains(countMinions);
                            System.out.println();
                            System.out.println("Для продолжения нажмите Enter...");
                            reader.readLine();
                        } else {
                            System.out.println("Отрицательное число Миньонов!");
                        }
                    } catch (IOException | NumberFormatException e) {
                        System.out.println("Введите корректное значение!");
                    }
                }
                case 2 -> {
                    System.out.println();
                    System.out.println("Введите id злодея: ");
                    System.out.print("=> ");
                    try {
                        int villains_id = Integer.parseInt(reader.readLine());
                        if (villains_id < 1) {
                            throw new IOException();
                        }
                        showDataMinions(villains_id);
                    } catch (IOException e) {
                        System.out.println("Введите целое неотрицательное число!");
                    }
                }
                case 3 -> {
                    String nameMinion = "";
                    int ageMinion = -1;
                    String nameTown = "";
                    String nameVillain = "";

                    System.out.println();
                    System.out.println("Введите данные на нового миньона: ");
                    System.out.print("=> ");
                    try {
                        String[] dataSplit = reader.readLine().split(" ");
                        nameMinion = dataSplit[1];
                        ageMinion = Integer.parseInt(dataSplit[2]);
                        nameTown = dataSplit[3];
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println();
                    System.out.println("Введите данные на злодея: ");
                    System.out.print("=> ");
                    try {
                        String[] dataSplit = reader.readLine().split(" ");
                        nameVillain = dataSplit[1];
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    addNewMinion(nameMinion, ageMinion, nameTown, nameVillain);

                }
                case 4 -> {
                    int villainID = -1;
                    try {
                        System.out.println("Введите ID злодея для его удаления и освобождения миньонов!");
                        System.out.print("=> ");
                        villainID = Integer.parseInt(reader.readLine());

                        deleteVillain(villainID);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case 0 -> {
                    System.out.println("Программа завершена.");
                    flag = true;
                }
                default -> {
                    System.out.println("Введите корректное значение!");
                }
            }

        }
    }

    /**
     * Удаление и вывод информации об удалении
     * @param villainID - ID злодея.
     */
    private void deleteVillain(int villainID) {
        if (villainID > 0) {
            String nameVillain = dbHandler.getVillainName(villainID);
            int countMinion = dbHandler.getCountMinion(villainID);
            dbHandler.deleteVillainWithBD(villainID);
            System.out.println(nameVillain + " был удалён " + countMinion + " миньон(-а -ов) были освобождены!");
        } else {
            System.out.println("Введен некорректный ID злодея");
        }
    }

    /**
     * Добавление нового миньона.
     * @param nameMinion - имя миньона
     * @param ageMinion - возраст миньона
     * @param nameTown - название города
     * @param nameVillain - имя злодея.
     */
    private void addNewMinion(String nameMinion, int ageMinion, String nameTown, String nameVillain) {

        if (!dbHandler.isTownInBD(nameTown)) {
            System.out.println();
            System.out.println("Введите название страны: ");
            System.out.print("=> ");

            String country = "";

            try {
                country = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            dbHandler.addTownInBD(nameTown, country);
            System.out.println("Город " + nameTown + " был добавлен в базу данных.");
        }

        if (!dbHandler.isVillainInBD(nameVillain)) {
            dbHandler.addVillainInBD(nameVillain);
            System.out.println("Злодей " + nameVillain + " был добавлен в базу данных.");
        }

        dbHandler.addMinionInBD(nameMinion, ageMinion, nameTown, nameVillain);
        System.out.println("Успешно добавлен " + nameMinion + ", чтобы быть миньоном " + nameVillain + ".");

    }

    /**
     * Вывод данных о миньоне.
     * @param villains_id - ID злодея.
     */
    private void showDataMinions(int villains_id) {
        String villainName = dbHandler.getVillainName(villains_id);

        if (villainName.equals("")) {
            System.out.println("*=======================================*");
            System.out.println("В базе данных не существует злодея с идентификатором " + villains_id + ".");
            System.out.println("*=======================================*");
            return;
        }

        List<Minion> minionList = dbHandler.getMinionsList(villains_id);
        System.out.println("*=======================================*");
        System.out.println("Villain: " + villainName);
        if (minionList.size() == 0) {
            System.out.println("(no minions)");
        } else {
            int count = 1;
            for (Minion minion: minionList
                 ) {
                System.out.println(count++ + ". " + minion.getName() + " " + minion.getAge());
            }

        }
        System.out.println("*=======================================*");
    }

    /**
     * Вывод имен злодеев у которых миньонов дольше чем заданное значение.
     * @param countMinions - заданное количество миньонов
     */
    private void showNamesVillains(int countMinions) {
        List<Villains> villainsList = dbHandler.getVillainsList(countMinions);
        System.out.println();
        System.out.println("*=======================================*");
        for (Villains v: villainsList
             ) {
            System.out.println(v.getName() + " - " + v.getCountMinions());
        }
        System.out.println("*=======================================*");
    }

    /**
     * Получение выбора пользователя.
     * @return - возвращает целое число - выбор пункта меню.
     */
    public int getChoiceUser() {

        while (true) {
            try  {
                System.out.print("=> ");
                int a = Integer.parseInt(reader.readLine());
                return a;
            } catch (IOException | NumberFormatException e) {
                System.out.println("Введите корректное значение!");
            }
        }
    }

    /**
     * Вывод на экран основного меню пользователя.
     */
    public void showMainMenu() {
        System.out.println();
        System.out.println("*=======================================*");
        System.out.println("| * ** *** Выберите пункт меню *** ** * |");
        System.out.println("|---------------------------------------|");
        System.out.println("| 1. Вывод злодеев по числу миньонов.   |");
        System.out.println("| 2. Вывод миньонов заданного злодея.   |");
        System.out.println("| 3. Добавление миньона.                |");
        System.out.println("| 4. Удаление злодея.                   |");
        System.out.println("|---------------------------------------|");
        System.out.println("| 0. Завершение программы.              |");
        System.out.println("*=======================================*");
    }
}
