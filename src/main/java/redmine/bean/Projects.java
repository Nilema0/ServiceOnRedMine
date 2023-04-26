package redmine.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Projects {
    @JsonProperty("projects")
    private List<Project> projectList;
    @JsonProperty("total_count")
    int totalCount;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Project {
        private int id;
        private String name;
    }
}
