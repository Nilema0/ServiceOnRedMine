package RedMine;

import RedMine.bean.Issues;
import RedMine.request.IssueRequest;
import org.springframework.web.bind.annotation.*;

@RestController
public class RedmineController {
    RedmineService service = new RedmineService();

    @GetMapping("/issues/{id}")
    Issues getIssues(@PathVariable int id) {
        return service.getIssues(id);
    }

    @PostMapping("/issues")
    String postIssue(@RequestBody IssueRequest dataForIssue) {
        service.postIssue(dataForIssue);
        return ("Задача создана, её параметры: " + dataForIssue.toString());
    }
}