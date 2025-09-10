package com.planning.sprint.service;

import com.planning.sprint.model.RoomUpdate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomService {

    private final Map<String, RoomUpdate> rooms = new ConcurrentHashMap<>();

    public RoomUpdate getRoom(String roomId) {
        return rooms.computeIfAbsent(roomId, id -> new RoomUpdate(id, new HashMap<>(), false, new HashSet<>()));
    }

    public void addPlayer(String roomId, String username) {
        RoomUpdate room = getRoom(roomId);
        room.getPlayers().add(username); // 👈 Guarda el jugador
    }

    public RoomUpdate addVote(String roomId, String user, String value) {
        RoomUpdate room = getRoom(roomId);
        room.getVotes().put(user, value);
        return room;
    }

    public RoomUpdate reveal(String roomId) {
        RoomUpdate room = getRoom(roomId);
        room.setRevealed(true);
        return room;
    }

    public RoomUpdate reset(String roomId) {
        RoomUpdate room = getRoom(roomId);
        room.getVotes().clear();
        room.setRevealed(false);
        return room;
    }

    public RoomUpdate removePlayer(String roomId, String username) {
        RoomUpdate room = getRoom(roomId);
        room.getPlayers().remove(username);
        room.getVotes().remove(username);
        return room;
    }
}
