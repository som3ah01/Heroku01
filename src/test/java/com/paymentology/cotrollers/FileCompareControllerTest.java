package com.paymentology.cotrollers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paymentology.controllers.FileController;
import com.paymentology.dto.CompareResponseDTO;
import com.paymentology.dto.CompareResultDTO;
import com.paymentology.dto.UnMatchedResultDTO;
import com.paymentology.exceptions.BadFileExecption;
import com.paymentology.service.impl.FileCompareServiceImpl;

@ExtendWith(SpringExtension.class)
public class FileCompareControllerTest {

	private static final String CSV_RECORD = "[comment='null', recordNumber=1, values=[Card Campaign, 2014-01-11 22:27:44, -20000, *MOLEPS ATM25  MOLEPOLOLE BW, DEDUCT, 0584011808649511, 1, P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzA5, ]]";

	private @InjectMocks FileController fileController;
	private @Mock FileCompareServiceImpl fileService;

	@Test
	void Should_ReturnCourseDTO_When_ProvidesFiles() {
		// Given
		CompareResultDTO compareResult = new CompareResultDTO();
		UnMatchedResultDTO unMatchedResult = new UnMatchedResultDTO();
		CompareResponseDTO response = new CompareResponseDTO(compareResult, unMatchedResult);

		// AND
		MockMultipartFile file1 = new MockMultipartFile("file1", "file1.csv", MediaType.MULTIPART_FORM_DATA_VALUE,
				CSV_RECORD.getBytes());
		MockMultipartFile file2 = new MockMultipartFile("file2", "file1.csv", MediaType.MULTIPART_FORM_DATA_VALUE,
				CSV_RECORD.getBytes());
		// When
		Mockito.when(fileService.compareFiles(Mockito.any(), Mockito.any())).thenReturn(response);
		// Then
		ResponseEntity<CompareResponseDTO> resualt = fileController.uploadCompareFiles(file1, file2);
		// AND
		assertThat(resualt.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertNotNull(resualt.getBody().getCompareResult());
		assertNotNull(resualt.getBody().getUnMatchedResult());
	}

	@Test
	void Should_Fail_When_ProvidesBadFiles() throws BadFileExecption {
		// Given
		MockMultipartFile file1 = new MockMultipartFile("file1", "file1.csv", MediaType.MULTIPART_FORM_DATA_VALUE,
				CSV_RECORD.getBytes());
		MockMultipartFile file2 = new MockMultipartFile("file2", "file1.csv", MediaType.MULTIPART_FORM_DATA_VALUE,
				CSV_RECORD.getBytes());
		// When
		Mockito.when(fileService.compareFiles(Mockito.any(), Mockito.any()))
				.thenThrow(new BadFileExecption("Bad file", "BAD_FILE"));
		// Then
		assertThrows(BadFileExecption.class, () -> {
			fileController.uploadCompareFiles(file1, file2);
		});
	}

}
