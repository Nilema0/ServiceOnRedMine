package RedMine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class RedmineController {
    @Autowired
    RedmineService service;

    @GetMapping("/issues")
    String getIssues() throws URISyntaxException, IOException, InterruptedException {
        return service.getIssues();
    }

    @GetMapping("/projects")
    String getProjects() throws URISyntaxException, IOException, InterruptedException {
        return service.getProjects();
    }

    /*
    @PostMapping("/projects")
    String postProjects(@RequestBody PostProjectRequest postProjectRequest){
        service.postProject(postProjectRequest);
        return "ok";
    }*/

}