package org.lotterysystem.service.dto;

import lombok.Data;
import org.lotterysystem.service.enums.UserIdentityEnum;

@Data
public class UserLoginDTO {
    private String token;

    private UserIdentityEnum identity;
}
