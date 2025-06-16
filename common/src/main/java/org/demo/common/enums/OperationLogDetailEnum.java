package org.demo.common.enums;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public enum OperationLogDetailEnum {

    REGISTER("Register at " + getTime() + " CST."), RESET_PASSWORD("Reset password at " + getTime() + " CST.");

    final String detail;

    OperationLogDetailEnum(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    private static String getTime() {
        LocalDateTime localNow = LocalDateTime.now();

        ZonedDateTime beijingTime = localNow.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("Asia/Shanghai"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return  beijingTime.format(formatter);
    }
}
