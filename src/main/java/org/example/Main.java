package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("web")
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("get")
    public ResponseEntity<RecordList> get() {
        return ResponseEntity.ok(
                new RecordList(
                        new HashMap<>(Map.of(
                                "a", new ColumnTypeObject("dict", Map.of("b", ColumnTypeScalar.STRING)),
                                "x", ColumnTypeScalar.STRING,
                                "v", new ColumnTypeVirtual("a.b", ColumnTypeScalar.STRING)
                        )),
                        List.of(
                                new HashMap<>(Map.of(
                                        "a", Map.of("b", "hi-1,sw!"),
                                        "x", "hello"
                                )),
                                new HashMap<>(Map.of(
                                        "a", Map.of("b", "hi-2,sw!"),
                                        "x", "hello"
                                )),
                                new HashMap<>(Map.of(
                                        "a", Map.of("b", "hi-3,sw!"),
                                        "x", "hello"
                                )),
                                new HashMap<>(Map.of(
                                        "a", Map.of("b", "hi-4,sw!"),
                                        "x", "hello"
                                )),
                                new HashMap<>(Map.of(
                                        "a", Map.of("b", "hi-5,sw!"),
                                        "x", "hello"
                                ))
                        ),
                        null
                )
        );
    }
}