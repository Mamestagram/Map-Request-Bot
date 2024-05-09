package org.example;

import org.example.Object.Bot;
import org.example.Object.Database;
import org.example.Object.Setting;

public class Main {

    public static Bot bot;
    public static Database database;
    public static Setting setting;

    // Start up
    public static void main(String[] args) {
        bot = new Bot();
        setting = new Setting();
        database = new Database();
        bot.loadJDA();
    }
}