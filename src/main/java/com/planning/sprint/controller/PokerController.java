package com.planning.sprint.controller;

import com.planning.sprint.model.JoinMessage;
import com.planning.sprint.model.RoomUpdate;
import com.planning.sprint.model.VoteMessage;
import com.planning.sprint.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PokerController {

    private final RoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;

    // --- Mensajes WebSocket ---

    @MessageMapping("/join")
    public void handleJoin(@Payload JoinMessage message, SimpMessageHeaderAccessor headerAccessor) {
        // guardar datos en la sesión de WebSocket
        headerAccessor.getSessionAttributes().put("username", message.getUsername());
        headerAccessor.getSessionAttributes().put("roomId", message.getRoomId());

        roomService.addPlayer(message.getRoomId(), message.getUsername());
        RoomUpdate room = roomService.getRoom(message.getRoomId());

        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), room);
    }

    @MessageMapping("/vote")
    public void handleVote(@Payload VoteMessage message) {
        RoomUpdate room = roomService.addVote(message.getRoomId(), message.getUser(), message.getValue());
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), room);
    }

    @MessageMapping("/reveal")
    public void handleReveal(@Payload VoteMessage message) {
        RoomUpdate room = roomService.reveal(message.getRoomId());
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), room);
    }

    @MessageMapping("/reset")
    public void handleReset(@Payload VoteMessage message) {
        RoomUpdate room = roomService.reset(message.getRoomId());
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), room);
    }

    // --- Rutas HTTP para las vistas ---

    @GetMapping("/room/{id}")
    public String joinPage() {
        return "forward:/join.html";
    }

    @GetMapping("/room/{id}/play")
    public String roomPage() {
        return "forward:/room.html";
    }
}