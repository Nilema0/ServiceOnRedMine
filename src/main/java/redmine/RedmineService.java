package redmine;

import io.netty.handler.logging.LogLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;
import redmine.bean.*;
import redmine.bean.request.IssueRequest;
import redmine.bean.request.IssueRequestToGet;
import redmine.util.ParserCsv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static redmine.util.RequestValidatorUtil.validateOrderRequest;
import static redmine.util.RequirementCheckerUtil.*;
import static redmine.util.StringHolderUtil.*;

@Service
@NoArgsConstructor
@Slf4j
public class RedmineService {
    private final WebClient client = WebClient
            .builder()
            .baseUrl(REDMINE_URL)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    private final static HttpClient httpClient = HttpClient
            .create()
            .wiretap("reactor.netty.http.client.HttpClient",
                    LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);

    public void createIssue(final IssueRequest data) throws ExecutionException, InterruptedException {
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
                .toFuture()
                .get();
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
        if (!checkUsers(issue.getAssignedTo(), getUsers()) && issue.getAssignedTo() != 0) {
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

    public void createSeveralIssues(String filePath) throws IOException, ExecutionException, InterruptedException {
        val receivedData = ParserCsv.parse(filePath);
        List<IssueRequest> sentData = new ArrayList<>();
        for (val datum : receivedData) {
            sentData.add(getIdByName(datum, getProjects(), getStatuses(), getTrackers(),
                    getUsers(), getPriorities()));
        }

        for (val sentDatum : sentData) {
            String nameOfIssue = sentDatum.getIssue().getSubject();
            sentDatum.getIssue().setSubject(nameOfIssue + "(1)");
            createIssue(sentDatum);
            for (int index = 1; index < sentDatum.getIssue().getNumber(); index++) {
                sentDatum.getIssue().setSubject(nameOfIssue + "(" + (index + 1) + ")");
                sentDatum.getIssue().setStartDate(editDate
                        (sentDatum.getIssue().getStartDate(),
                                sentDatum.getIssue().getCustomList().get(1).getValue(),
                                Integer.parseInt(sentDatum.getIssue().getCustomList().get(0).getValue())));
                sentDatum.getIssue().setEndDate(editDate
                        (sentDatum.getIssue().getEndDate(),
                                sentDatum.getIssue().getCustomList().get(1).getValue(),
                                Integer.parseInt(sentDatum.getIssue().getCustomList().get(0).getValue())));
                createIssue(sentDatum);
            }
        }
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
        if (issue.getAssigned().equals(""))
            issueToPost.setAssignedTo(1);
        else {
            for (val us : users.getUserList()) {
                StringBuilder name = new StringBuilder();
                name.append(us.getFirstName())
                        .append(" ")
                        .append(us.getLastName());
                if (issue.getAssigned().equalsIgnoreCase(name.toString()))
                    issueToPost.setAssignedTo(us.getId());
            }
        }
        for (val pr : priorities.getPriorities()) {
            if (issue.getPriorityName().equalsIgnoreCase(pr.getName()))
                issueToPost.setPriorityId(pr.getId());
        }


        issueToPost.getCustomList().add(new IssueRequest.IssueMiniRequest.customFields(
                6, data.getIssue().getCustomList().get(1).getValue()));
        issueToPost.getCustomList().add(new IssueRequest.IssueMiniRequest.customFields(
                4, data.getIssue().getCustomList().get(0).getValue()));

        issueToPost.setStartDate(issue.getStartDate());
        issueToPost.setEndDate(issue.getEndDate());
        issueToPost.setSubject(issue.getSubject());
        issueToPost.setNumber(issue.getNumber());

        return dataForPost;
    }

    public String editDate(String date, String type, int value) {
        Calendar calendar = new GregorianCalendar();
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7)) - 1;
        int day = Integer.parseInt(date.substring(8));
        calendar.set(year, month, day);
        switch (type) {
            case "year":
                calendar.add(Calendar.YEAR, value);
                break;
            case "month":
                calendar.add(Calendar.MONTH, value);
                break;
            case "week":
                calendar.add(Calendar.DAY_OF_MONTH, 7 * value);
                break;
            case "day":
                calendar.add(Calendar.DAY_OF_MONTH, value);
                break;
            case "hour":
                calendar.add(Calendar.HOUR, value);
                break;
        }
        StringBuilder dateEdited = new StringBuilder();
        dateEdited.append(calendar.get(Calendar.YEAR));
        dateEdited.append("-");
        if (calendar.get(Calendar.MONTH) < 10)
            dateEdited.append("0");
        dateEdited.append(calendar.get(Calendar.MONTH) + 1);
        dateEdited.append("-");
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10)
            dateEdited.append("0");
        dateEdited.append(calendar.get(Calendar.DAY_OF_MONTH));
        return dateEdited.toString();
    }

    public String getListPriorities(){
        List<String> prioritiesName = new ArrayList<>();
        val prioritiesList = getPriorities().getPriorities();
        for (val priority : prioritiesList) {
            prioritiesName.add(priority.getName());
        }
        return prioritiesName.toString();
    }

    public String getListStatuses(){
        val statusesList = getStatuses().getStatuses();
        List<String> statusesName = new ArrayList<>();
        for (val status : statusesList) {
            statusesName.add(status.getName());
        }
        return statusesName.toString();
    }
    public String getListTrackers(){
        val trackersList = getTrackers().getTrackers();
        List<String> trackersName = new ArrayList<>();
        for (val tracker : trackersList) {
            trackersName.add(tracker.getName());
        }
        return trackersName.toString();
    }
    public String getListUsers(){
        val usersList = getUsers().getUserList();
        List<String> usersName = new ArrayList<>();
        for(val user : usersList){
            String name = user.getFirstName() +
                    " " +
                    user.getLastName();
            usersName.add(name);
        }
        return usersName.toString();
    }
    public String getListProjects(){
        val projectsList = getProjects().getProjectList();
        List<String> projectsName = new ArrayList<>();
        for(val project : projectsList){
            projectsName.add(project.getName());
        }
        return projectsName.toString();
    }

}