package com.net.sparrow.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * ES配置
 */
@Configuration
public class EsConfig {

    @Value("${spring.elasticsearch.host:}")
    private String host;

    @Value("${spring.elasticsearch.port:9200}")
    private int port;

    @Value("${spring.elasticsearch.username:}")
    private String username;

    @Value("${spring.elasticsearch.password:}")
    private String password;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestClientBuilder clientBuilder = RestClient
                .builder(Arrays.stream(host.split(","))
                        .map(s -> new HttpHost(s, port))
                        .toArray(HttpHost[]::new));
        if (StringUtils.hasText(username)) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            clientBuilder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }
        return new RestHighLevelClient(clientBuilder);
    }
}