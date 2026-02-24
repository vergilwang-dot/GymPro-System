package com.gym.model;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class Member {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Integer balance;
    private String role;
}