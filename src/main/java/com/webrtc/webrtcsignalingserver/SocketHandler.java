package com.webrtc.webrtcsignalingserver;


import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException{

        System.err.println( session.toString() + message.toString());
        for (WebSocketSession webSocketSession : sessions) {
            System.out.println(webSocketSession);
            if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())){
                webSocketSession.sendMessage(message);
            }

        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        sessions.add(session);
        System.out.println("--------------------- conncect");
        System.out.println(session);
    }


}
