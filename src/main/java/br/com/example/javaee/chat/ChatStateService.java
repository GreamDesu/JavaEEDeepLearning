package br.com.example.javaee.chat;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ChatStateService {
    private Map<String, ChatState> chatStateMap = new HashMap<>();

    public ChatState getStateForChat(String chatId) {
        ChatState state = chatStateMap.get(chatId);
        if (state == null) {
            state = new ChatState(chatId);
            chatStateMap.put(chatId, state);
        }
        return state;
    }

}
