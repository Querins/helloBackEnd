package com.test.helloBackEnd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;
import com.test.helloBackEnd.jpa.Contact;
import com.test.helloBackEnd.jpa.ContactsRepository;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HelloBackEndApplicationTests {

	private final Logger logger = LoggerFactory.getLogger(HelloBackEndApplicationTests.class);

	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	ObjectMapper mapper;

	@Autowired
	private ContactsRepository repository;
	private Random r = new Random(System.currentTimeMillis());

	private final int SIZE = 100;
	private final int LENGTH = 10;

	@Before
	public void populateRepo() {

		RandomStringGenerator generator = new RandomStringGenerator.Builder()
				.withinRange('a', 'z').build();

		for(int i = 0; i < SIZE; i++) {
			String randString = generator.generate(LENGTH);
			Contact c = new Contact(i, randString);
			restTemplate.put("/hello/contacts", c);
		}

	}

	@Test
	public void filterTest() throws IOException, URISyntaxException {

		List<String> patterns = Arrays.asList(".*", "[^R].*", "[^zqa]*", ".*a"); // patterns for test
		for(String pattern : patterns) {

			String url = "/hello/contacts?nameFilter=" + URLEncoder.encode(pattern, "UTF-8"); // URL should be encoded to avoid illegal chars
			Map<String, List<Contact>> actual = restTemplate.getForObject(url, Map.class);

			Contact[] contacts = mapper.convertValue( actual.get("contacts"), Contact[].class );

			List<Contact> expected = StreamSupport.stream(repository.findAll().spliterator(), false)
					.filter( contact -> contact.getName().matches(pattern) )
					.collect(Collectors.toList());

			for(int i = 0; i < contacts.length; i++) {
				assertTrue(contacts[i].equals(expected.get(i)));
			}

		}

	}

	@Test
	public void getContact() {

		for(int i = 0; i < SIZE; i++) {

			Contact contact = restTemplate.getForObject("/hello/contacts/{id}", Contact.class, i);
			assertThat(contact).isEqualTo(repository.findOne((long) i));
		}
	}

	@Test
	public void deleteArbitraryContact() {

		long rand = r.nextInt(SIZE);

		restTemplate.delete("/hello/contacts/{id}", rand);
		assertThat(repository.findOne(rand)).isNull();

		assertThat(
				restTemplate.getForObject("/hello/contacts/{id}", Contact.class, rand)
		).isNull();

	}

	@Test
	public void deleteNonExistingContact() throws URISyntaxException {

		long valueOutOfRange = r.nextInt(SIZE + 1) + SIZE; // generate random value out of range

		// try to get removed Contact
		ResponseEntity<Void> resp = restTemplate.exchange(new RequestEntity<Contact>(
				HttpMethod.DELETE, new URI("/hello/contacts/" + valueOutOfRange)
		), Void.class);

		// assure that removed Contact is not found by the service
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

	}

}
