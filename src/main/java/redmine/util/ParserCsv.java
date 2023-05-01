package redmine.util;

import lombok.val;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import redmine.bean.request.IssueRequestToGet;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static redmine.util.StringHolderUtil.*;

public class ParserCsv {
    public static List<IssueRequestToGet> parse(final String csvFilePath) throws IOException {
        try (val reader = new FileReader(csvFilePath.trim())) {
            return CSVFormat.DEFAULT
                    .builder()
                    .setHeader(HEAD)
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader)
                    .getRecords()
                    .stream()
                    .map(ParserCsv::reader)
                    .collect(Collectors.toList());
        }
    }

    private static IssueRequestToGet reader(final CSVRecord data) {
        return IssueRequestToGet.builder()
                .issue(IssueRequestToGet.IssueMiniRequestToGet.builder()
                        .projectName(data.get(0))
                        .trackerName(data.get(1))
                        .statusName(data.get(2))
                        .priorityName(data.get(3))
                        .subject(data.get(4))
                        .assigned(data.get(5))
                        .customList(List.of(
                                IssueRequestToGet.IssueMiniRequestToGet.CustomFields.builder()
                                        .value(data.get(6))
                                        .build(),
                                IssueRequestToGet.IssueMiniRequestToGet.CustomFields.builder()
                                        .value(data.get(7))
                                        .build()
                        ))
                        .startDate(data.get(8))
                        .endDate(data.get(9))
                        .number(Integer.parseInt(data.get(10)))
                        .build())
                .build();
    }
}
