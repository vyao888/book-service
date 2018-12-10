package com.canoo.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Builder
@Data
@AllArgsConstructor
@Document(collection = "books")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)

public class Book {

	public Book() {
		this.isbn = generateIsbn();
	}

	@Id
	private String isbn;
	@NonNull
	private String title;
	@NonNull
	private List<String> authors;
	private String description;
	@NonNull
	private LocalDate publishDate;
	@NonNull
	private BigDecimal price;

	public static String generateIsbn() {
		int n = new Random().nextInt(900000000) + 1000000000;
		return String.valueOf(n);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Book book = (Book) o;
		return Objects.equals(isbn, book.isbn);
	}

	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}
}
