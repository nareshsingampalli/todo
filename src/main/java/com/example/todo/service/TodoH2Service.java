package com.example.todo.service;

import com.example.todo.model.*;
import com.example.todo.repository.TodoRepository;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TodoH2Service implements TodoRepository {

	@Autowired
	private JdbcTemplate db;

	@Override
	public ArrayList<Todo> getTodos() {
		List<Todo> todoList = db.query("select * from todolist", new TodoRowMapper());
		return new ArrayList<>(todoList);
	}

	@Override
	public Todo getTodoById(int id) {
		try {
			return db.queryForObject("select * from todolist where id = ?", new TodoRowMapper(),
					id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public Todo addTodo(Todo todo) {
		db.update("insert into todolist(todo,status,priority) values(?,?,?)", todo.getTodo(),
				todo.getStatus(), todo.getPriority());
		Todo savedTodo = db.queryForObject(
				"select * from todolist where todo = ? and status = ? and priority  = ?",
				new TodoRowMapper(), todo.getTodo(), todo.getStatus(), todo.getPriority());
		return savedTodo;
	}

	@Override
	public Todo updateTodo(Todo todo, int id) {
		if (todo.getTodo() != null) {
			db.update("update todolist set todo = ? where id = ? ", todo.getTodo(),id);
		}
		if (todo.getStatus() != null)
			db.update("update todolist set status = ? where id = ? ", todo.getStatus(), id);
		if (todo.getPriority() != null)
			db.update("update todolist set priority = ? where id = ? ", todo.getPriority(),id);
		return getTodoById(id);
	}

	@Override
	public void deleteTodo(int id) {
		db.update("delete from todolist where id = ? ", id);

	}

}
