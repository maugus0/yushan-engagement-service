package com.yushan.engagement_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

/**
 * Gender enumeration - matches user service
 */
public enum Gender {
    UNKNOWN,
    MALE,
    FEMALE;

    @JsonCreator
    public static Gender fromString(String value) {
        if (value == null) {
            return UNKNOWN;
        }
        try {
            return Gender.valueOf(value.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    @JsonValue
    @Override
    public String toString() {
        return name();
    }
}
