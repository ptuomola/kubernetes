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
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class HelloController {

	// private static String BACKEND_URL = "http://backend-svc:2347/todos";
	// private static String FILE_PATH = "/var/tmp/app/files";

	// For local debugging
	private static String BACKEND_URL = "http://localhost:8080/todos";
	private static String FILE_PATH = "/tmp";
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) throws IOException {
		var dateString = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_DATE );
		var dayFileName = FILE_PATH + "/" + dateString + ".jpg";

		if(!new File(dayFileName).exists()) {
			fetchDayFile(dayFileName);
		}

		model.addAttribute("dayFileName", "images/" + dateString + ".jpg");
		model.addAttribute("todos", getTodos());
		model.addAttribute("formData", new FormData());
		
		return "index.html";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String formPostTodo(@ModelAttribute FormData formData, Model model) throws IOException
	{
		String newTodo = formData.getNewTodo();

		if(!newTodo.equals("")) 
		{
			System.out.println("Posting new todo: " + newTodo);
			postTodo(newTodo);
		}

		return "redirect:/";
	}

	private void fetchDayFile(String dayFileName) throws IOException
	{
		System.out.println("Fetching " + dayFileName);
		try(InputStream in = new URL("https://picsum.photos/1200").openStream()) {
			Files.copy(in, Paths.get(dayFileName));
		}
	}

	@RequestMapping(value = "/todo/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public String toggleTodo(@PathVariable Integer id)
	{
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.put(BACKEND_URL + "/" + id, null);
		return "ok";
	}



	private List getTodos()
	{
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List> response = restTemplate.getForEntity(BACKEND_URL, List.class);
		return response.getBody();
	}

	private void postTodo(String todo)
	{
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<>(todo);
		restTemplate.postForObject(BACKEND_URL, request, String.class);
	}

	@RequestMapping(value = "/healthz", method = RequestMethod.GET)
	public String healthCheck() {
		getTodos();
		return "healthCheck.html";
	}
}