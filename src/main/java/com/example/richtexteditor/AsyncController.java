package com.example.richtexteditor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
@RestController
public class AsyncController {

    @Async
    @GetMapping("/process-task")
    public CompletableFuture<ResponseEntity<String>> processTask() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Giả lập một tác vụ chậm mất 5 giây
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                return ResponseEntity.status(500).body("Task interrupted");
            }
            return ResponseEntity.ok("Task completed!");
        });
    }
}
