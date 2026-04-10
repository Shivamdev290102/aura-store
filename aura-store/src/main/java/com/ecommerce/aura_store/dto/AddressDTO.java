package com.ecommerce.aura_store.dto;

import com.ecommerce.aura_store.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class AddressDTO {
    private Long addressId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pinCode;
    private String type;
    @JsonIgnore
    private User user;
}
