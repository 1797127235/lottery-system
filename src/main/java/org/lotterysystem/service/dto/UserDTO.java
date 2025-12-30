package org.lotterysystem.service.dto;

import lombok.Data;
import org.lotterysystem.service.enums.UserIdentityEnum;

@Data
public class UserDTO {
    private Long id;

    private String userName;

    private String email;

    private String phoneNumber;

    private UserIdentityEnum identity;
}
