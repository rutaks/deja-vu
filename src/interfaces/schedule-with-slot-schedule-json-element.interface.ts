import { BasicScheduleJsonElement } from "./basic-schedule-json-element.interface";

export interface ScheduleWithSlotScheduleJsonElement
  extends BasicScheduleJsonElement {
  slots: number;
}
