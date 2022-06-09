package com.paymentology.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompareResultDTO {
	
	private CompareFileResultDTO file1Result;
	private CompareFileResultDTO file2Result;

}
