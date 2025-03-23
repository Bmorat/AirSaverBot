package org.example;
import java.util.HashMap;
import java.util.Map;

public class UserSession {
    private static final Map<Long, UserState> userStates = new HashMap<>();

    public static UserState getUserState(Long chatId) {
        return userStates.computeIfAbsent(chatId, id -> new UserState());
    }

    public static void clearUserState(Long chatId) {
        userStates.remove(chatId);
    }
}
