import { ScheduleJsonParser } from "../utils/schedule-json-parser.util";
import { TemporalExpressionType, RangeEveryYearExpression } from "../enums";
import { ScheduleConfig } from "../interfaces";

describe("ScheduleJsonParser", () => {
  const validConfig: ScheduleConfig = {
    schedule: [
      {
        type: TemporalExpressionType.RANGE_EVERY_YEAR,
        of: RangeEveryYearExpression.START_DAY_TO_END_DAY,
        startDate: "6-1",
        endDate: "8-31",
        slots: 30,
      },
      {
        type: TemporalExpressionType.DAY_IN_MONTH,
        day: "1",
        ordinal: 1,
        slots: 1,
      },
    ],
  };

  describe("parse", () => {
    it("should parse valid config successfully", () => {
      const elements = ScheduleJsonParser.parse(validConfig);
      expect(elements).toHaveLength(2);

      const summerDate = new Date("2025-07-15");
      expect(elements[0].isOccurring(summerDate)).toBe(true);
      expect(elements[0].slots()).toBe(30);
    });

    it("should throw error for missing schedule", () => {
      expect(() => ScheduleJsonParser.parse({} as ScheduleConfig)).toThrow();
    });

    it("should handle complex nested expressions", () => {
      const complexConfig: ScheduleConfig = {
        schedule: [
          {
            type: TemporalExpressionType.INTERSECTION,
            expressions: [
              {
                type: TemporalExpressionType.RANGE_EVERY_YEAR,
                of: RangeEveryYearExpression.START_DAY_TO_END_DAY,
                startDate: "6-1",
                endDate: "8-31",
              },
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
              },
            ],
            slots: 1,
          },
        ],
      };

      const elements = ScheduleJsonParser.parse(complexConfig);
      expect(elements).toHaveLength(1);
    });
  });
});
