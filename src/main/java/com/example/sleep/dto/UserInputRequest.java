package com.example.sleep.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInputRequest {

    private Long memberNo;                 // ✅ 회원 고유번호 (member_tb.member_no 참조)

    private Double sleepHours;             // 수면 시간

    private Double caffeineMg;             // 카페인 섭취량

    private Double alcoholConsumption;     // 알코올 섭취량

    private Double physicalActivityHours;  // 신체활동 시간
}
