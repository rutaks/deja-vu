export interface ScheduleElement {
  isOccurring(date: Date): boolean;
  slots(): number;
}
