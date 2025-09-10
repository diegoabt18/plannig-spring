package com.planning.sprint.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomUpdate {
    private String roomId;
    private Map<String, String> votes;
    private boolean revealed;
    private Set<String> players = new HashSet<>();
}