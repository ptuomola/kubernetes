package org.tuomola.project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {


	private static String FILE_PATH = "/var/tmp/app/files";

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) throws IOException {
		var dateString = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_DATE );
		var dayFileName = FILE_PATH + "/" + dateString + ".jpg";

		if(!new File(dayFileName).exists()) {
			fetchDayFile(dayFileName);
		}

		model.addAttribute("dayFileName", "images/" + dateString + ".jpg");

		return "index.html";
	}

	private void fetchDayFile(String dayFileName) throws IOException
	{
		System.out.println("Fetching " + dayFileName);
		try(InputStream in = new URL("https://picsum.photos/1200").openStream()) {
			Files.copy(in, Paths.get(dayFileName));
		}
	}
}