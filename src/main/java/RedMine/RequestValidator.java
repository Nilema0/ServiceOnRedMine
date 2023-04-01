package RedMine;

import RedMine.bean.request.IssueRequest;
import lombok.experimental.UtilityClass;
import lombok.val;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@UtilityClass
public class RequestValidator {
    private final Validator validator;
    static {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()){
            validator = factory.getValidator();
        }
    }
    public void validateOrderRequest(final IssueRequest issueRequest) {
        val violations = validator.validate(issueRequest);
        if (violations.size() > 0) {
            StringBuilder mistake = new StringBuilder();
            for (val violation : violations) {
                mistake.append(violation.getMessage());
                mistake.append("\n");
            }
            throw new ValidationFailedException(mistake.toString());
        }
    }
}
