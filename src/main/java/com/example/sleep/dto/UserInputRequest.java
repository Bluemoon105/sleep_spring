package com.example.sleep.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInputRequest {

    private Long memberNo;

    private Double sleepHours;

    private Double caffeineMg;

    private Double alcoholConsumption;

    private Double physicalActivityHours;
}
