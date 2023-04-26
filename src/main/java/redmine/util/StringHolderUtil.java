package redmine.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringHolderUtil {
    public final String[] HEAD = {"project_id", "tracker_id", "status_id", "priority_id",
            "subject", "description", "assigned_to_id", "is_private", "estimated_hours"};

    public final String AUTH_DATA = "Basic YWRtaW46YWRtaW5hZG1pbg==";
    public final String AUTHORIZATION = "Authorization";
    public final String REDMINE_URL = "http://localhost:3000";
}
