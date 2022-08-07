package com.company;

import com.company.controller.ControllerMenu;

/**
 * Основной класс для запуска программы.
 */
public class Main {

    public static void main(String[] args) {
        ControllerMenu controllerMenu = new ControllerMenu();
        controllerMenu.workWithMenu();
    }
}