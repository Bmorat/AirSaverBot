package AirBot;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AirSaverBot extends TelegramLongPollingBot {
    private static AirSaverBot instance;
    private final CommandHandler commandHandler = new CommandHandler();

    public AirSaverBot() {
        instance = this;
    }
    @Override
    public String getBotUsername() {
        return "AirSaverBot";  // Reemplaza con el username de tu bot
    }

    public static AirSaverBot getInstance() {
        return instance;
    }
    @Override
    public String getBotToken() {
        return "";  // Reemplaza con el token de BotFather
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String userMessage = update.getMessage().getText();

            commandHandler.handle(update);

        }
    }
}