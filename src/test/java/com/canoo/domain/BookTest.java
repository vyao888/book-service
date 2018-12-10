package com.canoo.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BookTest {

	private static final String ISBN = Book.generateIsbn();
	private static final String TITLE = "Effective Java";
	private static final String DESCRIPTION = "The 3rd Edition";
	private static final LocalDate PUBLISHED = LocalDate.now().minusYears(1);
	private static final List<String> AUTHORS = Arrays.asList("Joshua Bloch");
	private static final BigDecimal PRICE = new BigDecimal("27.44");
	
	private Book book = null;

	@Before
	public void setUp() throws Exception {
		book = createBook();
	}

	@After
	public void tearDown() throws Exception {
		this.book = null;
	}

	@Test
	public void testConstructor() {
		assertTrue(this.book.equals(createBookViaConstructor()));
		assertTrue(this.book.hashCode()==createBookViaConstructor().hashCode());
		Map<String, Book> map = new HashMap<>();
		map.put(this.book.getIsbn(), this.book);
		Book e = map.get(this.book.getIsbn());
		assertNotNull(e);
		e = new Book();
		assertNotNull(e);
	}

	@Test
	public void testBookIsbn() {
		assertEquals(ISBN, this.book.getIsbn());
		try {
			Book.builder().isbn("").build();
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testBookTitle() {
		assertEquals(TITLE, this.book.getTitle());
		try {
			Book.builder().title(null).build();
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testBookAuthors() {
		assertEquals(AUTHORS, this.book.getAuthors());
		try {
			Book.builder().authors(null).build();
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testBookDescription() {
		assertEquals(DESCRIPTION, this.book.getDescription());
		Book.builder().isbn(ISBN).title(TITLE).authors(AUTHORS).description("").price(PRICE).publishDate(PUBLISHED).build();
	}

	@Test
	public void testBookPublishDate() {
		assertEquals(PUBLISHED, this.book.getPublishDate());
		try {
			Book.builder().publishDate(null).build();
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testBookPrice() {
		assertEquals(PRICE, this.book.getPrice());
		try {
			Book.builder().price(null).build();
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testEquals() {
		assertEquals(this.book, createBookViaConstructor());
	}

	private Book createBook() {
		return Book.builder()
				.isbn(ISBN)
				.title(TITLE)
				.authors(AUTHORS)
				.description(DESCRIPTION)
				.publishDate(PUBLISHED)
				.price(PRICE)
				.build();
	}

	private Book createBookViaConstructor() {
		return new Book(ISBN, TITLE, AUTHORS, DESCRIPTION, PUBLISHED, PRICE);
	}
	
}
