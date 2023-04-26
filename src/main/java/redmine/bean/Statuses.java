package redmine.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statuses {
    @JsonProperty("issue_statuses")
    private List<Status> statuses;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Status{
        private int id;
        private String name;
    }
}
