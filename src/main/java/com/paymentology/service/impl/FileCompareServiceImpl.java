package com.paymentology.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.paymentology.dto.CompareFileResultDTO;
import com.paymentology.dto.CompareResponseDTO;
import com.paymentology.dto.CompareResultDTO;
import com.paymentology.dto.UnMatchedFileReportDTO;
import com.paymentology.dto.UnMatchedReportDTO;
import com.paymentology.dto.UnMatchedResultDTO;
import com.paymentology.exceptions.BadFileExecption;
import com.paymentology.globals.GlobalEnums;
import com.paymentology.globals.GlobalMSGs;
import com.paymentology.service.FileCompareService;

@Service
public class FileCompareServiceImpl implements FileCompareService {

	@Override
	public CompareResponseDTO compareFiles(MultipartFile file1, MultipartFile file2) {

		List<CSVRecord> csvRecordsFile1 = readFile(file1);
		List<CSVRecord> csvRecordsFile2 = readFile(file2);

		CompareFileResultDTO fiele1Resault = CompareRecords(csvRecordsFile1, csvRecordsFile2);
		CompareFileResultDTO fiele2Resault = CompareRecords(csvRecordsFile2, csvRecordsFile1);

		CompareResultDTO compareResault = new CompareResultDTO(fiele1Resault, fiele2Resault);

		UnMatchedReportDTO unMatchedFile1 = generateUnMatchedFileReport(file1, csvRecordsFile1, csvRecordsFile2);
		UnMatchedReportDTO unMatchedFile2 = generateUnMatchedFileReport(file2, csvRecordsFile2, csvRecordsFile1);

		UnMatchedResultDTO unMatchedResault = new UnMatchedResultDTO(unMatchedFile1, unMatchedFile2);

		return new CompareResponseDTO(compareResault, unMatchedResault);

	}

	private List<CSVRecord> readFile(MultipartFile file) {
		Assert.notNull(file, String.format(GlobalMSGs.MISSING_MANDATORY_VALUE_MSG, "file"));
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
			CSVParser csvParserFile = new CSVParser(fileReader, CSVFormat.DEFAULT);
			List<CSVRecord> csvRecords = csvParserFile.getRecords();
			csvParserFile.close();
			return csvRecords;
		} catch (IOException e) {
			throw new BadFileExecption(String.format(GlobalMSGs.BAD_FORMAT_VALUE_MSG, file.getOriginalFilename()),
					GlobalEnums.Errors.BAD_FILE.toString());
		}

	}

	private CompareFileResultDTO CompareRecords(List<CSVRecord> csvRecordsFile1, List<CSVRecord> csvRecordsFile2) {

		Predicate<CSVRecord> match = csvRecord -> csvRecordsFile2.stream()
				.anyMatch(cvs -> cvs.get(5).equals(csvRecord.get(5)));

		Predicate<CSVRecord> notMatch = csvRecord -> csvRecordsFile2.stream()
				.noneMatch(cvs -> cvs.get(5).equals(csvRecord.get(5)));

		List<CSVRecord> noMatchedList = csvRecordsFile1.stream().filter(notMatch).collect(Collectors.toList());
		Integer matchCount = (int) csvRecordsFile1.stream().filter(match).count();
		Integer notMatchCount = noMatchedList.size();

		CompareFileResultDTO fileResault = new CompareFileResultDTO();
		fileResault.setTotalRecords(csvRecordsFile1.size());
		fileResault.setMatchingRecords(matchCount);
		fileResault.setUnMatchingRecords(notMatchCount);
		return fileResault;

	}

	private UnMatchedReportDTO generateUnMatchedFileReport(MultipartFile file, List<CSVRecord> csvRecordsFile1,
			List<CSVRecord> csvRecordsFile2) {

		Predicate<CSVRecord> notMatch = csvRecord -> csvRecordsFile2.stream()
				.noneMatch(cvs -> cvs.get(5).equals(csvRecord.get(5)));

		List<UnMatchedFileReportDTO> fileList = csvRecordsFile1.stream().filter(notMatch).map(cvs -> {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String dateStr = cvs.get(1);
			// convert String to LocalDate
			LocalDate date = LocalDateTime.parse(dateStr, formatter).toLocalDate();
			String referance = cvs.get(5);
			Integer amount = Integer.parseInt(cvs.get(2));
			return new UnMatchedFileReportDTO(date, referance, amount);
		}).collect(Collectors.toList());

		return new UnMatchedReportDTO(file.getOriginalFilename(), fileList);

	}

}
