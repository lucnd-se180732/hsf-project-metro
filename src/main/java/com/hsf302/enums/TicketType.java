package com.hsf302.enums;

import lombok.Getter;

@Getter
public enum TicketType {
    SINGLE("Vé lượt"),
    ONE_DAY("Vé 1 ngày"),
    THREE_DAY("Vé 3 ngày"),
    MONTHLY("Vé tháng"),
    STUDENT_MONTHLY("Vé tháng sinh viên");

    private final String displayName;

    TicketType(String displayName) {
        this.displayName = displayName;
    }

}
