package redmine.util;

import redmine.ValidationFailedException;
import redmine.bean.request.IssueRequest;
import lombok.experimental.UtilityClass;
import lombok.val;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@UtilityClass
public class RequestValidatorUtil {
    private final Validator validator;
    static {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()){
            validator = factory.getValidator();
        }
    }
    public void validateOrderRequest(final IssueRequest issueRequest) {
        val violations = validator.validate(issueRequest);
        if (violations.size() > 0) {
            StringBuilder exceptionHolder = new StringBuilder();
            for (val violation : violations) {
                exceptionHolder.append(violation.getMessage());
                exceptionHolder.append("\n");
            }
            throw new ValidationFailedException(exceptionHolder.toString());
        }
    }
}
