package org.example.api_project.services;

import org.example.api_project.dto.ModelAnswerRequest;
import org.example.api_project.dto.ModelAnswerResponse;
import org.example.api_project.dto.ResponseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
public class NetworkService {
    private final WebClient httpClient = WebClient.builder().build();

    public ResponseInfo<ModelAnswerResponse> makeResponse(String url, ModelAnswerRequest request) {
        try {
            var temp = httpClient.post()
                    .uri(url)
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            response -> response.bodyToMono(String.class).map(Exception::new))
                    .onStatus(HttpStatusCode::is5xxServerError,
                            response -> response.bodyToMono(String.class).map(Exception::new));

            var response = temp
                    .bodyToMono(ModelAnswerResponse.class)
                    .block();

            return new ResponseInfo<>(HttpStatus.OK,
                    Optional.ofNullable(response),
                    "OK");
        } catch (Exception e) {
            return new ResponseInfo<>(HttpStatus.INTERNAL_SERVER_ERROR,
                    Optional.empty(),
                    "Internal Server Error");
        }
    }

}
