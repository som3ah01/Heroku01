package com.paymentology.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.paymentology.dto.CompareResponseDTO;
import com.paymentology.dto.CompareResultDTO;
import com.paymentology.dto.UnMatchedResultDTO;
import com.paymentology.exceptions.BadFileExecption;
import com.paymentology.service.impl.FileCompareServiceImpl;

@WebMvcTest(controllers = { FileCompareAPI.class })
public class FileCompareAPITestIT {

	private static final String URL = "/api/upload";
	private static final String CSV_RECORD = "[comment='null', recordNumber=1, values=[Card Campaign, 2014-01-11 22:27:44, -20000, *MOLEPS ATM25  MOLEPOLOLE BW, DEDUCT, 0584011808649511, 1, P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzA5, ]]";

	@Autowired
	private MockMvc mockMvc;

	private @MockBean FileCompareServiceImpl fileService;

	@Test
	void testSucssesPostUploadAndCompareFilesIfProvidedFiles() throws Exception {
		// Given
		MockMultipartFile file1 = new MockMultipartFile("file1", "file1.csv", MediaType.MULTIPART_FORM_DATA_VALUE,
				CSV_RECORD.getBytes());
		MockMultipartFile file2 = new MockMultipartFile("file2", "file1.csv", MediaType.MULTIPART_FORM_DATA_VALUE,
				CSV_RECORD.getBytes());

		// AND
		CompareResultDTO compareResult = new CompareResultDTO();
		UnMatchedResultDTO unMatchedResult = new UnMatchedResultDTO();
		CompareResponseDTO response = new CompareResponseDTO(compareResult, unMatchedResult);

		// When
		Mockito.when(fileService.compareFiles(Mockito.any(), Mockito.any())).thenReturn(response);

		// Then
		mockMvc.perform(multipart(URL).file(file1).file(file2)).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.compareResult").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.unMatchedResult").exists());

	}

	@Test
	void testFailPostUploadAndCompareFilesIfrovidedBadFiles() throws Exception {
		// Given
		MockMultipartFile file1 = new MockMultipartFile("file1", "file1.csv", MediaType.MULTIPART_FORM_DATA_VALUE,
				CSV_RECORD.getBytes());
		MockMultipartFile file2 = new MockMultipartFile("file2", "file1.csv", MediaType.MULTIPART_FORM_DATA_VALUE,
				CSV_RECORD.getBytes());

		// When
		Mockito.when(fileService.compareFiles(Mockito.any(), Mockito.any()))
				.thenThrow(new BadFileExecption("Bad File", "Bad_File"));

		// Then
		mockMvc.perform(multipart(URL).file(file1).file(file2)).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof BadFileExecption));

	}
}
