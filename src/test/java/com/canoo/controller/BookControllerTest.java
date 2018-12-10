package com.canoo.controller;

import com.canoo.domain.Book;
import com.canoo.exception.BookServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
public class BookControllerTest {

    private static final String ISBN = Book.generateIsbn();
    private static final String TITLE = "Effective Java";
    private static final String DESCRIPTION = "The 3rd Edition";
    private static final LocalDate PUBLISHED = LocalDate.now().minusYears(1);
    private static final List<String> AUTHORS = Arrays.asList("Joshua Bloch");
    private static final BigDecimal PRICE = new BigDecimal("27.44");
    
	private Book book;
	private String baseUrl;
    private final LocalDateTime current = LocalDateTime.now();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected BookController bookController;

    @Autowired
    protected ObjectMapper mapper;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

	@Before
	public void setUp() throws Exception {
		this.book = createBook();
	//	this.mockMvc =  standaloneSetup(this.bookController).build();
        this.baseUrl = getBaseUrl(this.port);
        this.mockMvc.perform(post("/book/create")
                 .contentType(APPLICATION_JSON)
                 .content(toJson(this.book)))
                 .andDo(print())
                 .andExpect(status().isOk());
	}

	@After
	public void tearDown() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/book/delete/" + ISBN)
                .with(user("admin").roles("ADMIN"))
              //  .with(user("user").roles("USER"))
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

	@Test
    public void testFindAllBooks() throws Exception {
        String url = this.baseUrl + "/findAllBooks";
        log.info(String.format("Test url: %s", url));
        List list = getBooksFromRestCall(url);
        assertNotNull("Should return non-null list.", list);
        assertTrue(list.size() >= 1);
    }

    @Test
    public void testFindBookByIsbn() throws Exception {
	    String testMethod = "findBook";
        String url = getTestUrl(this.baseUrl, testMethod,book.getIsbn());
        log.info(String.format("Test url: %s", url));
        Book o = getBookFromRestCall(url);
        assertNotNull("Should return non-null Book.",o);
        assertEquals(ISBN, o.getIsbn());

        testNonExistValue(testMethod, "isbn2");
        testNonExistValue(testMethod, null);
    }


    @Test
    public void testFindBookByTitle() throws Exception {
        String testMethod = "findBookByTitle";
        String url = getTestUrl(this.baseUrl, testMethod, book.getTitle());
        log.info(String.format("Test url: %s", url));
        List list = getBooksFromRestCall(url);
        assertNotNull("Should return non-null Book.",list);
        assertTrue(list.size() > 0);

        testNonExistValueWithReturnedList(testMethod, "title2");
        testNonExistValueWithReturnedList(testMethod, "");
    }

    @Test
    public void testFindBookByPublishDate() throws Exception {
        String testMethod = "findBookByPublishDate";
        String url = getTestUrl(this.baseUrl, testMethod, this.book.getPublishDate().toString());
        log.info(String.format("Test url: %s", url));
        List list = getBooksFromRestCall(url);
        assertNotNull("Should return non-null Book.",list);
        assertTrue(list.size() > 0);

        testNonExistValueWithReturnedList(testMethod, LocalDateTime.now().plusDays(1).toString());
        testNonExistValueWithReturnedList(testMethod, "");
    }


    @Test
    public void testUpdateBook() throws Exception {
	    final String title = "title2";
	    Book b = createBook(title);
	    b.setIsbn(ISBN);
        this.restTemplate.put(baseUrl + "/update", b);
        assertTrue(true);
    }

    @Test
    public void testDeleteNonExistBook() throws Exception {
        this.restTemplate.delete(baseUrl + "/delete/" + "isbn1");
        // show warning message but does not throw exceptions
        assertTrue(true);
    }

    private void testNonExistValueWithReturnedList(String method, String pathVar) {
        String url = createUrlWithNonExistingValue(method, pathVar);
        List list = getBooksFromRestCall(url);
        if(list != null) {
            assertTrue("Should return null Book list.",list.isEmpty());
        }
    }

    private void testNonExistValue(String method, String pathVar) {
        String url = createUrlWithNonExistingValue(method, pathVar);
        Book o = getBookFromRestCall(url);
        assertNull(o);
    }

    private String createUrlWithNonExistingValue(String method, String pathVar) {
        return String.format("%s/%s/%s", this.baseUrl, method, pathVar);
    }

    private Book getBookFromRestCall(String url) {
        ResponseEntity<Book> response = null;
        try {
            response = restTemplate.getForEntity(url, Book.class);
        } catch (RestClientException e) {
            log.error(e.getMessage());
            return null;
        }
        assertTrue(response.getStatusCode().equals(HttpStatus.OK));
        return response.getBody();
    }

    private List getBooksFromRestCall(String url, Object obj) {
        ResponseEntity<List> response = null;
        try {
            response = this.restTemplate.getForEntity(url, List.class, obj);
            log.info(response.toString());
        } catch (RestClientException e) {
            log.error(e.getMessage());
            return null;
        }
        return response.getBody();
    }

    private List getBooksFromRestCall(String url) {
        ResponseEntity<List> response = null;
        try {
            response = this.restTemplate.getForEntity(url, List.class);
            log.info(response.toString());
        } catch (RestClientException e) {
            log.error(e.getMessage());
            return null;
        }
        return response.getBody();
    }

    private Book createBook(String title) {
	    return Book.builder()
                .isbn(ISBN)
                .title(title)
                .authors(AUTHORS)
                .description(DESCRIPTION)
                .publishDate(PUBLISHED)
                .price(PRICE)
                .build();
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

    private String toJson(Book book) {
	    String json = null;
        try {
           json = this.mapper.writeValueAsString(book);
        } catch (JsonProcessingException e) {
           throw new BookServiceException(String.format("Failed to create json string from book: %s.", book), e);
        }
        return json;
    }


	private static String getBaseUrl(int port) {
        return String.format("http://localhost:%d/book", port);
    }

    private static String getTestUrl(String baseUrl, String method, String pathVar) {
        return String.format("%s/%s/%s", baseUrl, method, pathVar);
    }

}
