import { TemporalExpression } from "../../interfaces";

export class DayInWeekTE implements TemporalExpression {
  private constructor(
    private readonly dayOfWeek: number | null,
    private readonly ordinal: number,
    private readonly referenceDate: Date | null
  ) {
    // Validate the combination of parameters
    const isValid =
      (dayOfWeek !== null && ordinal === 0 && referenceDate === null) ||
      (referenceDate !== null && ordinal > 0 && dayOfWeek === null);

    if (!isValid) {
      throw new Error(
        "Must have either dayOfWeek set (and ordinal == 0, referenceDate is null), " +
          "or referenceDate set, with ordinal > 0 (and dayOfWeek is null)."
      );
    }
  }

  static of(dayOfWeek: number): DayInWeekTE {
    if (dayOfWeek < 0 || dayOfWeek > 6) {
      throw new Error("Day of week must be between 0 and 6");
    }
    return new DayInWeekTE(dayOfWeek, 0, null);
  }

  static ofOrdinal(ordinal: number, referenceDate: Date): DayInWeekTE {
    if (ordinal < 1) {
      throw new Error("'ordinal' must be >= 1");
    }
    return new DayInWeekTE(null, ordinal, referenceDate);
  }

  includes(date: Date): boolean {
    if (this.dayOfWeek === null && this.referenceDate !== null) {
      return (
        this.referenceDate.getDay() === date.getDay() &&
        this.weeksBetween(this.referenceDate, date) % this.ordinal === 0
      );
    } else if (this.dayOfWeek !== null) {
      return date.getDay() === this.dayOfWeek;
    }
    return false;
  }

  private weeksBetween(d1: Date, d2: Date): number {
    const millisecondsPerWeek = 7 * 24 * 60 * 60 * 1000;
    const diffTime = Math.abs(d2.getTime() - d1.getTime());
    return Math.floor(diffTime / millisecondsPerWeek);
  }
}
