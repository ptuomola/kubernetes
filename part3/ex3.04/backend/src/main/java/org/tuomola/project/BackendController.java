package org.tuomola.project;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class BackendController {
	@Autowired
  JdbcTemplate jdbcTemplate;

	public BackendController(JdbcTemplate jdbcTemplate)
  {
    this.jdbcTemplate = jdbcTemplate;
    jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS todos ( todotext VARCHAR )");
  }


	@GetMapping("/todos")
	public List<String> getTodos() {
		ArrayList<String> todos = new ArrayList<>();

		jdbcTemplate.query("SELECT todotext FROM todos", 
			(rs, rowNum) -> todos.add(rs.getString("todotext")));

		return todos;
	}

	@PostMapping("/todos")
	public void addTodo(@RequestBody String todo) {
		if(todo.length() > 140) {
			throw new RuntimeException("Todo too long - max lenght 140");
		}
		
		jdbcTemplate.update("INSERT INTO todos VALUES (?)", todo);
	}
}
