package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rw.rutaks.deja_vu.dtos.ScheduleConfig;
import rw.rutaks.deja_vu.interfaces.Schedule;
import rw.rutaks.deja_vu.models.ScheduleElement;
import rw.rutaks.deja_vu.utils.ScheduleJsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ScheduleJsonParserTest {

    @Test
    void canParseAndEvaluateTheJson_WithDifferenceIntersectionUnionDayInMonthAndRangeEveryYear() throws JsonProcessingException {
        ScheduleConfig config = JSON_firstAndThirdMondayFromAprilToOctoberApartFromFirstAndSecondJan();
        List<ScheduleElement> elements = ScheduleJsonParser.parse(config);
        Schedule schedule = Schedule.of(elements);
        LocalDate localDate = LocalDate.of(2023, 10, 2);

        Assertions.assertTrue(schedule.isOccurring(localDate));
        Assertions.assertEquals(100, schedule.slots(localDate));
    }

    @Test
    void canParseAndEvaluateTheJson_WithStartMonthToEndMonth() throws JsonProcessingException {
        ScheduleConfig config = JSON_JanToMarch();
        List<ScheduleElement> elements = ScheduleJsonParser.parse(config);
        Schedule schedule = Schedule.of(elements);
        LocalDate desiredDate = LocalDate.of(2023, 1, 2);
        LocalDate outOfScopeDate = LocalDate.of(2023, 4, 2);

        assertTrue(schedule.isOccurring(desiredDate));
        Assertions.assertEquals(100, schedule.slots(desiredDate));

        Assertions.assertFalse(schedule.isOccurring(outOfScopeDate));
        Assertions.assertEquals(0, schedule.slots(outOfScopeDate));
    }

    @Test
    void canParseAndEvaluateTheJson_WithInvalidScheduleConfig() throws JsonProcessingException {
        ScheduleConfig config = new ScheduleConfig();
        Assertions.assertThrows(NullPointerException.class, () -> ScheduleJsonParser.parse(config));
    }

    @Test
    void canParseAndEvaluateTheJson_WithInvalidDayInWeek() throws JsonProcessingException {
        ScheduleConfig config = JSON_InvalidDayInWeek();
        Assertions.assertThrows(DateTimeException.class, () -> ScheduleJsonParser.parse(config));
    }

    @Test
    void canParseAndEvaluateTheJson_WithInvalidStartDay() throws JsonProcessingException {
        ScheduleConfig config = JSON_InvalidStartDay();
        Assertions.assertThrows(DateTimeException.class, () -> ScheduleJsonParser.parse(config));
    }

    @Test
    void canParseAndEvaluateTheJson_WithMissingStartDay() throws JsonProcessingException {
        ScheduleConfig config = JSON_MissingStartDay();
        Assertions.assertThrows(DateTimeException.class, () -> ScheduleJsonParser.parse(config));
    }

    @Test
    void canParseAndEvaluateTheJson_WithInvalidEndDay() throws JsonProcessingException {
        ScheduleConfig config = JSON_InvalidEndDay();
        Assertions.assertThrows(DateTimeException.class, () -> ScheduleJsonParser.parse(config));
    }

    static ScheduleConfig JSON_JanToMarch() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        var json =
            "{\n" +
            "    \"schedule\": [\n" +
            "        {\n" +
            "          \"slots\": 100,\n" +
            "          \"type\": \"RANGE_EVERY_YEAR\",\n" +
            "          \"of\": \"START_MONTH_TO_END_MONTH\",\n" +
            "          \"startMonth\": \"JANUARY\",\n" +
            "          \"endMonth\": \"MARCH\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
        return objectMapper.readValue(json, ScheduleConfig.class);
    }

    static ScheduleConfig JSON_InvalidDayInWeek() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        var json =
            "{\n" +
            "    \"schedule\": [\n" +
            "        {\n" +
            "          \"slots\": 100,\n" +
            "          \"type\": \"DAY_IN_WEEK\",\n" +
            "          \"day\": null\n" +
            "        }\n" +
            "    ]\n" +
            "}";
        return objectMapper.readValue(json, ScheduleConfig.class);
    }

    static ScheduleConfig JSON_InvalidStartDay() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        var json =
            "{\n" +
            "    \"schedule\": [\n" +
            "        {\n" +
            "          \"slots\": 100,\n" +
            "          \"type\": \"RANGE_EVERY_YEAR\",\n" +
            "          \"of\": \"START_DAY_TO_END_DAY\",\n" +
            "          \"startDate\": \"13-01\",\n" +
            "          \"endDate\": \"11-21\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
        return objectMapper.readValue(json, ScheduleConfig.class);
    }

    static ScheduleConfig JSON_MissingStartDay() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        var json =
            "{\n" +
            "    \"schedule\": [\n" +
            "        {\n" +
            "          \"slots\": 100,\n" +
            "          \"type\": \"RANGE_EVERY_YEAR\",\n" +
            "          \"of\": \"START_DAY_TO_END_DAY\",\n" +
            "          \"startDate\": \"13-\",\n" +
            "          \"endDate\": \"11-11\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
        return objectMapper.readValue(json, ScheduleConfig.class);
    }

    static ScheduleConfig JSON_InvalidEndDay() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        var json =
            "{\n" +
            "    \"schedule\": [\n" +
            "        {\n" +
            "          \"slots\": 100,\n" +
            "          \"type\": \"RANGE_EVERY_YEAR\",\n" +
            "          \"of\": \"START_DAY_TO_END_DAY\",\n" +
            "          \"startDate\": \"11-01\",\n" +
            "          \"endDate\": \"11-41\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
        return objectMapper.readValue(json, ScheduleConfig.class);
    }

    static ScheduleConfig JSON_firstAndThirdMondayFromAprilToOctoberApartFromFirstAndSecondJan() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        var json =
            "{\n" +
            "  \"schedule\": [\n" +
            "    {\n" +
            "      \"slots\": 100,\n" +
            "      \"type\": \"DIFFERENCE\",\n" +
            "      \"includedDate\": {\n" +
            "        \"type\": \"INTERSECTION\",\n" +
            "        \"expressions\": [\n" +
            "          {\n" +
            "            \"type\": \"UNION\",\n" +
            "            \"expressions\": [\n" +
            "              {\n" +
            "                \"type\": \"DAY_IN_MONTH\",\n" +
            "                \"day\": \"MONDAY\",\n" +
            "                \"ordinal\": 1\n" +
            "              },\n" +
            "              {\n" +
            "                \"type\": \"DAY_IN_MONTH\",\n" +
            "                \"day\": \"MONDAY\",\n" +
            "                \"ordinal\": 3\n" +
            "              }\n" +
            "            ]\n" +
            "          },\n" +
            "          {\n" +
            "            \"type\": \"RANGE_EVERY_YEAR\",\n" +
            "            \"of\": \"START_MONTH_TO_END_MONTH\",\n" +
            "            \"startMonth\": \"APRIL\",\n" +
            "            \"endMonth\": \"OCTOBER\"\n" +
            "          },\n" +
            "\n" +
            "          {\n" +
            "            \"type\": \"DAY_IN_WEEK\",\n" +
            "            \"day\": \"MONDAY\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      \"excludedDate\": {\n" +
            "        \"type\": \"UNION\",\n" +
            "        \"expressions\": [\n" +
            "          {\n" +
            "            \"type\": \"RANGE_EVERY_YEAR\",\n" +
            "            \"of\": \"START_DAY_TO_END_DAY\",\n" +
            "            \"startDate\": \"01-01\",\n" +
            "            \"endDate\": \"01-01\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"type\": \"RANGE_EVERY_YEAR\",\n" +
            "            \"of\": \"START_DAY_TO_END_DAY\",\n" +
            "            \"startDate\": \"01-02\",\n" +
            "            \"endDate\": \"01-02\"\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}\n";

        return objectMapper.readValue(json, ScheduleConfig.class);
    }
}
