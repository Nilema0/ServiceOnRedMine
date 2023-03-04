package RedMine;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

public class RequestCreatorUtil {

    public static HttpRequest getRedMineIssuesRequest() throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/issues.xml"))
                .GET()
                .build();
    }
}
