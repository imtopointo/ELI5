package com.eli5.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ExplainController {

   @PostMapping("/api/explain")
public String explain(@RequestBody ExplainRequest request) {

    int level = request.level();
    String question = request.question();

    return OpenAIClient.ask(
        "Explain this at level " + level + ": " + question
    );
}


}

