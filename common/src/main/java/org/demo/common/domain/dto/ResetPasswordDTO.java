package org.demo.common.domain.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {

    Long userId;
    String rowPsw;
    String newPsw;

}
