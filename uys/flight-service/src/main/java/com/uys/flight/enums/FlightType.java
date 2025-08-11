package com.uys.flight.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Flight Type Enum
 */
@Getter
@RequiredArgsConstructor
public enum FlightType {
    DOMESTIC("Domestic", "Domestic flight"),
    INTERNATIONAL("International", "International flight"),
    CHARTER("Charter", "Charter flight"),
    CARGO("Cargo", "Cargo flight");

    private final String displayName;
    private final String description;
}