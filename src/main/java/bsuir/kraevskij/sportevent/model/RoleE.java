package bsuir.kraevskij.sportevent.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleE {
    USER("USER"),
    ADMIN("ADMIN");

    private final String value;

    RoleE(String value) {
        this.value = value;
    }

    @JsonCreator
    public static RoleE fromValue(String value) {
        for (RoleE role : values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role value: " + value);
    }
    public static boolean isValidRole(String role) {
        try {
            RoleE.fromValue(role);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
