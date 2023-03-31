package RedMine.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IssueRequest {
    IssueMiniRequest issue;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class IssueMiniRequest {
        int project_id;
        int tracker_id;
        int status_id;
        int priority_id;
        String subject;
        int assigned_to_id;
    }
}
