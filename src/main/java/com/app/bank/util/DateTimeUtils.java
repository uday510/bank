package com.app.bank.util;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class DateTimeUtils {

    private DateTimeUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets the current date in UTC as java.sql.Date.
     * @return UTC Date
     */
    public static Date getUTCDate() {
        LocalDate localDate = Instant.now().atZone(ZoneId.of("UTC")).toLocalDate();
        return Date.valueOf(localDate);
    }
}
