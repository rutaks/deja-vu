import { RangeEveryYearExpression, TemporalExpressionType } from "../enums";

export interface BasicScheduleJsonElement {
  type: TemporalExpressionType;
  of?: RangeEveryYearExpression;
  startDate?: string;
  endDate?: string;
  day?: string;
  ordinal?: number;
  startMonth?: string;
  endMonth?: string;
  includedDate?: BasicScheduleJsonElement;
  excludedDate?: BasicScheduleJsonElement;
  expressions?: BasicScheduleJsonElement[];
}
