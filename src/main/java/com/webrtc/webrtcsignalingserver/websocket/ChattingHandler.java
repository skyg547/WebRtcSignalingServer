package com.webrtc.webrtcsignalingserver.websocket;


import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;


@Component
public class ChattingHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions = new ArrayList<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

//        log("#ChattingHandler , after ConnecttionEstablished");
        System.err.println("connect");
        sessions.add(session);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.err.println("message");

        for(WebSocketSession s : sessions){
            s.sendMessage(new TextMessage(session.getPrincipal().getName() +":" + message.getPayload()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.err.println("close");
        sessions.remove(session);

    }
}
