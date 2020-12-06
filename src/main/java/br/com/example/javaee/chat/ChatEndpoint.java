package br.com.example.javaee.chat;

import br.com.example.javaee.chat.encoders.ChatMessageDecoder;
import br.com.example.javaee.chat.encoders.ChatMessageEncoder;
import br.com.example.javaee.chat.model.ChatMessage;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat/{room}", encoders = ChatMessageEncoder.class, decoders = ChatMessageDecoder.class)
public class ChatEndpoint {
    private final Logger log = Logger.getLogger(getClass().getName());

    @Inject
    private ChatStateService chatService;

    @OnOpen
    public void open(final Session session, @PathParam("room") final String room) {
        session.getUserProperties().put("room", room);

        for (ChatMessage chatMessage : chatService.getStateForChat(room).getMessages()) {
            sendMessage(session, chatMessage);
        }
    }

    @OnClose
    public void close(final Session session, @PathParam("room") final String room) throws IOException {
        ChatMessage chatMessage = new ChatMessage(session.getId() + " покинул комнату", "[System]", new Date());
        chatService.getStateForChat(room).addMessage(chatMessage);
        for (Session s : session.getOpenSessions()) {
            if (s.isOpen()
                    && room.equals(s.getUserProperties().get("room"))) {
                sendMessage(s, chatMessage);
            }
        }
    }

    @OnMessage
    public void onMessage(final Session session, final ChatMessage chatMessage) throws IOException {
        String room = (String) session.getUserProperties().get("room");
        chatService.getStateForChat(room).addMessage(chatMessage);
        for (Session s : session.getOpenSessions()) {
            if (s.isOpen()
                    && room.equals(s.getUserProperties().get("room"))) {
                sendMessage(s, chatMessage);
            }
        }
    }

    private void sendMessage(Session s, ChatMessage chatMessage) {
        try {
            s.getBasicRemote().sendObject(chatMessage);
        } catch (IOException | EncodeException e) {
            log.log(Level.WARNING, "onMessage failed", e);
        }
    }
}
