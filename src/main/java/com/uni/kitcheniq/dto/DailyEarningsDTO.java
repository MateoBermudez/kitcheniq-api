package com.uni.kitcheniq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyEarningsDTO {
    private LocalDate date;
    private Double totalEarnings;
}
