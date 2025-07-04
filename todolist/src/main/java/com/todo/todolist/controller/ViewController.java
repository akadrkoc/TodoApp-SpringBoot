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

    @GetMapping("/")
    public String showAllTodos(@RequestParam(required = false) String priority,
                               @RequestParam(required = false) String status,
                               Model model) {
        List<Todo> todos;

        if (priority != null && !priority.isEmpty() && status != null && !status.isEmpty()) {
            boolean isCompleted = status.equals("completed");
            todos = todoRepository.findByPriorityAndCompleted(priority.toLowerCase(), isCompleted);
        } else if (priority != null && !priority.isEmpty()) {
            todos = todoRepository.findByPriority(priority.toLowerCase());
        } else if (status != null && !status.isEmpty()) {
            boolean isCompleted = status.equals("completed");
            todos = todoRepository.findByCompleted(isCompleted);
        } else {
            todos = todoRepository.findAllByOrderByCompletedAscCreatedAtAsc();
        }

        model.addAttribute("todos", todos);
        model.addAttribute("currentUri", "/");
        model.addAttribute("priority", priority);
        model.addAttribute("status", status);
        return "index";
    }


    @PostMapping("/add")
    public String addTodo(@RequestParam String title,
                          @RequestParam String priority,
                          @RequestParam(required = false) String redirect) {
        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setCompleted(false);
        todo.setPriority(priority.toLowerCase());
        todoRepository.save(todo);
        return "redirect:" + (redirect != null ? redirect : "/");
    }

    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id,
                             @RequestParam(required = false) String redirect) {
        todoRepository.deleteById(id);
        return "redirect:" + (redirect != null ? redirect : "/");
    }

    @GetMapping("/complete/{id}")
    public String completeTodo(@PathVariable Long id,
                               @RequestParam(required = false) String redirect) {
        todoRepository.findById(id).ifPresent(todo -> {
            todo.setCompleted(true);
            todoRepository.save(todo);
        });
        return "redirect:" + (redirect != null ? redirect : "/");
    }

    @GetMapping("/toggle/{id}")
    public String toggleTodo(@PathVariable Long id,
                             @RequestParam(required = false) String redirect) {
        todoRepository.findById(id).ifPresent(todo -> {
            todo.setCompleted(!todo.isCompleted());
            todoRepository.save(todo);
        });
        return "redirect:" + (redirect != null ? redirect : "/");
    }

    @GetMapping("/completed")
    public String showCompletedTodos(Model model) {
        List<Todo> completed = todoRepository.findByCompletedTrue();
        model.addAttribute("todos", completed);
        model.addAttribute("currentUri", "/completed");
        return "index";
    }

    @GetMapping("/pending")
    public String showPendingTodos(Model model) {
        List<Todo> pending = todoRepository.findByCompletedFalse();
        model.addAttribute("todos", pending);
        model.addAttribute("currentUri", "/pending");
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        return todoRepository.findById(id)
                .map(todo -> {
                    model.addAttribute("todo", todo);
                    return "edit";
                })
                .orElse("redirect:/");
    }

    @PostMapping("/edit/{id}")
    public String updateTodo(@PathVariable Long id,
                             @RequestParam String title,
                             @RequestParam(required = false) String redirect) {
        todoRepository.findById(id).ifPresent(todo -> {
            todo.setTitle(title);
            todoRepository.save(todo);
        });
        return "redirect:" + (redirect != null ? redirect : "/");
    }

    @GetMapping("/priority/{level}")
    public String showPriorityTodos(@PathVariable String level, Model model) {
        List<Todo> todos = todoRepository.findByPriority(level.toLowerCase());
        model.addAttribute("todos", todos);
        model.addAttribute("currentUri", "/priority/" + level.toLowerCase());
        return "index";
    }
}
