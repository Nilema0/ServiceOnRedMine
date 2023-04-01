package RedMine;

import RedMine.bean.request.IssueRequest;
import lombok.val;
import org.apache.commons.csv.CSVFormat;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserCsv {
    final static String[] head= {"project_id", "tracker_id", "status_id", "priority_id",
            "subject", "description", "assigned_to_id", "is_private", "estimated_hours"};
    public static List<IssueRequest> Parse(final String csvFilePath) throws IOException {
        try (val reader = new FileReader(csvFilePath.trim())) {
            final List<IssueRequest> issues = new ArrayList<>();
            CSVFormat.DEFAULT
                    .builder()
                    .setHeader(head)
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader)
                    .getRecords()
                    .forEach(csvRecord ->
                            issues.add(IssueRequest.builder()
                                    .issue(IssueRequest.IssueMiniRequest.builder()
                                            .project_id(Integer.parseInt(csvRecord.get(0)))
                                            .tracker_id(Integer.parseInt(csvRecord.get(1)))
                                            .status_id(Integer.parseInt(csvRecord.get(2)))
                                            .priority_id(Integer.parseInt(csvRecord.get(3)))
                                            .subject(csvRecord.get(4))
                                            .description(csvRecord.get(5))
                                            .assigned_to_id(Integer.parseInt(csvRecord.get(6)))
                                            .is_private(Boolean.parseBoolean(csvRecord.get(7)))
                                            .estimated_hours(Integer.parseInt(csvRecord.get(8)))
                                            .build())
                                    .build())
                    );
            return issues;
        }
    }
}
