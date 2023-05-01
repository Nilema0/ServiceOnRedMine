package redmine.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Issues {
    @JsonProperty("issues")
    private List<Issue> issueList;
    @JsonProperty("total_count")
    int totalCount;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Issue {
        private int id;
        private Project project;
        private Status status;
        private Priority priority;
       @JsonProperty("assigned_to")
        private Assigned assigned;
        private String subject;
        @JsonProperty("start_date")
        private String startDate;
        @JsonProperty("due_date")
        private String endDate;
        @JsonProperty("custom_fields")
        private List<Custom> customFields;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Project {
        private int id;
        private String name;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Status {
        private String name;
        @JsonProperty("is_closed")
        private boolean isClosed;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Priority {
        private int id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Assigned {
        private String firstName;
        private String lastName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Custom {
        private int id;
        private String value;
    }
}
