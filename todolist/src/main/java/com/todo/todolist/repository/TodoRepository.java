package com.todo.todolist.repository;

import com.todo.todolist.model.PriorityLevel;
import com.todo.todolist.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByCompletedTrue();
    List<Todo> findByCompletedFalse();
    List<Todo> findAllByOrderByCompletedAscCreatedAtAsc();


    List<Todo> findByPriority(String priority);
}