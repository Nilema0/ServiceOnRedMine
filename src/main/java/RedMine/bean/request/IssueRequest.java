package RedMine.bean.request;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IssueRequest {
    @Valid
    @NotNull(message = "Параметры задачи не могут быть пустыми")
    IssueMiniRequest issue;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class IssueMiniRequest {

        @PositiveOrZero(message = "Id проекта не может быть отрицательным")
        int project_id;
        @PositiveOrZero(message = "Id трекера не может быть отрицательным")
        int tracker_id;
        @PositiveOrZero(message = "Id статуса не может быть отрицательным")
        int status_id;
        @PositiveOrZero(message = "Id приоритета не может быть отрицательным")
        int priority_id;
        String subject;
        String description;
        @PositiveOrZero(message = "Id пользователя не может быть отрицательным")
        int assigned_to_id;
        boolean is_private;
        int estimated_hours;
    }
}
