package com.emailsender;

import java.util.List;
import java.util.Map;

public class EmailSenderApp {
    public static void main(String[] args) {
        try {
            System.out.println("Запуск Email рассылки");

            // Загрузка конфигурации
            ConfigManager config = ConfigManager.getInstance();

            // Чтение адресов и имён
            List<Map.Entry<String, String>> recipients =
                    FileReaderUtil.readRecipients("emails.txt");

            // Чтение текста сообщения
            String messageText = FileReaderUtil.readMessage("message.txt");

            // Инициализация сервиса отправки
            EmailService emailService = new EmailService(
                    config.getProperty("smtp.host"),
                    config.getProperty("smtp.port"),
                    config.getProperty("email.username"),
                    config.getProperty("email.password")
            );

            // Отправка писем
            int sentCount = 0;
            int total = recipients.size();
            int current = 1;

            for (Map.Entry<String, String> recipient : recipients) {
                String email = recipient.getKey();
                String name = recipient.getValue();

                System.out.println("[" + current + "/" + total + "] Отправка для: " + name);

                // Заменяем {name} на реальное имя
                String personalizedMessage = messageText.replace("{name}", name);
                String subject = config.getProperty("email.subject", "Информационное сообщение");

                boolean success = emailService.sendEmail(
                        email,
                        name,
                        subject,
                        personalizedMessage
                );

                if (success) {
                    System.out.println("Успешно отправлено: " + name + " <" + email + ">");
                    sentCount++;
                } else {
                    System.out.println("Ошибка отправки: " + name + " <" + email + ">");
                }

                current++;

                // Пауза между отправками
                if (current <= total) {
                    System.out.println(" Ожидание 3 секунды...");
                    Thread.sleep(3000);
                }
            }

            System.out.println("Рассылка завершена");
            System.out.println("Успешно отправлено: " + sentCount + " из " + total);

        } catch (Exception e) {
            System.err.println("Ошибка при отправке: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
