package rw.rutaks.deja_vu.utils;

import rw.rutaks.deja_vu.dtos.BasicScheduleJsonElement;
import rw.rutaks.deja_vu.dtos.ScheduleConfig;
import rw.rutaks.deja_vu.dtos.ScheduleWithSlotScheduleJsonElement;
import rw.rutaks.deja_vu.enums.TemporalExpressionType;
import rw.rutaks.deja_vu.models.ErrorCodes;
import rw.rutaks.deja_vu.models.DayInMonthTE;
import rw.rutaks.deja_vu.models.DayInWeekTE;
import rw.rutaks.deja_vu.models.DifferenceTE;
import rw.rutaks.deja_vu.models.IntersectionTE;
import rw.rutaks.deja_vu.models.RangeEveryYearTE;
import rw.rutaks.deja_vu.models.ScheduleElement;
import rw.rutaks.deja_vu.interfaces.TemporalExpression;
import rw.rutaks.deja_vu.models.UnionTE;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;

public class ScheduleJsonParser {

    private ScheduleJsonParser() {}

    /**
     * Parses a list of schedule elements from a provided ScheduleConfig.
     * This method takes a ScheduleConfig object, extracts the schedule data from it, and
     * converts it into a list of ScheduleElement objects. Each ScheduleElement represents
     * a schedule with associated slots.
     *
     * @param scheduleConfig The ScheduleConfig object containing schedule information to parse.
     * @return A list of ScheduleElement objects representing the parsed schedule data.
     * @throws NullPointerException if scheduleConfig is null.
     */
    public static List<ScheduleElement> parse(ScheduleConfig scheduleConfig) {
        List<ScheduleWithSlotScheduleJsonElement> scheduleWithSlots = scheduleConfig.getSchedule();
        List<ScheduleElement> elements = new ArrayList<>();
        if (scheduleWithSlots == null) {
            throw new NullPointerException(ErrorCodes.SCHEDULE_NOT_DEFINED);
        }

        for (ScheduleWithSlotScheduleJsonElement element : scheduleWithSlots) {
            elements.add(ScheduleElement.of(parseExpression(element), element.getSlots()));
        }

        return elements;
    }

    /**
     * Parses a TemporalExpression from a BasicScheduleJsonElement.
     * This method takes a BasicScheduleJsonElement and parses it into a TemporalExpression
     * based on its type. The supported types include {@link TemporalExpressionType} . If the provided element has an
     * unsupported type, a DateTimeException is thrown.
     *
     * @param scheduleElement The BasicScheduleJsonElement to be parsed into a TemporalExpression.
     * @return A TemporalExpression representing the parsed schedule element.
     * @throws DateTimeException if the provided element has an unsupported type or is null.
     */
    private static TemporalExpression parseExpression(BasicScheduleJsonElement scheduleElement) {
        if (scheduleElement == null) {
            throw new DateTimeException(ErrorCodes.SCHEDULE_NOT_FOUND);
        }
        return switch (scheduleElement.getType()) {
            case RANGE_EVERY_YEAR -> parseRangeEveryYearExpression(scheduleElement);
            case DAY_IN_MONTH -> parseDayInMonthExpression(scheduleElement);
            case DAY_IN_WEEK -> parseDayInWeekExpression(scheduleElement);
            case DIFFERENCE -> parseDifferenceExpression(scheduleElement.getIncludedDate(), scheduleElement.getExcludedDate());
            case UNION -> UnionTE.of(parseExpressions(scheduleElement.getExpressions()));
            case INTERSECTION -> IntersectionTE.of(parseExpressions(scheduleElement.getExpressions()));
            default -> throw new DateTimeException(ErrorCodes.TEMPORAL_EXPRESSION_IS_NOT_DEFINED);
        };
    }

    /**
     * Parses a list of BasicScheduleJsonElement objects into a list of TemporalExpression objects.
     * This method iterates through the provided list of BasicScheduleJsonElements and parses each
     * one into a TemporalExpression using the parseExpression method. The resulting TemporalExpression
     * objects are added to a list and returned.
     *
     * @param scheduleElements The list of BasicScheduleJsonElement objects to be parsed.
     * @return A list of TemporalExpression objects representing the parsed schedule elements.
     * @throws DateTimeException if any of the provided elements cannot be parsed.
     */
    private static List<TemporalExpression> parseExpressions(List<BasicScheduleJsonElement> scheduleElements) {
        List<TemporalExpression> temporalExpressions = new ArrayList<>();
        for (BasicScheduleJsonElement scheduleElement : scheduleElements) {
            temporalExpressions.add(parseExpression(scheduleElement));
        }
        return temporalExpressions;
    }

    /**
     * Parses a difference expression from two BasicScheduleJsonElement objects.
     * This method creates a Difference expression by parsing two BasicScheduleJsonElement objects:
     * one representing the included dates and the other representing the excluded dates.
     *
     * @param includedDate The BasicScheduleJsonElement representing included dates.
     * @param excludedDate The BasicScheduleJsonElement representing excluded dates.
     * @return A Difference expression representing the difference between included and excluded dates.
     * @throws DateTimeException if any of the provided elements cannot be parsed.
     */
    private static TemporalExpression parseDifferenceExpression(
        BasicScheduleJsonElement includedDate,
        BasicScheduleJsonElement excludedDate
    ) {
        return DifferenceTE.of(parseExpression(includedDate), parseExpression(excludedDate));
    }

    /**
     * Parses a DayInWeek expression from a BasicScheduleJsonElement.
     * This method creates a DayInWeek expression by parsing a BasicScheduleJsonElement representing a day of the week.
     *
     * @param basicScheduleJsonElement The BasicScheduleJsonElement representing a day of the week.
     * @return A DayInWeek expression representing the specified day of the week.
     * @throws DateTimeException if the day is not defined.
     */
    private static TemporalExpression parseDayInWeekExpression(BasicScheduleJsonElement basicScheduleJsonElement) {
        if (basicScheduleJsonElement.getDay() == null) {
            throw new DateTimeException(ErrorCodes.DAY_PROPERTY_NOT_PROVIDED);
        }
        return DayInWeekTE.of(DayOfWeek.valueOf(basicScheduleJsonElement.getDay()));
    }

    /**
     * Parses a DayInMonth expression from a BasicScheduleJsonElement.
     * This method creates a DayInMonth expression by parsing a BasicScheduleJsonElement representing a day of the week
     * and an ordinal value specifying the day's position within the month.
     *
     * @param basicScheduleJsonElement The BasicScheduleJsonElement representing a day of the week and ordinal value.
     * @return A DayInMonth expression representing the specified day of the week and ordinal value.
     * @throws DateTimeException if the day or ordinal is not defined.
     */
    private static TemporalExpression parseDayInMonthExpression(BasicScheduleJsonElement basicScheduleJsonElement) {
        if (basicScheduleJsonElement.getDay() == null) {
            throw new DateTimeException(ErrorCodes.DAY_PROPERTY_NOT_PROVIDED);
        }
        return DayInMonthTE.of(DayOfWeek.valueOf(basicScheduleJsonElement.getDay()), basicScheduleJsonElement.getOrdinal());
    }

    /**
     * Parses a RangeEveryYear expression from a BasicScheduleJsonElement.
     * This method creates a RangeEveryYear expression by parsing a BasicScheduleJsonElement representing either a range
     * of days or a range of months within a year.
     *
     * @param basicScheduleJsonElement The BasicScheduleJsonElement representing the range information.
     * @return A RangeEveryYear expression representing the specified range within a year.
     * @throws DateTimeException if the range type is not defined or if the provided dates or months are invalid.
     */
    private static TemporalExpression parseRangeEveryYearExpression(BasicScheduleJsonElement basicScheduleJsonElement) {
        return switch (basicScheduleJsonElement.getOf()) {
            case START_DAY_TO_END_DAY -> RangeEveryYearTE.of(
                convertDayMonthStringToDate(basicScheduleJsonElement.getStartDate()),
                convertDayMonthStringToDate(basicScheduleJsonElement.getEndDate())
            );
            case START_MONTH_TO_END_MONTH -> RangeEveryYearTE.of(
                Month.valueOf(basicScheduleJsonElement.getStartMonth()),
                Month.valueOf(basicScheduleJsonElement.getEndMonth())
            );
            default -> throw new DateTimeException(ErrorCodes.TEMPORAL_EXPRESSION_IS_NOT_DEFINED);
        };
    }

    /**
     * Converts a date string in "month-day" format to a MonthDay object.
     * <p>
     * This method takes a date string in the "month-day" format, validates it, and converts it to a MonthDay object.
     *
     * @param dayMonthString The date string in "month-day" format (e.g., "12-25" for December 25th).
     * @return A MonthDay object representing the parsed date.
     * @throws DateTimeException if the date format is invalid or if the day or month is out of allowed range.
     */
    private static MonthDay convertDayMonthStringToDate(String dayMonthString) {
        String[] dateParts = dayMonthString.split("-");

        if (dateParts.length != 2) {
            throw new DateTimeException(ErrorCodes.INVALID_DATE_FORMAT);
        }

        int monthValue = Integer.parseInt(dateParts[0]);
        int dayOfMonth = Integer.parseInt(dateParts[1]);

        if (!(monthValue >= 1 && monthValue <= 12)) {
            throw new DateTimeException(ErrorCodes.MONTH_VALUE_OUT_OF_RANGE);
        }

        if (!(dayOfMonth >= 1 && dayOfMonth <= 31)) {
            throw new DateTimeException(ErrorCodes.DAY_OF_MONTH_OUT_OF_RANGE);
        }

        Month month = Month.of(monthValue);
        return MonthDay.of(month, dayOfMonth);
    }
}
