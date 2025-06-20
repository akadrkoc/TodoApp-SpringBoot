package com.todo.todolist.controller;

import com.todo.todolist.model.Todo;
import com.todo.todolist.repository.TodoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ViewController {

    private final TodoRepository todoRepository;

    public ViewController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // Homepage: show and sort whole list (findAllByOrderByCompletedAscCreatedAtAsc)
    @GetMapping("/")
    public String showAllTodos(Model model) {
        List<Todo> todos = todoRepository.findAllByOrderByCompletedAscCreatedAtAsc();
        model.addAttribute("todos", todos);
        return "index";
    }

    // Adding task
    @PostMapping("/add")
    public String addTodo(@RequestParam String title) {
        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setCompleted(false);
        todoRepository.save(todo);
        return "redirect:/";
    }

    // Deleting task
    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id) {
        todoRepository.deleteById(id);
        return "redirect:/";
    }

    // Completing task
    @GetMapping("/complete/{id}")
    public String completeTodo(@PathVariable Long id) {
        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo != null) {
            todo.setCompleted(true);
            todoRepository.save(todo);
        }
        return "redirect:/";
    }

    // Changing the task situation with checkbox
    @GetMapping("/toggle/{id}")
    public String toggleTodo(@PathVariable Long id) {
        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo != null) {
            todo.setCompleted(!todo.isCompleted());
            todoRepository.save(todo);
        }
        return "redirect:/";
    }

    // Show the completed tasks
    @GetMapping("/completed")
    public String showCompletedTodos(Model model) {
        List<Todo> completed = todoRepository.findByCompletedTrue();
        model.addAttribute("todos", completed);
        return "index";
    }

    // Show the uncompleted tasks
    @GetMapping("/pending")
    public String showPendingTodos(Model model) {
        List<Todo> pending = todoRepository.findByCompletedFalse();
        model.addAttribute("todos", pending);
        return "index";
    }
    // Show edit page
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo == null) {
            return "redirect:/";
        }
        model.addAttribute("todo", todo);
        return "edit";
    }

    // Update after form submit
    @PostMapping("/edit/{id}")
    public String updateTodo(@PathVariable Long id, @RequestParam String title) {
        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo != null) {
            todo.setTitle(title);
            todoRepository.save(todo);
        }
        return "redirect:/";
    }
}