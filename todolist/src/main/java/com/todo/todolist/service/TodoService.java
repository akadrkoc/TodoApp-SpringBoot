package com.todo.todolist.service;

import com.todo.todolist.model.Todo;
import com.todo.todolist.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long id, Todo updatedTodo) {
        return todoRepository.findById(id).map(todo -> {
            todo.setTitle(updatedTodo.getTitle());
            todo.setCompleted(updatedTodo.isCompleted());
            return todoRepository.save(todo);
        }).orElse(null);
    }

    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
    public List<Todo> getCompletedTodos() {
        return todoRepository.findAll()
                .stream()
                .filter(Todo::isCompleted)
                .toList();
    }

    public List<Todo> getPendingTodos() {
        return todoRepository.findAll()
                .stream()
                .filter(todo -> !todo.isCompleted())
                .toList();
    }
    public List<Todo> getSortedTodos() {
        return todoRepository.findAllByOrderByCompletedAscCreatedAtAsc();
    }

}