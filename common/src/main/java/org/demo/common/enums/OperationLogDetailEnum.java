package org.demo.common.enums;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public enum OperationLogDetailEnum {

    REGISTER("Register at " + getTime() + " GMT."), RESET_PASSWORD("Reset password at " + getTime() + " GMT.");

    final String detail;

    OperationLogDetailEnum(String detail) {
        this.detail = detail;
    }

    private static String getTime() {
        LocalDateTime localNow = LocalDateTime.now();

        ZonedDateTime beijingTime = localNow.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("GMT"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return  beijingTime.format(formatter);
    }
}
