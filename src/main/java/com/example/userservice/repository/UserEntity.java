package com.example.userservice.repository;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Data // entity 는 Setter 를 지양하지만, 수업에 맞춰 @Data 로 설정
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;

}
