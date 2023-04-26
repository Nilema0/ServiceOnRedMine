package redmine.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Priorities {
    @JsonProperty("issue_priorities")
    private List<Priority> priorities;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Priority {
        private int id;
        private String name;
    }
}
