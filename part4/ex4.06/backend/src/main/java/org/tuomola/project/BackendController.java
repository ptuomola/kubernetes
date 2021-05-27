package org.tuomola.project;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class BackendController {
	@Autowired
  JdbcTemplate jdbcTemplate;

	private NatsSender ns = new NatsSender();

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

	public TodoObject getTodo(Integer todoId) {
		ArrayList<TodoObject> todos = new ArrayList<>();

		jdbcTemplate.query("SELECT id, todotext, done FROM todos WHERE id = " + todoId,
			(rs, rowNum) -> todos.add(new TodoObject(rs.getInt("id"), rs.getString("todotext"), rs.getBoolean("done"))));

		return todos.get(0);
	}
	
	
	@PostMapping("/todos")
	public void addTodo(@RequestBody String todo) throws InvalidDataAccessApiUsageException, IOException, InterruptedException {
		if(todo.length() > 140) {
			throw new RuntimeException("Todo too long - max lenght 140");
		}
		
		String SQL = "INSERT INTO todos (todotext, done) VALUES (?, false) RETURNING id";

		KeyHolder keyHolder = new GeneratedKeyHolder(); 

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(SQL, 
														 Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, todo);
			return ps;
	}, keyHolder);
	
		ns.sendTodo(getTodo(keyHolder.getKey().intValue()));
	}



	@PutMapping("/todos/{todoId}")
	public void toggleDone(@PathVariable Integer todoId) throws IOException, InterruptedException
	{
		jdbcTemplate.update("UPDATE todos SET done = NOT done WHERE id = ?", todoId);
		ns.sendTodo(getTodo(todoId));
	}
}
