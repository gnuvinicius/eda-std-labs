package br.dev.garage474.msorder.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    // Ex: 2024-01-15T10:30:00

    @Override
    public LocalDateTime unmarshal(String value) throws Exception {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(value, FORMATTER);
    }

    @Override
    public String marshal(LocalDateTime value) throws Exception {
        if (value == null) {
            return null;
        }
        return value.format(FORMATTER);
    }
}
