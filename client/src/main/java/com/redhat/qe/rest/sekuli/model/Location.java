package com.redhat.qe.rest.sekuli.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private Integer x;
    private Integer y;
    private Screen screen;
}
