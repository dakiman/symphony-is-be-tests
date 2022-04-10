package com.daki.apitesting.http;

import com.daki.apitesting.model.Item;
import com.daki.apitesting.model.Post;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class PostsClient extends BaseClient {

    @Value("${services.mock-server.url}")
    private String baseUrl;

    @Override
    protected String getBaseUrl() {
        return baseUrl;
    }

    public Post[] getPosts() {
        return executeRequest(HttpMethod.GET, "/posts", Post[].class);
    }
}
