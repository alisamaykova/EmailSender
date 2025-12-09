package com.emailsender;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    private final String host;
    private final String port;
    private final String username;
    private final String password;

    public EmailService(String host, String port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public boolean sendEmail(String toEmail, String toName, String subject, String message) {
        try {
            System.out.println("   Подготовка письма для: " + toName);

            // Настройка свойств для mail.ru
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.ssl.trust", host);

            // Создание сессии с аутентификацией
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Для отладки (можно увидеть диалог с сервером)
            session.setDebug(true);

            // Создание сообщения
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username, "Отправитель рассылки"));

            // Добавляем получателя
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail, toName));

            // Тема письма
            msg.setSubject(subject, "UTF-8");

            // Текст сообщения
            String textContent = "Добрый день, " + toName + "!\n\n"
                    + message + "\n\n--\nЭто автоматическое письмо. Пожалуйста, не отвечайте на него.";

            msg.setText(textContent, "UTF-8");

            System.out.println("Отправка на сервер...");
            // Отправка сообщения
            Transport.send(msg);

            System.out.println("Письмо успешно отправлено");
            return true;

        } catch (Exception e) {
            System.err.println("Ошибка при отправке: " + e.getMessage());
            return false;
        }
    }
}