package org.example;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class CommandHandler {
    private final Map<String, Consumer<Update>> commands = new HashMap<>();
    private final Map<String, UserState> userStates = new HashMap<>();

    public CommandHandler() {
        commands.put("/start", this::handleStart);
        commands.put("hola", this::handleStart);
        commands.put("/help", this::handleHelp);
        commands.put("/buscar", this::handleBuscar);
    }

    public void handle(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText().toLowerCase();

            // Obtener el estado actual del usuario o inicializarlo si no existe
            userStates.putIfAbsent(chatId, new UserState()); // Si no existe, lo crea
            UserState userState = userStates.get(chatId);

            System.out.println("Estado actual del usuario (" + chatId + "): " + userState.getEstado());

            // Si el usuario está en medio de una búsqueda, procesamos su respuesta
            if (userState.getEstado() > 0) {
                processUserResponse(update, userState);

                // 🔴 **IMPORTANTE:** Guardamos el estado actualizado en el mapa
                userStates.put(chatId, userState);
                return;
            }

            // Si no está en un proceso, buscar comando
            Consumer<Update> command = commands.get(text);
            if (command != null) {
                command.accept(update);
            }
        }
    }


    private void handleBuscar(Update update) {
        String chatId = update.getMessage().getChatId().toString();

        //Asegurar que el estado del usuario se almacene correctamente
        userStates.putIfAbsent(chatId, new UserState()); // Si no existe, lo crea
        UserState userState = userStates.get(chatId); // Ahora sí obtiene el existente

        userState.setEstado(1); // Iniciar el proceso de búsqueda
        userStates.put(chatId, userState); // Guardar el nuevo estado

        sendMessage(chatId, "Por favor, dime la ciudad de origen ✈️.");
        System.out.println("Estado inicializado en 1 para el usuario (" + chatId + ")");
    }

    private void processUserResponse(Update update, UserState userState) {
        String chatId = update.getMessage().getChatId().toString();
        String userInput = update.getMessage().getText();

        switch (userState.getEstado()) {
            case 1:
                // Guardar ciudad de origen y pedir ciudad de destino
                userState.setOrigen(userInput);
                userState.setEstado(2);
                sendMessage(chatId, "Genial! Ahora dime la ciudad de destino ✈️.");
                break;

            case 2:
                // Guardar ciudad de destino y pedir fecha del vuelo
                userState.setDestino(userInput);
                userState.setEstado(3);
                sendMessage(chatId, "Perfecto! ¿Para qué fecha quieres buscar vuelos? (Formato: YYYY-MM-DD) 📅.");
                break;

            case 3:
                // Guardar fecha y buscar vuelos
                userState.setFecha(userInput);
                userState.setEstado(0); // Volver a estado inicial
                sendMessage(chatId, "Buscando vuelos desde " + userState.getOrigen() + " hasta " + userState.getDestino() + " el " + userState.getFecha() + " 🛫. Un momento...");

                String resultado = AmadeusAPI.buscarVuelos(userState.getOrigen(), userState.getDestino(), userState.getFecha());
                sendMessage(chatId, resultado);
                break;

            default:
                sendMessage(chatId, "No entiendo tu mensaje. Usa /help para ver los comandos disponibles.");
                break;


        }
    }

    private void handleStart(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String response = "¡Hola! Soy AirSaverBot 🤖✈️.\n"
                + "Puedo ayudarte a encontrar vuelos baratos 🛫.\n"
                + "Usa /buscar para encontrar vuelos.\n"
                + "Usa /help para más información.";

        sendMessage(chatId, response);
    }

    private void handleHelp(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String response = "Estos son los comandos disponibles:\n"
                + "/start - Mensaje de bienvenida\n"
                + "/buscar - Buscar vuelos\n"
                + "/help - Ver los comandos disponibles";

        sendMessage(chatId, response);
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            AirSaverBot.getInstance().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
