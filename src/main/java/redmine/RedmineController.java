package redmine;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import redmine.bean.request.IssueRequest;

import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class RedmineController {
    private final RedmineService service = new RedmineService();

    //создание задачи вручную
    @PostMapping("/issues")
    String postIssue(@Valid @RequestBody IssueRequest dataForIssue) throws ExecutionException, InterruptedException {
        service.createIssue(dataForIssue);
        return ("Задача создана, её параметры: " + dataForIssue.toString());
    }

    //создание задачи с параметрами из csv файла
    @PostMapping("/parse")
    String parse(@RequestBody String filePath) throws IOException, ExecutionException, InterruptedException {
        service.createSeveralIssues(filePath);
        return("Задача создана");
    }
    @GetMapping("/trackers")
    String getTrackers(){
        return service.getListTrackers();
    }
    @GetMapping("/priorities")
    String getPriorities(){
        return service.getListPriorities();
    }
    @GetMapping("/statuses")
    String getStatuses(){
        return service.getListStatuses();
    }
    @GetMapping("/users")
    String getUsers(){
        return service.getListUsers();
    }
    @GetMapping("/projects")
    String getProjects(){
        return service.getListProjects();
    }
}