package com.webrtc.webrtcsignalingserver.websocket;


import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
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
        System.err.println("********* handle text message ***********");
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
        System.out.println("--------------------- conncect start -------");
        System.out.println(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status  ) throws Exception{
        System.out.println("--------conect close ------");
        sessions.remove(session);
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("**--error--**");
        super.handleTransportError(session, exception);
    }
}
