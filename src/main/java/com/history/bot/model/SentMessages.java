package com.history.bot.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SentMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_chat_id")
    @ToString.Exclude
    private User user;

    private Timestamp timestamp;

    private LocalDate timestampMessage;

    public SentMessages(User user, Timestamp timestamp, LocalDate timestampMessage) {
        this.user = user;
        this.timestamp = timestamp;
        this.timestampMessage = timestampMessage;
    }
}
