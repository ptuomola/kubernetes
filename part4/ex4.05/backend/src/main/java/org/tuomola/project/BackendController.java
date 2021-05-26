package org.tuomola.project;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class BackendController {
	@Autowired
  JdbcTemplate jdbcTemplate;

	public BackendController(JdbcTemplate jdbcTemplate)
  {
    this.jdbcTemplate = jdbcTemplate;
    jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS todos ( id SERIAL PRIMARY KEY, todotext VARCHAR, done BOOLEAN )");
  }

	@GetMapping("/healthz")
	public List<TodoObject> ok() {
		return getTodos(); 
	}

	@GetMapping("/todos")
	public List<TodoObject> getTodos() {
		ArrayList<TodoObject> todos = new ArrayList<>();

		jdbcTemplate.query("SELECT id, todotext, done FROM todos", 
			(rs, rowNum) -> todos.add(new TodoObject(rs.getInt("id"), rs.getString("todotext"), rs.getBoolean("done"))));

		return todos;
	}
	
	@PostMapping("/todos")
	public void addTodo(@RequestBody String todo) {
		if(todo.length() > 140) {
			throw new RuntimeException("Todo too long - max lenght 140");
		}
		
		jdbcTemplate.update("INSERT INTO todos (todotext, done) VALUES (?, false)", todo);
	}

	@PutMapping("/todos/{todoId}")
	public void toggleDone(@PathVariable Integer todoId)
	{
		jdbcTemplate.update("UPDATE todos SET done = NOT done WHERE id = ?", todoId);
	}
}
