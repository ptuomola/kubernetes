package org.tuomola.project;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class BackendController {

	private static List<String> todos = new ArrayList<String>();

	@GetMapping("/todos")
	public List<String> getTodos() {
		return todos;
	}

	@PostMapping("/todos")
	public void addTodo(@RequestBody String todo) {
			todos.add(todo);
	}
}
