package com.paymentology.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.paymentology.dto.CompareResponseDTO;

@RequestMapping("/api")
public interface FileCompareAPI {
	/*
	 * Upload 2 Files as MultiPart and compare them
	 * return One Object with main two objects
	 * compare result of the 2 files and unMatched 2 files Report
	 * {
	 * 	 totalRecords,
	 * 	 
	 * }
	 */
	
	@PostMapping("/upload")
	ResponseEntity<CompareResponseDTO> uploadCompareFiles(@RequestParam("file1") MultipartFile file1,
			@RequestParam("file2") MultipartFile file2);

}
