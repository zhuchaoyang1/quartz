package com.kedacom.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtil {

    public static Date localDate2Date(LocalDateTime localDateTime) {
        if (null == localDateTime) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);//Combines this date-time with a time-zone to create a  ZonedDateTime.
        return Date.from(zdt.toInstant());
    }

    public static Date natureDateStr2Date(String nutureDateStr) {
        DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime localDateTime = LocalDateTime.parse(nutureDateStr, timeDtf);
        return localDate2Date(localDateTime);
    }


}
