package ir.desibell.notificationService.processes.payment;

import com.google.gson.Gson;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

public class HttpUtilManager {

    private static HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15000)
            .responseTimeout(Duration.ofMillis(15000))
            .doOnConnected(conn
                    -> conn.addHandlerLast(new ReadTimeoutHandler(15000, TimeUnit.MILLISECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(15000, TimeUnit.MILLISECONDS)));

    private WebClient webClientInstance(String baseUrl, String authorization) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, authorization)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    private RequestBodySpec httpGetMethod(String baseUrl, String url, String authorization) {
        WebClient client = this.webClientInstance(baseUrl, authorization);
        UriSpec<RequestBodySpec> uriSpec = client.method(HttpMethod.GET);
        RequestBodySpec bodySpec = uriSpec.uri(url);
        return bodySpec;
    }

    private RequestBodySpec httpPostMethod(String baseUrl, String url, String authorization) {
        WebClient client = this.webClientInstance(baseUrl, authorization);
        UriSpec<RequestBodySpec> uriSpec = client.method(HttpMethod.POST);
        RequestBodySpec bodySpec = uriSpec.uri(url);
        return bodySpec;
    }

    public Mono<String> requestHttpGet(String baseUrl, String url, String authorization, Map<String, Object> bodyMap) {
        RequestBodySpec bodySpec = this.httpGetMethod(baseUrl, url, authorization);
        String bodyRow = new Gson().toJson(bodyMap);
        RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(bodyRow);
        ResponseSpec responseSpec = headersSpec
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifNoneMatch("*")
                .ifModifiedSince(ZonedDateTime.now())
                .retrieve();

        Mono<String> response = responseSpec.bodyToMono(String.class);

        return response;
    }

    public Mono<String> requestHttpPost(String baseUrl, String url, String authorization, Map<String, Object> bodyMap) {
        RequestBodySpec bodySpec = this.httpPostMethod(baseUrl, url, authorization);
        String bodyRow = new Gson().toJson(bodyMap);
        RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(bodyRow);
        ResponseSpec responseSpec = headersSpec
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifNoneMatch("*")
                .ifModifiedSince(ZonedDateTime.now())
                .retrieve();

        Mono<String> response = responseSpec.bodyToMono(String.class);

        return response;
    }
}
