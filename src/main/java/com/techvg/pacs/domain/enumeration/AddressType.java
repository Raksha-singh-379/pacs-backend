package com.techvg.pacs.domain.enumeration;

/**
 * The AddressType enumeration.
 */
public enum AddressType {
    CURRENT_ADDRESS("Current_Address"),
    PERMANENT_ADDRESS("Permanent_Address"),
    EMPLOYMENT_ADDRESS("Employment_Address");

    private final String value;

    AddressType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
