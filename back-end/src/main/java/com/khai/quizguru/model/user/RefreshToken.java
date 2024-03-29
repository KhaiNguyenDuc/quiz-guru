package com.khai.quizguru.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Entity(name = "refresh_token")
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;


}