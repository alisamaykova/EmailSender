package com.emailsender;

import java.io.*;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private static ConfigManager instance;
    private Properties properties;

    private ConfigManager() {
        loadConfiguration();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadConfiguration() {
        try {
            properties = new Properties();

            // Пробуем загрузить из файла
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                try (InputStream input = new FileInputStream(configFile)) {
                    properties.load(input);
                }
            } else {
                // Создаём файл с настройками для mail.ru
                createDefaultConfig();
                // Загружаем заново
                try (InputStream input = new FileInputStream(configFile)) {
                    properties.load(input);
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка загрузки конфигурации: " + e.getMessage());
            createDefaultConfig();
        }
    }

    private void createDefaultConfig() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            Properties defaultProps = new Properties();

            // Настройки для mail.ru
            defaultProps.setProperty("smtp.host", "smtp.mail.ru");
            defaultProps.setProperty("smtp.port", "587");
            defaultProps.setProperty("email.username", "ваш_ящик@mail.ru");
            defaultProps.setProperty("email.password", "ваш_пароль");
            defaultProps.setProperty("email.subject", "Информационное сообщение");

            defaultProps.store(output, "Конфигурация Email рассылки");
            System.out.println("Создан файл конфигурации: " + CONFIG_FILE);
            System.out.println("ПОЖАЛУЙСТА, ОТКРОЙТЕ ФАЙЛ " + CONFIG_FILE + " И НАСТРОЙТЕ ПАРАМЕТРЫ!");

        } catch (Exception e) {
            System.err.println("Ошибка создания конфигурации: " + e.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}