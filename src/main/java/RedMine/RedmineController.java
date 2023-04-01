package RedMine;

import RedMine.bean.Issues;
import RedMine.bean.request.IssueRequest;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class RedmineController {
    private final RedmineService service = new RedmineService();

    @GetMapping("/issues/{id}")
    Issues getIssues(@PathVariable int id) {
        return service.getIssues(id);
    }

    //создание задачи вручную
    @PostMapping("/issues")
    String postIssue(@RequestBody IssueRequest dataForIssue) {
        service.postIssue(dataForIssue);
        return ("Задача создана, её параметры: " + dataForIssue.toString());
    }

    //создание задачи с параметрами из csv файла
    @PostMapping("/parse")
    void parse(@RequestBody String filePath) throws IOException {
        List<IssueRequest> l = ParserCsv.Parse(filePath);
        for (IssueRequest issueRequest : l) {
            service.postIssue(issueRequest);
        }
    }
}