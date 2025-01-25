import {
  DayInWeekTE,
  UnionTE,
  IntersectionTE,
  DifferenceTE,
  DayInMonthTE,
} from "../models/expressions";

describe("Composite Temporal Expressions", () => {
  const monday = DayInWeekTE.of(1);
  const friday = DayInWeekTE.of(5);

  describe("UnionTE", () => {
    it("should handle multiple expressions", () => {
      const weekendDays = UnionTE.of([monday, friday]);
      expect(weekendDays.includes(new Date("2025-01-06"))).toBe(true);
      expect(weekendDays.includes(new Date("2025-01-10"))).toBe(true);
      expect(weekendDays.includes(new Date("2025-01-07"))).toBe(false);
    });

    it("should throw error for empty expressions", () => {
      expect(() => UnionTE.of([])).toThrow();
    });

    it("should throw error for null expressions", () => {
      expect(() => UnionTE.of([null as any])).toThrow();
    });
  });

  describe("IntersectionTE", () => {
    const workingMondays = IntersectionTE.of([
      monday,
      DayInWeekTE.ofOrdinal(2, new Date("2025-01-06")),
    ]);

    it("should require all expressions to match", () => {
      expect(workingMondays.includes(new Date("2025-01-06"))).toBe(true);
      expect(workingMondays.includes(new Date("2025-01-20"))).toBe(true);
      expect(workingMondays.includes(new Date("2025-01-13"))).toBe(false);
    });
  });

  describe("DifferenceTE", () => {
    const nonHolidayMondays = DifferenceTE.of(
      monday,
      DayInMonthTE.of(1, 1) // First Monday
    );

    it("should exclude specified dates", () => {
      expect(nonHolidayMondays.includes(new Date("2025-01-06"))).toBe(false);
      expect(nonHolidayMondays.includes(new Date("2025-01-13"))).toBe(true);
    });
  });
});
