import { TemporalExpression } from "../../interfaces";

export class DayInMonthTE implements TemporalExpression {
  private static readonly MAX_WEEKS_IN_MONTH = 5;
  private static readonly DAYS_IN_WEEK = 7;

  private constructor(
    private readonly day: number,
    private readonly ordinal: number
  ) {
    if (
      ordinal === 0 ||
      ordinal < -DayInMonthTE.MAX_WEEKS_IN_MONTH ||
      ordinal > DayInMonthTE.MAX_WEEKS_IN_MONTH
    ) {
      throw new Error(`ordinal=${ordinal} is not in [-5, 5] excluding 0.`);
    }
  }

  static of(day: number, ordinal: number): DayInMonthTE {
    return new DayInMonthTE(day, ordinal);
  }

  includes(date: Date): boolean {
    return this.dayMatches(date) && this.weekMatches(date);
  }

  private dayMatches(date: Date): boolean {
    return date.getDay() === this.day;
  }

  private weekMatches(date: Date): boolean {
    return this.ordinal > 0
      ? this.weekFromStartMatches(date)
      : this.weekFromEndMatches(date);
  }

  private weekFromStartMatches(date: Date): boolean {
    return this.weekInMonth(date.getDate()) === this.ordinal;
  }

  private weekFromEndMatches(date: Date): boolean {
    return (
      this.weekInMonth(this.daysLeftInMonth(date) + 1) ===
      Math.abs(this.ordinal)
    );
  }

  private weekInMonth(dayOfMonth: number): number {
    return Math.floor((dayOfMonth - 1) / DayInMonthTE.DAYS_IN_WEEK) + 1;
  }

  private daysLeftInMonth(date: Date): number {
    const lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
    return lastDay.getDate() - date.getDate();
  }
}
