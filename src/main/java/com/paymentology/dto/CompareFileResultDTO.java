package com.paymentology.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompareFileResultDTO {
	
	private Integer totalRecords;
	private Integer matchingRecords;
	private Integer unMatchingRecords;

}
