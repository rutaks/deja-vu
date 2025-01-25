# Déjà vu
A flexible TypeScript library for handling complex scheduling patterns and temporal rules.

## Features

- Date range scheduling
- Recurring events (weekly, monthly)
- Complex schedule combinations
- Slot allocation
- Forward/backward date iteration

## Installation

```bash
npm install deja-vu
```

## Quick Start

```typescript
import { ScheduleJsonParser, TemporalExpressionType } from 'deja-vu';

const config = {
  schedule: [{
    type: TemporalExpressionType.DAY_IN_WEEK,
    day: "1", // Monday
    slots: 30
  }]
};

const scheduleElements = ScheduleJsonParser.parse(config);
const isAvailable = scheduleElements[0].isOccurring(new Date());
```

## Usage Guide

### Schedule Query Methods

#### Available Methods

| Method | Usage | Example |
|--------|-------|---------|
| `isOccurring(date)` | Check if schedule is active on date | `schedule.isOccurring(new Date())` |
| `slots(date)` | Get available slots for date | `schedule.slots(new Date())` |
| `datesInRange(start, end)` | Get all active dates in range | `schedule.datesInRange(new Date(), endDate)` |

#### Date Navigation

```typescript
// Find next available date
const nextDate = schedule.nextOccurrence(new Date());

// Get all dates between Jan-Mar 2025
const dates = schedule.datesInRange(
  new Date('2025-01-01'),
  new Date('2025-03-31')
);

// Generate future dates
for (const date of schedule.futureDates(new Date())) {
  if (someCondition) break;
  // Process date
}

// Check specific date
const isActive = schedule.isOccurring(new Date('2025-06-01'));
const availableSlots = schedule.slots(new Date('2025-06-01'));
```
### Configuration Properties

#### Day in Week Expression
Used for weekly recurring events.

| Property | Type | Required | Description | Example |
|----------|------|----------|-------------|---------|
| type | string | Yes | Must be `DAY_IN_WEEK` | `TemporalExpressionType.DAY_IN_WEEK` |
| day | string | Yes | Day of week (0-6, where 0 is Sunday) | `"1"` for Monday |
| slots | number | Yes | Available slots for this schedule | `30` |

```typescript
{
  type: TemporalExpressionType.DAY_IN_WEEK,
  day: "1",  // Monday
  slots: 30
}
```

#### Day in Month Expression
Used for monthly recurring events.

| Property | Type | Required | Description | Example |
|----------|------|----------|-------------|---------|
| type | string | Yes | Must be `DAY_IN_MONTH` | `TemporalExpressionType.DAY_IN_MONTH` |
| day | string | Yes | Day of week (0-6) | `"1"` for Monday |
| ordinal | number | Yes | Week of month (-5 to 5, excluding 0) | `1` for first occurrence |
| slots | number | Yes | Available slots | `5` |

```typescript
{
  type: TemporalExpressionType.DAY_IN_MONTH,
  day: "1",     // Monday
  ordinal: 1,   // First Monday
  slots: 5
}
```

#### Range Every Year Expression
Used for yearly recurring date ranges.

| Property | Type | Required | Description | Example |
|----------|------|----------|-------------|---------|
| type | string | Yes | Must be `RANGE_EVERY_YEAR` | `TemporalExpressionType.RANGE_EVERY_YEAR` |
| of | string | Yes | Range type | `RangeEveryYearExpression.START_DAY_TO_END_DAY` |
| startDate | string | Yes | Start date (MM-DD) | `"6-1"` for June 1st |
| endDate | string | Yes | End date (MM-DD) | `"8-31"` for August 31st |
| slots | number | Yes | Available slots | `20` |

```typescript
{
  type: TemporalExpressionType.RANGE_EVERY_YEAR,
  of: RangeEveryYearExpression.START_DAY_TO_END_DAY,
  startDate: "6-1",
  endDate: "8-31",
  slots: 20
}
```

#### Difference Expression
Used to exclude specific dates from a schedule.

| Property | Type | Required | Description | Example |
|----------|------|----------|-------------|---------|
| type | string | Yes | Must be `DIFFERENCE` | `TemporalExpressionType.DIFFERENCE` |
| includedDate | object | Yes | Base schedule | See example |
| excludedDate | object | Yes | Schedule to exclude | See example |
| slots | number | Yes | Available slots | `15` |

```typescript
{
  type: TemporalExpressionType.DIFFERENCE,
  includedDate: {
    type: TemporalExpressionType.DAY_IN_WEEK,
    day: "1"
  },
  excludedDate: {
    type: TemporalExpressionType.DAY_IN_MONTH,
    day: "1",
    ordinal: 1
  },
  slots: 15
}
```

#### Union Expression
Used to combine multiple schedules (OR condition).

| Property | Type | Required | Description | Example |
|----------|------|----------|-------------|---------|
| type | string | Yes | Must be `UNION` | `TemporalExpressionType.UNION` |
| expressions | array | Yes | Array of schedule expressions | See example |
| slots | number | Yes | Available slots | `10` |

```typescript
{
  type: TemporalExpressionType.UNION,
  expressions: [
    { type: TemporalExpressionType.DAY_IN_WEEK, day: "1" },
    { type: TemporalExpressionType.DAY_IN_WEEK, day: "3" }
  ],
  slots: 10
}
```

#### Intersection Expression
Used to find overlapping schedules (AND condition).

| Property | Type | Required | Description | Example |
|----------|------|----------|-------------|---------|
| type | string | Yes | Must be `INTERSECTION` | `TemporalExpressionType.INTERSECTION` |
| expressions | array | Yes | Array of schedule expressions | See example |
| slots | number | Yes | Available slots | `25` |

```typescript
{
  type: TemporalExpressionType.INTERSECTION,
  expressions: [
    {
      type: TemporalExpressionType.RANGE_EVERY_YEAR,
      of: RangeEveryYearExpression.START_DAY_TO_END_DAY,
      startDate: "6-1",
      endDate: "8-31"
    },
    {
      type: TemporalExpressionType.DAY_IN_WEEK,
      day: "1"
    }
  ],
  slots: 25
}
```

### Schedule Methods

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| isOccurring | date: Date | boolean | Checks if schedule occurs on given date |
| slots | date: Date | number | Gets available slots for given date |
| datesInRange | start: Date, end: Date | Date[] | Gets all scheduled dates in range |
| nextOccurrence | date: Date | Date | Gets next scheduled date after given date |
| previousOccurrence | date: Date | Date | Gets previous scheduled date before given date |
| futureDates | start: Date | Generator<Date> | Generates future scheduled dates |
| pastDates | start: Date | Generator<Date> | Generates past scheduled dates |

### Complete Example

```typescript
import { ScheduleJsonParser, TemporalExpressionType, RangeEveryYearExpression } from 'deja-vu';

const config = {
  schedule: [
    {
      // Weekday summer classes
      type: TemporalExpressionType.INTERSECTION,
      expressions: [
        {
          type: TemporalExpressionType.RANGE_EVERY_YEAR,
          of: RangeEveryYearExpression.START_DAY_TO_END_DAY,
          startDate: "6-1",
          endDate: "8-31"
        },
        {
          type: TemporalExpressionType.DAY_IN_WEEK,
          day: "1"  // Monday only
        }
      ],
      slots: 30
    }
  ]
};

const scheduleElements = ScheduleJsonParser.parse(config);
const isAvailable = scheduleElements[0].isOccurring(new Date('2025-06-02')); // true
const availableSlots = scheduleElements[0].slots(); // 30
```
