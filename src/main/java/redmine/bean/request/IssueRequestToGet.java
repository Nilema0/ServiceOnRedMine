package redmine.bean.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IssueRequestToGet {
    @Valid
    @NotNull(message = "Параметры задачи не могут быть пустыми")
    IssueMiniRequestToGet issue;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class IssueMiniRequestToGet {
        @NotEmpty
        @JsonProperty("project_name")
        String projectName;
        @NotEmpty
        @JsonProperty("tracker_name")
        String trackerName;
        @NotEmpty
        @JsonProperty("status_name")
        String statusName;
        @NotEmpty
        @JsonProperty("priority_name")
        String priorityName;
        String subject;
        @JsonProperty("assigned_to_name")
        String assigned;
        @JsonProperty("start_date")
        String startDate;
        @JsonProperty("due_date")
        String endDate;
        @JsonProperty("custom_fields")
        private List<CustomFields> customList;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class CustomFields {
            private int id;
            private String value;
        }
    }
}
