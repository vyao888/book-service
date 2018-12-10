package com.canoo.controller;

import com.canoo.domain.Book;
import com.canoo.exception.BookServiceException;
import com.canoo.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.StringUtils.isBlank;


@RestController
@RequestMapping("/book")
@Slf4j
public class BookController {
	private final static long MAX_BOOKS = 6;

	@Autowired
	private BookRepository repository;

	
	@RequestMapping("/findAllBooks")
	public List<Book> findAllBooks() {
		return repository.findAll();
	}

	@RequestMapping("/findBooks/{limit}")
	public List<Book> findBooks(@PathVariable String limit) {
		long max = MAX_BOOKS;
		if(StringUtils.isNotBlank(limit)) {
			max = Long.parseLong(limit.trim());
		}
		return repository.findAll().stream().limit(max)
				.sorted(comparing(Book::getTitle).thenComparing(Book::getPublishDate))
				.collect(Collectors.toList());
	}
	
	@RequestMapping("/findBook/{isbn}")
	public Book findBook(@PathVariable String isbn) {
		validate("Book ISBN", isbn);
		return this.findAllBooks().stream()
				.filter(e -> e.getIsbn().equalsIgnoreCase(isbn.trim()))
				.findAny().orElse(null);
	}
	
	@RequestMapping(value = "/findBookByTitle/{title}", method = RequestMethod.GET)
	public List<Book> findBookByTitle(@PathVariable String title) {
		validate("Book Title", title);
		return this.findAllBooks().stream()
				.filter(e -> e.getTitle().equalsIgnoreCase(title.trim()))
				.limit(MAX_BOOKS)
                .sorted(comparing(Book::getTitle).thenComparing(Book::getPublishDate))
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "/findBookByPublishDate/{date}", method = RequestMethod.GET)
	public List<Book> findBookByPublishDate(@PathVariable  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		log.info(String.format("findBookByPublishDate: %s", date.toString()));
		if(date == null) {
			throw new IllegalArgumentException("PublishDate must be set.");
		}
		return this.findAllBooks().stream()
				.filter(e -> e.getPublishDate().equals(date))
				.limit(MAX_BOOKS)
                .sorted(comparing(Book::getPublishDate).thenComparing(Book::getTitle))
				.collect(Collectors.toList());
	}
	
	@PostMapping(value = "/create")
	public Book create(@RequestBody Book book) {
		Book b = this.findBook(book.getIsbn());
		if(b != null) {
			String isbn = getUniqueIsbn();
			book.setIsbn(isbn);
		}
		b = this.repository.save(book);
		log.info(String.format("new Book: %s has been created successfully.", book.toString()));

		return b;
	}

	private String getUniqueIsbn() {
		String isbn = Book.generateIsbn();
		Book b = null;
		while((b = findBook(isbn)) != null) {
			isbn = Book.generateIsbn();
		}
		return isbn;
	}

	@PutMapping(value = "/update")
	public Book update(@RequestBody Book book) {
		Book r = null;
		if(this.findBook(book.getIsbn()) == null) {
			log.warn(String.format("Book with the ISBN: %s does not exist.", book.getIsbn()));
		} else {
			r = this.repository.save(book);
			log.info(String.format("The Book: %s has been updated successfully.", book.toString()));
		}
		return r;
	}
	
	@DeleteMapping(value = "/delete/{isbn}")
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void delete(@PathVariable String isbn) {
		Book b = this.findBook(isbn);
		if(b == null) {
			log.warn(String.format("Book with the ISBN: %s does not exist.", isbn));
		} else {
			this.repository.delete(b);
			log.info(String.format("The Book with the ISBN %s has been deleted successfully.", isbn));
		}
	}

	private static void validate(String field, String s) {
		if(isBlank(s)) {
			throw new BookServiceException(String.format("Invalid value: %s for field: %s.", s, field));
		}
	}

}
