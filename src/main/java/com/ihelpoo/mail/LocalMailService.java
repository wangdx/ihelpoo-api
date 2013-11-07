package com.ihelpoo.mail;

import javax.mail.MessagingException;
import java.io.IOException;

public final class LocalMailService implements MailService {

    public void send(final Message message) throws IOException {

        new Thread(new Runnable() {

            public void run() {
                try {
                    new MailSender().send(message);
                } catch (final MessagingException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
