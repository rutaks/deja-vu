import { TemporalExpressionType, RangeEveryYearExpression } from "./enums";
import { ScheduleJsonParser } from "./utils";

// Sample config
const testConfig = {
  schedule: [
    {
      // Summer school semester
      type: TemporalExpressionType.INTERSECTION,
      expressions: [
        {
          type: TemporalExpressionType.RANGE_EVERY_YEAR,
          of: RangeEveryYearExpression.START_DAY_TO_END_DAY,
          startDate: "6-1",
          endDate: "8-31",
        },
        {
          // Weekdays only
          type: TemporalExpressionType.DIFFERENCE,
          includedDate: {
            type: TemporalExpressionType.DAY_IN_WEEK,
            day: "1", // Monday-Friday
          },
          excludedDate: {
            type: TemporalExpressionType.UNION,
            expressions: [
              { type: TemporalExpressionType.DAY_IN_WEEK, day: "0" }, // Sunday
              { type: TemporalExpressionType.DAY_IN_WEEK, day: "6" }, // Saturday
            ],
          },
        },
      ],
      slots: 30,
    },
    {
      // Monthly faculty meetings - first Monday
      type: TemporalExpressionType.DAY_IN_MONTH,
      day: "1", // Monday
      ordinal: 1,
      slots: 1,
    },
    {
      // Winter break
      type: TemporalExpressionType.RANGE_EVERY_YEAR,
      of: RangeEveryYearExpression.START_DAY_TO_END_DAY,
      startDate: "12-20",
      endDate: "1-5",
      slots: 0,
    },
    {
      // Spring break
      type: TemporalExpressionType.RANGE_EVERY_YEAR,
      of: RangeEveryYearExpression.START_DAY_TO_END_DAY,
      startDate: "3-15",
      endDate: "3-21",
      slots: 0,
    },
  ],
};
// Test dates
const testDates = [
  new Date("2025-06-15"), // Should be true for summer range
  new Date("2025-01-15"), // Should be false for summer range
  new Date("2025-01-01"), // Monday - should be true for day in week
  new Date("2025-01-02"), // Tuesday - should be false for day in week
];

// Parse schedule
const scheduleElements = ScheduleJsonParser.parse(testConfig);
console.log(`>> Schedule 1: Jun 1st - Aug 31st, 5 slots`);
console.log(`>> Schedule 2: Every Monday, 3 slots`);
// Test each date
testDates.forEach((date) => {
  console.log(`\nTesting date: ${date.toDateString()}`);

  scheduleElements.forEach((element, index) => {
    const isOccurring = element.isOccurring(date);
    const slots = element.slots();
    console.log(
      `Schedule ${index + 1}: | Occurring: ${isOccurring} | Slots: ${slots}`
    );
  });
});
