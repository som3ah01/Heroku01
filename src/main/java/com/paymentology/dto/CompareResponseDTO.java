package com.paymentology.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompareResponseDTO {
	
	private CompareResultDTO compareResult;
	private UnMatchedResultDTO unMatchedResult ;

}
