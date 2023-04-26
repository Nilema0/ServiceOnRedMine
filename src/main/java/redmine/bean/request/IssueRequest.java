package redmine.bean.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IssueRequest {
    @Builder.Default
    IssueMiniRequest issue = new IssueMiniRequest();

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class IssueMiniRequest {

        @JsonProperty("project_id")
        int projectId;
        @JsonProperty("tracker_id")
        int trackerId;
        @JsonProperty("status_id")
        int statusId;
        @JsonProperty("priority_id")
        int priorityId;
        String subject;
        @JsonProperty("assigned_to_id")
        int assignedTo;
        @JsonProperty("start_date")
        String startDate;
        @JsonProperty("due_date")
        String endDate;
        @Builder.Default
        @JsonProperty("custom_fields")
        private List<customFields> customList = new ArrayList<>();
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class customFields {
            private  int id;
            private String value;
        }
    }
}
