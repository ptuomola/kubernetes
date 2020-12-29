package org.tuomola.mainapplication;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;

public class ScheduledBean {
	private UUID uuid;
	private String currentStr; 
	
	private static String FILE_PATH = "/tmp/timestamp";
	
	public ScheduledBean() {
		uuid = UUID.randomUUID();
	}

	@Scheduled(fixedDelay = 5000)
	public void printHash() throws IOException {
		currentStr = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT )
				+ ": "+ uuid.toString();
		
		FileWriter outFile = new FileWriter(FILE_PATH);
		outFile.write(ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT ));
		outFile.close();
	}
	
	public String getCurrentString()
	{
		return currentStr;
	}

}
