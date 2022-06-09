package com.paymentology.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnMatchedResultDTO {
	
	private UnMatchedReportDTO unMatchedFile1;
	private UnMatchedReportDTO unMatchedFile2;

}
