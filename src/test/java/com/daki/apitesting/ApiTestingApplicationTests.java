package com.daki.apitesting;

import com.daki.apitesting.http.LageriClient;
import com.daki.apitesting.http.PostsClient;
import com.daki.apitesting.model.Item;
import com.daki.apitesting.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class ApiTestingApplicationTests {
	@Value("${data.name}")
	String name;

	@Autowired
	PostsClient postsClient;

	@Autowired
	LageriClient lageriClient;

	@Test
	void contextLoads() {
		ResponseEntity<Item[]> itemsResponse = lageriClient.getItems();
		log.info("Items response {}", itemsResponse);
//		assertEquals(HttpStatus.OK, itemsResponse.getStatusCode());
		assertTrue(itemsResponse.getBody().length > 0);
	}

	@Test
	void contextLoads2() {
		ResponseEntity<Post[]> posts = postsClient.getPosts();

	}

}
