package org.springdemobot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springdemobot.enums.Role;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    private Long chatId;

    private String firstName;

    private String lastName;

    private String userName;

    private boolean isBlocked;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Timestamp registeredAt;

    public User(Long chatId, String firstName, String lastName, String userName, boolean isBlocked, Role role , Timestamp registeredAt) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.isBlocked = isBlocked;
        this.role = role;
        this.registeredAt = registeredAt;
    }
}
