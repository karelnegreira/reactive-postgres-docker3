package com.example.reactivepostgresdocker3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ReactivePostgresDocker3Application {

	public static void main(String[] args) {
		SpringApplication.run(ReactivePostgresDocker3Application.class, args);
	}

}


class Book {
	@Id
	private Long id;
	private String title;
	private String author;

	public Book() {}

	public Book(String title, String author) {
		this.title = title;
		this.author = author;
	}

	public Book(Long id, String title, String author) {
		this.id = id;
		this.title = title;
		this.author = author;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}


interface BookRepository extends ReactiveCrudRepository<Book, Long> {
}

@RestController
@RequestMapping("/api/book")
class BookController {
	@Autowired
	private BookRepository repository;

	@GetMapping("/all")
	public Flux<Book> getAll() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Mono<Book> getById(@PathVariable("id") Long id) {
		return repository.findById(id);
	}

	@PostMapping("")
	public Mono<Book> save(@RequestBody Book book) {
		return repository.save(book);
	}

	@PutMapping("/update")
	public Mono<Book> updateBook(@RequestBody Book book) {
		return repository.save(book);
	}

	@DeleteMapping("")
	public boolean deleteBook(@RequestBody Book book) {
		try {
			repository.deleteById(book.getId()).block();
		}catch(Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}
}