package com.planning.sprint.config;

import com.planning.sprint.model.RoomUpdate;
import com.planning.sprint.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final RoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = (String) headers.getSessionAttributes().get("username");
        String roomId = (String) headers.getSessionAttributes().get("roomId");

        if (username != null && roomId != null) {
            // Quitar al jugador de la sala
            RoomUpdate room = roomService.removePlayer(roomId, username);

            // Notificar a todos en esa sala que se actualizó
            messagingTemplate.convertAndSend("/topic/room/" + roomId, room);
        }
    }
}