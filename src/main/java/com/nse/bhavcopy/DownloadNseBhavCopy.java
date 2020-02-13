package com.nse.bhavcopy;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DownloadNseBhavCopy {

	private static final Logger log = LoggerFactory.getLogger(DownloadNseBhavCopy.class);

	public static void main(String args[]) throws Exception {
		LocalDate dateTime = null;
		RestTemplate restTemplate = new RestTemplate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
		if (args.length <= 0) {
			throw new Exception(
					"Kindly specify the date in program argument in the format dd/MM/yyyy e.g(11/12/2018) ");
		} else {
			try {
				dateTime = LocalDate.parse(args[0], formatter);
				String monthDisplay = dateTime.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();

				StringBuilder strFileNameBuilder = new StringBuilder();

				strFileNdameBuilders.append(dateTime.getYear());
				strFileNameBuilder.append("/");
				strFileNameBuilder.append(monthDisplay);
				strFileNameBuilder.append("/cm");
				strFileNameBuilder.append(dateTime.getDayOfMonth());
				strFileNameBuilder.append(monthDisplay);
				strFileNameBuilder.append(dateTime.getYear());
				strFileNameBuilder.append("bhav.csv.zip");

				String url = "https://www1.nseindia.com/content/historical/EQUITIES/" + strFileNameBuilder.toString();
				log.info("URL {}", url);

				byte[] forObject = restTemplate.getForObject(url, byte[].class);

				log.info(" my object {}", forObject.length);

				try (FileOutputStream stream = new FileOutputStream(
						dateTime.getDayOfMonth() + monthDisplay + dateTime.getYear() + "bhav.csv.zip")) {
					stream.write(forObject);
				}

				log.info("Zip file has been created!");

			}

			catch (HttpClientErrorException httpEx) {
				log.info(httpEx.getStatusCode().toString());
				if (httpEx.getStatusCode() == HttpStatus.NOT_FOUND) {
					log.info("Unable to download file some issue ");
				}

			}

			catch (Exception exception) {
				log.info("system ");
			}

		}

	}

	public static byte[] zipBytes(String filename, byte[] input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		ZipEntry entry = new ZipEntry(filename);
		entry.setSize(input.length);
		zos.putNextEntry(entry);
		zos.write(input);
		zos.closeEntry();
		zos.close();
		return baos.toByteArray();
	}

}