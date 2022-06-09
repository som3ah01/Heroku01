package com.paymentology.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnMatchedFileReportDTO {
	
	private LocalDate date;
	private String reference;
	private Integer amount;

}
