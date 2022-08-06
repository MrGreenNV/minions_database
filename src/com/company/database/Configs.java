package com.company.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Необходимые данные для подключения к БД.
 */

public class Configs {
    protected String dbHost = "localhost";
    protected String dbPort = "3306";
    protected String dbName = "minions_bd";
    protected String dbTimeZone = "serverTimezone=UTC";

    protected Properties getProps() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Properties props = new Properties();

        try {

            System.out.print("Введите логин: ");
            props.setProperty("user", reader.readLine());

            System.out.print("Введите пароль: ");
            props.setProperty("password", reader.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return props;
    }
}
