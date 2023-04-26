package redmine.util;

import lombok.experimental.UtilityClass;
import redmine.bean.*;

@UtilityClass
public class RequirementCheckerUtil {
    public boolean checkTracker(int id, Trackers trackers) {
        for (int i = 0; i < trackers.getTrackers().size(); i++) {
            if (trackers.getTrackers().get(i).getId() == id)
                return true;
        }
        return false;
    }

    public boolean checkProjects(int id, Projects projects) {
        for (int i = 0; i < projects.getTotalCount(); i++) {
            if (projects.getProjectList().get(i).getId() == id)
                return true;
        }
        return false;
    }

    public boolean checkStatuses(int id, Statuses statuses) {
        for (int i = 0; i < statuses.getStatuses().size(); i++) {
            if (statuses.getStatuses().get(i).getId() == id)
                return true;
        }
        return false;
    }

    public boolean checkUsers(int id, Users users) {
        for (int i = 0; i < users.getUserList().size(); i++) {
            if (users.getUserList().get(i).getId() == id)
                return true;
        }
        return false;
    }

    public boolean checkPriorities(int id, Priorities priorities) {
        for (int i = 0; i < priorities.getPriorities().size(); i++) {
            if (priorities.getPriorities().get(i).getId() == id)
                return true;
        }
        return false;
    }
}
