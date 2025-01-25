import { BasicSchedule } from "../models/basic-schedule.model";
import { ScheduleElement } from "../interfaces";

describe("BasicSchedule", () => {
  const mockElements: ScheduleElement[] = [
    {
      isOccurring: (date: Date) => date.getDay() === 1, // Mondays
      slots: () => 2,
    },
    {
      isOccurring: (date: Date) => date.getDay() === 3, // Wednesdays
      slots: () => 3,
    },
  ];

  const schedule = new BasicSchedule(mockElements);

  describe("isOccurring", () => {
    it("should return true for matching dates", () => {
      expect(schedule.isOccurring(new Date("2025-01-06"))).toBe(true); // Monday
      expect(schedule.isOccurring(new Date("2025-01-08"))).toBe(true); // Wednesday
    });

    it("should return false for non-matching dates", () => {
      expect(schedule.isOccurring(new Date("2025-01-07"))).toBe(false); // Tuesday
    });
  });

  describe("slots", () => {
    it("should return correct slots for matching dates", () => {
      expect(schedule.slots(new Date("2025-01-06"))).toBe(2); // Monday
      expect(schedule.slots(new Date("2025-01-08"))).toBe(3); // Wednesday
    });

    it("should return 0 for non-matching dates", () => {
      expect(schedule.slots(new Date("2025-01-07"))).toBe(0); // Tuesday
    });
  });

  describe("datesInRange", () => {
    it("should return all matching dates within range", () => {
      const start = new Date("2025-01-06"); // Monday
      const end = new Date("2025-01-10"); // Friday
      const dates = schedule.datesInRange(start, end);

      expect(dates).toHaveLength(2);
      expect(dates[0].getDay()).toBe(1); // Monday
      expect(dates[1].getDay()).toBe(3); // Wednesday
    });
  });

  describe("nextOccurrence", () => {
    it("should find next matching date", () => {
      const start = new Date("2025-01-07"); // Tuesday
      const next = schedule.nextOccurrence(start);

      expect(next.getDay()).toBe(3); // Wednesday
    });
  });
});
