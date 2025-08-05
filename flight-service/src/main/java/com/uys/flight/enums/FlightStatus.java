package com.uys.flight.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Flight Status Enum
 */
@Getter
@RequiredArgsConstructor
public enum FlightStatus {
    SCHEDULED("Scheduled", "Flight is scheduled"),
    DELAYED("Delayed", "Flight is delayed"),
    BOARDING("Boarding", "Passengers are boarding"),
    DEPARTED("Departed", "Flight has departed"),
    IN_FLIGHT("In Flight", "Flight is in the air"),
    ARRIVED("Arrived", "Flight has arrived"),
    CANCELLED("Cancelled", "Flight is cancelled"),
    DIVERTED("Diverted", "Flight is diverted");

    private final String displayName;
    private final String description;
}