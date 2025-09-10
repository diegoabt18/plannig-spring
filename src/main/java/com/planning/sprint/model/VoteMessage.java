package com.planning.sprint.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteMessage {
    private String roomId;
    private String user;
    private String value;
}