package com.daki.apitesting;

import com.daki.apitesting.http.LageriClient;
import com.daki.apitesting.http.PostsClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

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
		log.info(postsClient.getPosts()[0].getTitle());
	}

	@Test
	void contextLoads2() {
		log.info(lageriClient.getItems()[0].getDescriptionMk());
	}

}
