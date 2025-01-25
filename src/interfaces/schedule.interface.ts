export interface Schedule {
  isOccurring(date: Date): boolean;
  slots(date: Date): number;
  datesInRange(start: Date, end: Date): Date[];
  futureDates(start: Date): Generator<Date>;
  pastDates(start: Date): Generator<Date>;
  nextOccurrence(date: Date): Date;
  previousOccurrence(date: Date): Date;
}
