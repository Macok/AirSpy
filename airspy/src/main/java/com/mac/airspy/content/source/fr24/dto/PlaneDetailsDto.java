package com.mac.airspy.content.source.fr24.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Maciej on 2015-03-21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaneDetailsDto {
    private String fromCity;
    private String toCity;
    private String airline;
    private String aircraft;
    private String imageLarge;
    private String imagelinkLarge;

    public String getImagelinkLarge() {
        return imagelinkLarge;
    }

    public String getFromCity() {
        return fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public String getAirline() {
        return airline;
    }

    public String getAircraft() {
        return aircraft;
    }

    public String getImageLarge() {
        return imageLarge;
    }
}
