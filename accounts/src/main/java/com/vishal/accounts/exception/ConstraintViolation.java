package com.vishal.accounts.exception;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public enum ConstraintViolation {

    DUPLICATE_ENTRY("Duplicate entry", HttpStatus.CONFLICT),
    NULL_VIOLATION("cannot be null", HttpStatus.BAD_REQUEST),
    FOREIGN_KEY_VIOLATION("foreign key constraint fails", HttpStatus.BAD_REQUEST),
    UNKNOWN_ERROR("error", HttpStatus.INTERNAL_SERVER_ERROR);

    private static final Map<String, String> FIELD_MESSAGES = Map.of(
            "mobile_number_UNIQUE", "Duplicate mobile number. This number already exists.",
            "email_UNIQUE", "Duplicate email. This email is already in use."
    );

    private final String identifier;
    private final HttpStatus status;

    ConstraintViolation(String identifier, HttpStatus status) {
        this.identifier = identifier;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the ConstraintViolation enum based on message content.
     */
    public static ConstraintViolation fromMessage(String dbMessage) {
        return Arrays.stream(values())
                .filter(v -> dbMessage != null && dbMessage.toLowerCase().contains(v.identifier.toLowerCase()))
                .findFirst()
                .orElse(UNKNOWN_ERROR);
    }

    /**
     * Returns a user-friendly message, falling back to default enum message if not found in field mapping.
     */
    public static String resolveFriendlyMessage(String dbMessage) {
        if (dbMessage == null) return "Something went wrong.";

        for (Map.Entry<String, String> entry : FIELD_MESSAGES.entrySet()) {
            if (dbMessage.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        ConstraintViolation violation = fromMessage(dbMessage);
        return switch (violation) {
            case DUPLICATE_ENTRY -> "Duplicate value. This value already exists.";
            case NULL_VIOLATION -> "Some required fields are missing.";
            case FOREIGN_KEY_VIOLATION -> "Invalid reference. Related data does not exist.";
            default -> "An unexpected error occurred.";
        };
    }
}


