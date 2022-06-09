package com.paymentology.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnMatchedReportDTO {
	
	private String fileName;
	private List<UnMatchedFileReportDTO> unMatchedList;

}
