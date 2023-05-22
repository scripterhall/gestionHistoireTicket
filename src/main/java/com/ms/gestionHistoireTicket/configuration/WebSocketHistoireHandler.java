package com.ms.gestionHistoireTicket.configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.ms.gestionHistoireTicket.entities.HistoireTicket;

@Component
public class WebSocketHistoireHandler extends TextWebSocketHandler {


    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    int messagecount = 0;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
                Map<String, HistoireTicket> value = new Gson().fromJson(message.getPayload(), Map.class);
                if(value.keySet().contains("subscribe") || value.keySet().contains("supprimer") || value.keySet().contains("modifier")   ) {
                    // start the service with the subscribe name
                    for (WebSocketSession s : sessions) {
                        if (s.isOpen()) {
                            s.sendMessage(new TextMessage(message.getPayload()));
                        }
                    }
                } else if(value.keySet().contains("unsubscribe")) {
                    // stop the service with the unsubscribe name or remove the session that unsubscribed
                    // be careful not to stop the service if there are still sessions available
                } else {
                    // do something with the sent object
                    
                    
                   
        
                    // emit message with type='message'
                    
                }
            }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // the messages will be broadcasted to all users.
        System.out.println("connected");
        session.setTextMessageSizeLimit(1024*1024*40);
        session.setBinaryMessageSizeLimit(1024*1024*35);
        sessions.add(session);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("disconnected");
        // do something on connection closed
    } 

   
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        // hanedle transport error
    }
   
}
