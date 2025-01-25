import { ScheduleJsonParser } from "../utils/schedule-json-parser.util";
import { TemporalExpressionType, RangeEveryYearExpression } from "../enums";
import { ScheduleConfig } from "../interfaces";
import { ErrorCodes } from "../constants/error-codes.constants";

describe("ScheduleJsonParser", () => {
  describe("parse basic expressions", () => {
    it("should handle RANGE_EVERY_YEAR with START_DAY_TO_END_DAY", () => {
      const config: ScheduleConfig = {
        schedule: [
          {
            type: TemporalExpressionType.RANGE_EVERY_YEAR,
            of: RangeEveryYearExpression.START_DAY_TO_END_DAY,
            startDate: "6-1",
            endDate: "8-31",
            slots: 30,
          },
        ],
      };

      const elements = ScheduleJsonParser.parse(config);
      expect(elements[0].isOccurring(new Date("2025-07-15"))).toBe(true);
      expect(elements[0].isOccurring(new Date("2025-09-15"))).toBe(false);
    });

    it("should handle RANGE_EVERY_YEAR with START_MONTH_TO_END_MONTH", () => {
      const config: ScheduleConfig = {
        schedule: [
          {
            type: TemporalExpressionType.RANGE_EVERY_YEAR,
            of: RangeEveryYearExpression.START_MONTH_TO_END_MONTH,
            startMonth: "6",
            endMonth: "8",
            slots: 30,
          },
        ],
      };

      const elements = ScheduleJsonParser.parse(config);
      expect(elements[0].isOccurring(new Date("2025-07-15"))).toBe(true);
    });

    it("should handle DAY_IN_MONTH", () => {
      const config: ScheduleConfig = {
        schedule: [
          {
            type: TemporalExpressionType.DAY_IN_MONTH,
            day: "1",
            ordinal: 1,
            slots: 1,
          },
        ],
      };

      const elements = ScheduleJsonParser.parse(config);
      expect(elements[0].isOccurring(new Date("2025-01-06"))).toBe(true);
      expect(elements[0].isOccurring(new Date("2025-01-13"))).toBe(false);
    });
  });

  describe("parse composite expressions", () => {
    it("should handle INTERSECTION", () => {
      const config: ScheduleConfig = {
        schedule: [
          {
            type: TemporalExpressionType.INTERSECTION,
            expressions: [
              {
                type: TemporalExpressionType.DAY_IN_WEEK,
                day: "1",
              },
              {
                type: TemporalExpressionType.RANGE_EVERY_YEAR,
                of: RangeEveryYearExpression.START_DAY_TO_END_DAY,
                startDate: "6-1",
                endDate: "8-31",
              },
            ],
            slots: 1,
          },
        ],
      };

      const elements = ScheduleJsonParser.parse(config);
      const summerMonday = new Date("2025-06-02");
      const winterMonday = new Date("2025-01-06");

      expect(elements[0].isOccurring(summerMonday)).toBe(true);
      expect(elements[0].isOccurring(winterMonday)).toBe(false);
    });

    it("should handle DIFFERENCE", () => {
      const config: ScheduleConfig = {
        schedule: [
          {
            type: TemporalExpressionType.DIFFERENCE,
            includedDate: {
              type: TemporalExpressionType.DAY_IN_WEEK,
              day: "1",
            },
            excludedDate: {
              type: TemporalExpressionType.DAY_IN_MONTH,
              day: "1",
              ordinal: 1,
            },
            slots: 1,
          },
        ],
      };

      const elements = ScheduleJsonParser.parse(config);
      expect(elements[0].isOccurring(new Date("2025-01-06"))).toBe(false);
      expect(elements[0].isOccurring(new Date("2025-01-13"))).toBe(true);
    });
  });

  describe("error handling", () => {
    it("should throw for missing schedule", () => {
      expect(() => ScheduleJsonParser.parse({} as ScheduleConfig)).toThrow(
        ErrorCodes.SCHEDULE_NOT_DEFINED
      );
    });
  });
});
