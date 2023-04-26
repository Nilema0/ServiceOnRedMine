package redmine;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import redmine.bean.request.IssueRequest;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class RedmineController {
    private final RedmineService service = new RedmineService();

    //создание задачи вручную
    @PostMapping("/issues")
    String postIssue(@Valid @RequestBody IssueRequest dataForIssue) {
        service.createIssue(dataForIssue);
        return ("Задача создана, её параметры: " + dataForIssue.toString());
    }

    //создание задачи с параметрами из csv файла
    @PostMapping("/parse")
    String parse(@RequestBody String filePath) throws IOException {
        service.createSeveralIssues(filePath);
        return("Задача создана");
    }
}