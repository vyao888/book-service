package com.canoo.controller;

import com.canoo.domain.Book;
import com.canoo.exception.BookServiceException;
import com.canoo.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.StringUtils.isBlank;


@RestController
@RequestMapping("/books")
@Slf4j
public class BookController {
	private final static long MAX_BOOKS = 6;

	@Autowired
	private BookRepository repository;

	
	@GetMapping
	public List<Book> findAll() {
		return repository.findAll();
	}

	/*
	@GetMapping
	public List<Book> findBooks(@RequestParam String limit) {
		long max = MAX_BOOKS;
		if(StringUtils.isNotBlank(limit)) {
			max = Long.parseLong(limit.trim());
		}
		return repository.findAll().stream().limit(max)
				.sorted(comparing(Book::getTitle).thenComparing(Book::getPublishDate))
				.collect(Collectors.toList());
	}*/
	
	@GetMapping("/{isbn}")
	public Optional<Book> find(@PathVariable String isbn) {
		validate("Book ISBN", isbn);
		return this.repository.findById(isbn);
	}
	
	@GetMapping(value = "/findByTitle")
	public List<Book> findByTitle(@RequestParam  String title) {
		validate("Book Title", title);
		return this.findAll().stream()
				.filter(e -> e.getTitle().equalsIgnoreCase(title.trim()))
				.limit(MAX_BOOKS)
                .sorted(comparing(Book::getTitle).thenComparing(Book::getPublishDate))
				.collect(Collectors.toList());
	}

	@GetMapping(value = "/findByPublishDate")
	public List<Book> findByPublishDate(@RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		if(date == null) {
			throw new IllegalArgumentException("PublishDate must be set.");
		}
		log.info(String.format("findByPublishDate: %s", date.toString()));
		return this.findAll().stream()
				.filter(e -> e.getPublishDate().equals(date))
				.limit(MAX_BOOKS)
                .sorted(comparing(Book::getPublishDate).thenComparing(Book::getTitle))
				.collect(Collectors.toList());
	}
	
	@PostMapping
	public Book create(@RequestBody Book book) {
		Optional<Book> o = this.repository.findById(book.getIsbn());
		if(o.isPresent()) {
			o.get().setIsbn(getUniqueIsbn());
		}
		Book b = this.repository.save(book);
		log.info(String.format("new Book: %s has been created successfully.", b.toString()));

		return b;
	}

	private String getUniqueIsbn() {
		String isbn = Book.generateIsbn();
		while(this.repository.findById(isbn).isPresent()) {
			isbn = Book.generateIsbn();
		}
		return isbn;
	}

	@PutMapping("/{isbn}")
	public Book update(@PathVariable String isbn, @RequestBody Book book) {
	    Book b = null;
		if(!this.repository.findById(isbn).isPresent()) {
			log.warn(String.format("Book with the ISBN: %s does not exist.", isbn));
		} else {
			b = this.repository.save(book);
			log.info(String.format("The Book: %s has been updated successfully.", b.toString()));
		}
		return b;
	}
	
	@DeleteMapping(value = "/{isbn}")
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void delete(@PathVariable String isbn) {
		Optional<Book> o = this.repository.findById(isbn);
		if(!o.isPresent()) {
			log.warn(String.format("Book with the ISBN: %s does not exist.", isbn));
		} else {
			this.repository.delete(o.get());
			log.info(String.format("The Book with the ISBN %s has been deleted successfully.", isbn));
		}
	}

	private static void validate(String field, String s) {
		if(isBlank(s)) {
			throw new BookServiceException(String.format("Invalid value: %s for field: %s.", s, field));
		}
	}

}
