package com.paymentology.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.paymentology.dto.CompareResponseDTO;

@Service
public interface FileCompareService {
	
	/*
	 * 
	 */
	CompareResponseDTO compareFiles(MultipartFile file1, MultipartFile file2) ;

}
