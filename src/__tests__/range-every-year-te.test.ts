import { RangeEveryYearTE } from "../models/expressions/range-every-year-te.model";

describe("RangeEveryYearTE", () => {
  describe("constructor validation", () => {
    it("should throw error for invalid months", () => {
      expect(() => RangeEveryYearTE.ofMonths(0, 12)).toThrow();
      expect(() => RangeEveryYearTE.ofMonths(1, 13)).toThrow();
    });

    it("should throw error for invalid days", () => {
      expect(() =>
        RangeEveryYearTE.of({ month: 1, day: -1 }, { month: 1, day: 1 })
      ).toThrow();
      expect(() =>
        RangeEveryYearTE.of({ month: 1, day: 1 }, { month: 1, day: 32 })
      ).toThrow();
    });
  });

  describe("includes", () => {
    it("should handle single month ranges", () => {
      const january = RangeEveryYearTE.ofMonth(1);
      expect(january.includes(new Date("2025-01-15"))).toBe(true);
      expect(january.includes(new Date("2025-02-15"))).toBe(false);
    });

    it("should handle month ranges", () => {
      const summerMonths = RangeEveryYearTE.ofMonths(6, 8);
      expect(summerMonths.includes(new Date("2025-07-15"))).toBe(true);
      expect(summerMonths.includes(new Date("2025-09-15"))).toBe(false);
    });

    it("should handle day-specific ranges", () => {
      const summerBreak = RangeEveryYearTE.of(
        { month: 6, day: 15 },
        { month: 8, day: 15 }
      );
      expect(summerBreak.includes(new Date("2025-07-15"))).toBe(true);
      expect(summerBreak.includes(new Date("2025-06-14"))).toBe(false);
      expect(summerBreak.includes(new Date("2025-08-16"))).toBe(false);
    });
  });
});
