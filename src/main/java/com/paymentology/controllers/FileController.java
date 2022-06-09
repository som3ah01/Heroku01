package com.paymentology.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.paymentology.api.FileCompareAPI;
import com.paymentology.dto.CompareResponseDTO;
import com.paymentology.service.impl.FileCompareServiceImpl;

@RestController
@CrossOrigin(origins = {"*"})
public class FileController implements FileCompareAPI {
	
	private @Autowired FileCompareServiceImpl fileCompareService;

	
	@Override
	public ResponseEntity<CompareResponseDTO> uploadCompareFiles(@RequestParam("file1") MultipartFile file1,
			@RequestParam("file2") MultipartFile file2) {
		
		return ResponseEntity.status(HttpStatus.CREATED).body(fileCompareService.compareFiles(file1, file2));
	}

}
