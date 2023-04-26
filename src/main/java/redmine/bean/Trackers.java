package redmine.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trackers {
    private List<Tracker> trackers;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tracker {
        private int id;
        private String name;
    }
}
