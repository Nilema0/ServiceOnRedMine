package RedMine.bean;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Issues {
    private List<Issue> issues;
    int total_count;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Issue {
        private Status status;
        private Priority priority;
        private Assigned assigned_to;
        private String subject;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Status {
        private String name;
        private boolean is_closed;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Priority {
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Assigned {
        private String name;
    }

}
