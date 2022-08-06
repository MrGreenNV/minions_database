package com.company;

import com.company.controller.ControllerMenu;
import com.company.database.DataBaseHandler;

public class Main {

    public static void main(String[] args) {
        ControllerMenu controllerMenu = new ControllerMenu();
        controllerMenu.workWithMenu();
    }
}
