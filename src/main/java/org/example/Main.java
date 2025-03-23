package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new AirSaverBot());  // Registra el bot
            System.out.println("🤖 Bot iniciado correctamente!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
