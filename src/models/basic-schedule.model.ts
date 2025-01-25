import { Schedule, ScheduleElement } from "../interfaces";

export class BasicSchedule implements Schedule {
  constructor(private readonly elements: ScheduleElement[]) {
    if (!elements) {
      throw new Error("Elements cannot be null");
    }
  }

  isOccurring(date: Date): boolean {
    return this.elements.some((e) => e.isOccurring(date));
  }

  slots(date: Date): number {
    const element = this.elements.find((e) => e.isOccurring(date));
    return element ? element.slots() : 0;
  }

  datesInRange(start: Date, end: Date): Date[] {
    const result: Date[] = [];
    const cursor = new Date(start);

    while (cursor <= end) {
      if (this.isOccurring(cursor)) {
        result.push(new Date(cursor));
      }
      cursor.setDate(cursor.getDate() + 1);
    }
    return result;
  }

  *futureDates(start: Date): Generator<Date> {
    let cursor = this.nextOccurrence(start);
    while (true) {
      yield cursor;
      cursor = this.nextOccurrence(
        new Date(cursor.setDate(cursor.getDate() + 1))
      );
    }
  }

  *pastDates(start: Date): Generator<Date> {
    let cursor = this.previousOccurrence(start);
    while (true) {
      yield cursor;
      cursor = this.previousOccurrence(
        new Date(cursor.setDate(cursor.getDate() - 1))
      );
    }
  }

  nextOccurrence(date: Date): Date {
    const cursor = new Date(date);
    while (!this.isOccurring(cursor)) {
      cursor.setDate(cursor.getDate() + 1);
    }
    return cursor;
  }

  previousOccurrence(date: Date): Date {
    const cursor = new Date(date);
    while (!this.isOccurring(cursor)) {
      cursor.setDate(cursor.getDate() - 1);
    }
    return cursor;
  }

  static of(elements: ScheduleElement[]): Schedule {
    return new BasicSchedule(elements);
  }
}
