package org.test.task.transferandentity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EntityTicket {
    private String carrier;
    private LocalDateTime departure;
    private LocalDateTime arrival;
}
