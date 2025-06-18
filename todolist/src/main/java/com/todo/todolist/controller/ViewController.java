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

    // Ana sayfa: Tüm görevleri göster
    @GetMapping("/")
    public String showAllTodos(Model model) {
        List<Todo> todos = todoRepository.findAll();
        model.addAttribute("todos", todos);
        return "index";
    }

    // Görev ekleme
    @PostMapping("/add")
    public String addTodo(@RequestParam String title) {
        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setCompleted(false);
        todoRepository.save(todo);
        return "redirect:/";
    }

    // Görevi silme
    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id) {
        todoRepository.deleteById(id);
        return "redirect:/";
    }

    // Görevi tamamlama
    @GetMapping("/complete/{id}")
    public String completeTodo(@PathVariable Long id) {
        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo != null) {
            todo.setCompleted(true);
            todoRepository.save(todo);
        }
        return "redirect:/";
    }

    // Checkbox ile tamamlanma durumunu değiştirme
    @GetMapping("/toggle/{id}")
    public String toggleTodo(@PathVariable Long id) {
        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo != null) {
            todo.setCompleted(!todo.isCompleted());
            todoRepository.save(todo);
        }
        return "redirect:/";
    }

    // Tamamlanan görevleri göster
    @GetMapping("/completed")
    public String showCompletedTodos(Model model) {
        List<Todo> completed = todoRepository.findByCompletedTrue();
        model.addAttribute("todos", completed);
        return "index";
    }

    // Tamamlanmamış görevleri göster
    @GetMapping("/pending")
    public String showPendingTodos(Model model) {
        List<Todo> pending = todoRepository.findByCompletedFalse();
        model.addAttribute("todos", pending);
        return "index";
    }
    // Edit sayfasını göster
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo == null) {
            return "redirect:/"; // Eğer görev yoksa ana sayfaya yönlendir
        }
        model.addAttribute("todo", todo);
        return "edit"; // Thymeleaf edit.html sayfasını gösterecek
    }

    // Form submit sonrası güncellemeyi yap
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
