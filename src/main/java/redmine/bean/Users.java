package redmine.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @JsonProperty("users")
    private List<User> userList;
    @JsonProperty("total_count")
    int totalCount;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private int id;
        @JsonProperty("firstname")
        private String firstName;
        @JsonProperty("lastname")
        private String lastName;
    }
}
