import { TemporalExpression } from "../../interfaces";

export class RangeEveryYearTE implements TemporalExpression {
  private constructor(
    private readonly startMonth: number,
    private readonly endMonth: number,
    private readonly startDay: number = 0,
    private readonly endDay: number = 0
  ) {
    if (startMonth < 1 || startMonth > 12 || endMonth < 1 || endMonth > 12) {
      throw new Error("Month values must be between 1 and 12");
    }
    if (startDay < 0 || startDay > 31 || endDay < 0 || endDay > 31) {
      throw new Error("Day values must be between 0 and 31");
    }
  }

  static of(
    start: { month: number; day: number },
    end: { month: number; day: number }
  ): RangeEveryYearTE {
    return new RangeEveryYearTE(start.month, end.month, start.day, end.day);
  }

  static ofMonths(startMonth: number, endMonth: number): RangeEveryYearTE {
    return new RangeEveryYearTE(startMonth, endMonth);
  }

  static ofMonth(month: number): RangeEveryYearTE {
    return new RangeEveryYearTE(month, month);
  }

  includes(date: Date): boolean {
    const month = date.getMonth() + 1; // JavaScript months are 0-based

    if (this.startMonth === this.endMonth) {
      return this.startMonthIncludes(date) && this.endMonthIncludes(date);
    }
    return (
      this.monthsInclude(month) ||
      this.startMonthIncludes(date) ||
      this.endMonthIncludes(date)
    );
  }

  private monthsInclude(month: number): boolean {
    return month > this.startMonth && month < this.endMonth;
  }

  private startMonthIncludes(date: Date): boolean {
    const month = date.getMonth() + 1;
    if (month !== this.startMonth) return false;
    if (this.startDay === 0) return true;
    return date.getDate() >= this.startDay;
  }

  private endMonthIncludes(date: Date): boolean {
    const month = date.getMonth() + 1;
    if (month !== this.endMonth) return false;
    if (this.endDay === 0) return true;
    return date.getDate() <= this.endDay;
  }
}
