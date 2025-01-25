import { ErrorCodes } from "../constants/error-codes.constants";
import { RangeEveryYearExpression, TemporalExpressionType } from "../enums";
import {
  BasicScheduleJsonElement,
  ScheduleConfig,
  ScheduleElement,
  TemporalExpression,
} from "../interfaces";
import {
  DayInMonthTE,
  DayInWeekTE,
  DifferenceTE,
  IntersectionTE,
  RangeEveryYearTE,
  UnionTE,
} from "../models";

export class ScheduleJsonParser {
  static parse(scheduleConfig: ScheduleConfig): ScheduleElement[] {
    if (!scheduleConfig.schedule) {
      throw new Error(ErrorCodes.SCHEDULE_NOT_DEFINED);
    }

    return scheduleConfig.schedule.map((element) => ({
      isOccurring: (date: Date) => this.parseExpression(element).includes(date),
      slots: () => element.slots,
    }));
  }

  private static parseExpression(
    element: BasicScheduleJsonElement
  ): TemporalExpression {
    if (!element) {
      throw new Error(ErrorCodes.SCHEDULE_NOT_FOUND);
    }

    switch (element.type) {
      case TemporalExpressionType.RANGE_EVERY_YEAR:
        return this.parseRangeEveryYearExpression(element);
      case TemporalExpressionType.DAY_IN_MONTH:
        return this.parseDayInMonthExpression(element);
      case TemporalExpressionType.DAY_IN_WEEK:
        return this.parseDayInWeekExpression(element);
      case TemporalExpressionType.DIFFERENCE:
        if (!element.includedDate || !element.excludedDate) {
          throw new Error(
            "Included and excluded dates are required for DIFFERENCE type"
          );
        }
        return this.parseDifferenceExpression(
          element.includedDate,
          element.excludedDate
        );
      case TemporalExpressionType.UNION:
        if (!element.expressions) {
          throw new Error("Expressions are required for UNION type");
        }
        return UnionTE.of(this.parseExpressions(element.expressions));
      case TemporalExpressionType.INTERSECTION:
        if (!element.expressions) {
          throw new Error("Expressions are required for INTERSECTION type");
        }
        return IntersectionTE.of(this.parseExpressions(element.expressions));
      default:
        throw new Error(ErrorCodes.TEMPORAL_EXPRESSION_IS_NOT_DEFINED);
    }
  }

  private static parseExpressions(
    elements: BasicScheduleJsonElement[]
  ): TemporalExpression[] {
    return elements.map((element) => this.parseExpression(element));
  }

  private static parseDifferenceExpression(
    includedDate: BasicScheduleJsonElement,
    excludedDate: BasicScheduleJsonElement
  ): TemporalExpression {
    return DifferenceTE.of(
      this.parseExpression(includedDate),
      this.parseExpression(excludedDate)
    );
  }

  private static parseDayInWeekExpression(
    element: BasicScheduleJsonElement
  ): TemporalExpression {
    if (!element.day) {
      throw new Error(ErrorCodes.DAY_PROPERTY_NOT_PROVIDED);
    }
    return DayInWeekTE.of(parseInt(element.day, 10));
  }

  private static parseDayInMonthExpression(
    element: BasicScheduleJsonElement
  ): TemporalExpression {
    if (!element.day) {
      throw new Error(ErrorCodes.DAY_PROPERTY_NOT_PROVIDED);
    }
    if (typeof element.ordinal !== "number") {
      throw new Error("Ordinal is required for DAY_IN_MONTH type");
    }
    return DayInMonthTE.of(parseInt(element.day, 10), element.ordinal);
  }

  private static parseRangeEveryYearExpression(
    element: BasicScheduleJsonElement
  ): TemporalExpression {
    if (!element.of) {
      throw new Error('RangeEveryYear type must specify "of" property');
    }

    switch (element.of) {
      case RangeEveryYearExpression.START_DAY_TO_END_DAY:
        if (!element.startDate || !element.endDate) {
          throw new Error(
            "Start and end dates are required for START_DAY_TO_END_DAY type"
          );
        }
        return RangeEveryYearTE.of(
          this.convertDayMonthStringToDate(element.startDate),
          this.convertDayMonthStringToDate(element.endDate)
        );
      case RangeEveryYearExpression.START_MONTH_TO_END_MONTH:
        if (!element.startMonth || !element.endMonth) {
          throw new Error(
            "Start and end months are required for START_MONTH_TO_END_MONTH type"
          );
        }
        return RangeEveryYearTE.ofMonths(
          parseInt(element.startMonth, 10),
          parseInt(element.endMonth, 10)
        );
      default:
        throw new Error("Invalid RangeEveryYear expression type");
    }
  }

  private static convertDayMonthStringToDate(dayMonthString: string): {
    month: number;
    day: number;
  } {
    const dateParts = dayMonthString.split("-");

    if (dateParts.length !== 2) {
      throw new Error(ErrorCodes.INVALID_DATE_FORMAT);
    }

    const monthValue = parseInt(dateParts[0], 10);
    const dayOfMonth = parseInt(dateParts[1], 10);

    if (monthValue < 1 || monthValue > 12) {
      throw new Error(ErrorCodes.MONTH_VALUE_OUT_OF_RANGE);
    }

    if (dayOfMonth < 1 || dayOfMonth > 31) {
      throw new Error(ErrorCodes.DAY_OF_MONTH_OUT_OF_RANGE);
    }

    return { month: monthValue, day: dayOfMonth };
  }
}
