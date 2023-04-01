package RedMine.bean.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IssueRequest {
    IssueMiniRequest issue;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class IssueMiniRequest {
        int project_id;
        int tracker_id;
        int status_id;
        int priority_id;
        String subject;
        String description;
        int assigned_to_id;
        boolean is_private;
        int estimated_hours;
    }
}
