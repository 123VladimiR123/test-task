package org.test.task.transferandentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DTOTicker {
    private final String origin;
    private final String origin_name;
    private final String destination;
    private final String destination_name;
    private final String departure_date;
    private final String departure_time;
    private final String arrival_date;
    private final String arrival_time;
    private final String carrier;
    private final Integer stops;
    private final Integer price;
}
