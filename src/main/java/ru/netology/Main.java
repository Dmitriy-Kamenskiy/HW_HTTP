package ru.netology;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {
    public static final String REMOTE_SERVICE_URI
            = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("My Test Service")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        // создание объекта запроса с произвольными заголовками
        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        // отправка запроса
        CloseableHttpResponse response = httpClient.execute(request);
        // вывод полученных заголовков
        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
        // чтение тела ответа
        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
        System.out.println(body);
        readAnswer(body);
    }

    static void readAnswer(String body) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();

        List <Post> posts = mapper.readValue(body, new TypeReference<>() {});

        posts.stream().filter(o->o.getUpvotes()!=null).forEach(System.out::println);
    }

}