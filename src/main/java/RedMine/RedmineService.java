package RedMine;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse.BodyHandlers;

import static RedMine.RequestCreatorUtil.getRedMineIssuesRequest;
import static RedMine.RequestCreatorUtil.getRedMineProjectsRequest;

@Service
public class RedmineService {
    HttpClient httpClient = HttpClient.newHttpClient();
    PostProjectRequest postProjectRequest;

    public String getIssues() throws URISyntaxException, IOException, InterruptedException {
        return httpClient.send(
                getRedMineIssuesRequest(),
                BodyHandlers.ofString())
                .body();
    }

    public String getProjects() throws URISyntaxException, IOException, InterruptedException {
        return httpClient.send(
                getRedMineProjectsRequest(),
                BodyHandlers.ofString())
                .body();
    }
}