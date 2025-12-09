package com.emailsender;

import java.io.*;
import java.util.*;

public class FileReaderUtil {

    public static List<Map.Entry<String, String>> readRecipients(String filename) {
        List<Map.Entry<String, String>> recipients = new ArrayList<>();

        File file = new File(filename);
        if (!file.exists()) {
            createSampleEmailsFile(filename);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                // Пропускаем пустые строки и комментарии
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Разделяем email и имя
                String[] parts = line.split(",|;");
                if (parts.length >= 2) {
                    String email = parts[0].trim();
                    String name = parts[1].trim();

                    // Простая проверка email
                    if (email.contains("@") && email.contains(".")) {
                        recipients.add(new AbstractMap.SimpleEntry<>(email, name));
                        System.out.println("Добавлен получатель: " + name + " <" + email + ">");
                    } else {
                        System.err.println("Строка " + lineNumber + ": некорректный email - " + email);
                    }
                } else {
                    System.err.println("Строка " + lineNumber + ": неправильный формат - " + line);
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла " + filename + ": " + e.getMessage());
        }

        if (recipients.isEmpty()) {
            System.err.println("В файле " + filename + " не найдено корректных получателей!");
            System.err.println("Проверьте файл и перезапустите программу.");
        }

        return recipients;
    }

    public static String readMessage(String filename) {
        StringBuilder message = new StringBuilder();

        File file = new File(filename);
        if (!file.exists()) {
            createSampleMessageFile(filename);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                message.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла " + filename + ": " + e.getMessage());
            return "Добрый день, {name}!\n\nЭто тестовое сообщение.";
        }

        return message.toString().trim();
    }

    private static void createSampleEmailsFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("# Файл с email-адресами получателей");
            writer.println("# Формат: email, Имя Фамилия");
            writer.println("# Можно использовать запятую или точку с запятой");
            writer.println("");
            writer.println("# Примеры (замените на реальные данные):");
            writer.println("test1@mail.ru, Иван Иванов");
            writer.println("test2@gmail.com; Мария Петрова");
            writer.println("test3@yandex.ru, Алексей Сидоров");

            System.out.println("Создан пример файла с адресами: " + filename);
            System.out.println("Пожалуйста, откройте файл " + filename + " и добавьте реальные email!");

        } catch (IOException e) {
            System.err.println("Ошибка создания файла: " + e.getMessage());
        }
    }

    private static void createSampleMessageFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Добрый день, {name}!");
            writer.println("");
            writer.println("Это информационное сообщение для вас.");
            writer.println("");
            writer.println("Мы рады приветствовать вас в нашей системе.");
            writer.println("Здесь может быть любая важная информация.");
            writer.println("");
            writer.println("С уважением,");
            writer.println("Команда поддержки");

            System.out.println("Создан пример файла с сообщением: " + filename);

        } catch (IOException e) {
            System.err.println("Ошибка создания файла: " + e.getMessage());
        }
    }
}