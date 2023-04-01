package RedMine;

import RedMine.bean.Issues;
import RedMine.bean.request.IssueRequest;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static RedMine.RequestValidator.*;
@Service
@NoArgsConstructor
public class RedmineService {

    private final WebClient client = WebClient.create("http://localhost:3000");

    public Issues getIssues(int id) {
        return client.get()
                .uri("/issues.json?assigned_to_id=" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Issues.class)
                .block();
    }

    public void postIssue(IssueRequest data) {
        validateOrderRequest(data);
        client.post()
                .uri("/issues.json")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic YWRtaW46YWRtaW5hZG1pbg==")
                .body(Mono.just(data), IssueRequest.class)
                .retrieve()
                .bodyToMono(Issues.class)
                .block();
    }
}