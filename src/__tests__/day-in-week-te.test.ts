import { DayInWeekTE } from "../models/expressions/day-in-week-te.model";

describe("DayInWeekTE", () => {
  describe("constructor validation", () => {
    it("should throw error for invalid day", () => {
      expect(() => DayInWeekTE.of(-1)).toThrow();
      expect(() => DayInWeekTE.of(7)).toThrow();
    });

    it("should throw error for invalid ordinal", () => {
      expect(() => DayInWeekTE.ofOrdinal(0, new Date())).toThrow();
    });
  });

  describe("includes", () => {
    it("should identify specific day of week", () => {
      const monday = DayInWeekTE.of(1);
      expect(monday.includes(new Date("2025-01-06"))).toBe(true);
      expect(monday.includes(new Date("2025-01-07"))).toBe(false);
    });

    it("should identify every nth week from reference", () => {
      const biweekly = DayInWeekTE.ofOrdinal(2, new Date("2025-01-06"));
      expect(biweekly.includes(new Date("2025-01-20"))).toBe(true);
      expect(biweekly.includes(new Date("2025-01-13"))).toBe(false);
    });
  });
});
