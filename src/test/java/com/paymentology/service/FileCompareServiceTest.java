package com.paymentology.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paymentology.dto.CompareResponseDTO;
import com.paymentology.service.impl.FileCompareServiceImpl;

@ExtendWith(SpringExtension.class)
public class FileCompareServiceTest {

	private static final String CSV_RECORD = "[comment='null', recordNumber=1, values=[Card Campaign, 2014-01-11 22:27:44, -20000, *MOLEPS ATM25  MOLEPOLOLE BW, DEDUCT, 0584011808649511, 1, P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzA5, ]]";

	private @InjectMocks FileCompareServiceImpl fileService;

	@Test
	void Should_ReturnCourse_When_ProvidesCourseId() {
		// Given
		MockMultipartFile file1 = new MockMultipartFile("file1", "file1.csv", MediaType.MULTIPART_FORM_DATA_VALUE,
				CSV_RECORD.getBytes());
		MockMultipartFile file2 = new MockMultipartFile("file2", "file1.csv", MediaType.MULTIPART_FORM_DATA_VALUE,
				CSV_RECORD.getBytes());

		// When
		CompareResponseDTO result = fileService.compareFiles(file1, file2);

		// Then
		assertThat(result.getCompareResult()).isNotNull();
		assertThat(result.getUnMatchedResult()).isNotNull();
		assertThat(result.getCompareResult().getFile1Result().getTotalRecords()).isEqualTo(1);
		assertThat(result.getCompareResult().getFile2Result().getTotalRecords()).isEqualTo(1);

		assertThat(result.getUnMatchedResult().getUnMatchedFile1().getUnMatchedList().size()).isEqualTo(0);
		assertThat(result.getUnMatchedResult().getUnMatchedFile2().getUnMatchedList().size()).isEqualTo(0);
	}

}
