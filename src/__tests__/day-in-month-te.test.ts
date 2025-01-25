import { DayInMonthTE } from "../models/expressions/day-in-month-te.model";

describe("DayInMonthTE", () => {
  describe("constructor validation", () => {
    it("should throw error for invalid ordinal", () => {
      expect(() => DayInMonthTE.of(1, 0)).toThrow();
      expect(() => DayInMonthTE.of(1, 6)).toThrow();
      expect(() => DayInMonthTE.of(1, -6)).toThrow();
    });
  });

  describe("includes", () => {
    it("should identify first Monday of month", () => {
      const firstMonday = DayInMonthTE.of(1, 1); // Monday = 1
      expect(firstMonday.includes(new Date("2025-01-06"))).toBe(true);
      expect(firstMonday.includes(new Date("2025-01-13"))).toBe(false);
    });

    it("should identify last Friday of month", () => {
      const lastFriday = DayInMonthTE.of(5, -1); // Friday = 5
      expect(lastFriday.includes(new Date("2025-01-31"))).toBe(true);
      expect(lastFriday.includes(new Date("2025-01-24"))).toBe(false);
    });
  });
});
