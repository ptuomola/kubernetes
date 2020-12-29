package org.tuomola.mainapplication;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;

public class ScheduledBean {
	private UUID uuid;
	private String currentStr; 
	
	public ScheduledBean() {
		uuid = UUID.randomUUID();
	}

	@Scheduled(fixedDelay = 5000)
	public void printHash() throws IOException {
		currentStr = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT )
				+ ": "+ uuid.toString();
		System.out.println(currentStr);
	}
	
	public String getCurrentString()
	{
		return currentStr;
	}

}
