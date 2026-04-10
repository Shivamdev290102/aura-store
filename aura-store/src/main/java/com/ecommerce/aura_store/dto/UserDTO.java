package com.ecommerce.aura_store.dto;
import lombok.Data;

@Data
public class UserDTO {

    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String password;

}
