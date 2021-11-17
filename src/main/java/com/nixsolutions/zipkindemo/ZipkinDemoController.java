package com.nixsolutions.zipkindemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Main controller")
public class ZipkinDemoController {

    @Value("${urls.data-service}")
    private String url;

    private final Tracer tracer;
    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;

    @GetMapping("/text")
    @NewSpan(name = "getTextSpan")
    @Operation(
            summary = "get text endpoint",
            responses = {@ApiResponse(description = "get text success", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))}
    )
    public ResponseEntity<String> getText() throws JsonProcessingException {
        log.info("In getText() method");
        TextData text = TextData.builder()
                .title("title")
                .content("input content")
                .pages(34)
                .build();
        Objects.requireNonNull(tracer.currentSpan()).tag("input", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(text));
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        text.setContent(response.getBody());
        Objects.requireNonNull(tracer.currentSpan()).tag("output", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(text));
        return ResponseEntity.ok().header("correlationId", Objects.requireNonNull(tracer.currentSpan()).context().traceId()).body("get text");
    }

    @PostMapping("/text")
    @NewSpan(name = "postTextSpan")
    @Operation(summary = "post text endpoint")
    public ResponseEntity<String> postText(@RequestBody String text) {
        log.info(text);
        return ResponseEntity.ok().header("correlationId", Objects.requireNonNull(tracer.currentSpan()).context().traceId()).body(text + " returned");
    }
}
