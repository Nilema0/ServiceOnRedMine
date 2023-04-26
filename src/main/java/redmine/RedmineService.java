package redmine;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import redmine.bean.*;
import redmine.bean.request.IssueRequest;
import redmine.bean.request.IssueRequestToGet;
import redmine.util.ParserCsv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static redmine.util.RequestValidatorUtil.validateOrderRequest;
import static redmine.util.RequirementCheckerUtil.*;
import static redmine.util.StringHolderUtil.*;

@Service
@NoArgsConstructor
@Slf4j
public class RedmineService {
    private final WebClient client = WebClient.create(REDMINE_URL);

    public void createIssue(final IssueRequest data) {
        validateOrderRequest(data);
        validate(data);

        client.post()
                .uri("/issues.json")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, AUTH_DATA)
                .body(Mono.just(data), IssueRequest.class)
                .retrieve()
                .bodyToMono(Issues.class)
                .block();
    }

    public void validate(final IssueRequest data) {
        val message = new StringBuilder();
        val issue = data.getIssue();
        if (!checkTracker(issue.getTrackerId(), getTrackers())) {
            message.append(String.format("Нет трекера с id %d.", issue.getTrackerId()));
        }
        if (!checkProjects(issue.getProjectId(), getProjects())) {
            message.append(String.format("Нет проекта с id %d.", issue.getProjectId()));
        }
        if (!checkStatuses(issue.getStatusId(), getStatuses())) {
            message.append(String.format("Нет статуса с  id %d.", issue.getStatusId()));
        }
        if (!checkUsers(issue.getAssignedTo(), getUsers())) {
            message.append(String.format("Нет пользователя с таким id %d.", issue.getAssignedTo()));
        }
        if (!checkPriorities(issue.getPriorityId(), getPriorities())) {
            message.append(String.format("Нет приоритета с таким d %d.", issue.getPriorityId()));
        }

        if (message.length() > 0) {
            throw new ValidationFailedException(message.toString());
        }
    }

    public Trackers getTrackers() {
        return client.get()
                .uri("/trackers.json")
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, AUTH_DATA)
                .retrieve()
                .bodyToMono(Trackers.class)
                .block();
    }

    public Projects getProjects() {
        return client.get()
                .uri("/projects.json")
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, AUTH_DATA)
                .retrieve()
                .bodyToMono(Projects.class)
                .block();
    }

    public Statuses getStatuses() {
        return client.get()
                .uri("/issue_statuses.json")
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, AUTH_DATA)
                .retrieve()
                .bodyToMono(Statuses.class)
                .block();
    }

    public Users getUsers() {
        return client.get()
                .uri("/users.json")
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, AUTH_DATA)
                .retrieve()
                .bodyToMono(Users.class)
                .block();
    }

    public Priorities getPriorities() {
        return client.get()
                .uri("/enumerations/issue_priorities.json")
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, AUTH_DATA)
                .retrieve()
                .bodyToMono(Priorities.class)
                .block();
    }

    public void createSeveralIssues(String filePath) throws IOException {
        List<IssueRequestToGet> receivedData = ParserCsv.parse(filePath);
        List<IssueRequest> sentData = new ArrayList<>();
        for (IssueRequestToGet datum : receivedData) {
            sentData.add(getIdByName(datum, getProjects(), getStatuses(), getTrackers(),
                    getUsers(), getPriorities()));
        }
        sentData.forEach(this::createIssue);
    }
    public IssueRequest getIdByName(IssueRequestToGet data, Projects projects, Statuses statuses,
                                    Trackers trackers, Users users, Priorities priorities) {
        IssueRequest dataForPost = new IssueRequest();
        val issue = data.getIssue();
        val issueToPost = dataForPost.getIssue();
        for (val pr : projects.getProjectList()) {
            if (issue.getProjectName().equalsIgnoreCase(pr.getName()))
                issueToPost.setProjectId(pr.getId());
        }
        for (val st : statuses.getStatuses()) {
            if (issue.getStatusName().equalsIgnoreCase(st.getName()))
                issueToPost.setStatusId(st.getId());
        }
        for (val tr : trackers.getTrackers()) {
            if (issue.getTrackerName().equalsIgnoreCase(tr.getName()))
                issueToPost.setTrackerId(tr.getId());
        }
        for(val us : users.getUserList()){
            StringBuilder name = new StringBuilder();
            name.append(us.getFirstName())
                    .append(" ")
                    .append(us.getLastName());
            if(issue.getAssigned().equalsIgnoreCase(name.toString()))
                issueToPost.setAssignedTo(us.getId());
        }
        for(val pr : priorities.getPriorities()){
            if(issue.getPriorityName().equalsIgnoreCase(pr.getName()))
                issueToPost.setPriorityId(pr.getId());
        }
        issueToPost.setStartDate(issue.getStartDate());
        issueToPost.setEndDate(issue.getEndDate());
        issueToPost.setSubject(issue.getSubject());

        issueToPost.getCustomList().add(new IssueRequest.IssueMiniRequest.customFields(6,"1"));
        issueToPost.getCustomList().add(new IssueRequest.IssueMiniRequest.customFields(4,"week"));

        return dataForPost;
    }
}