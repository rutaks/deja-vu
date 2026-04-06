# Déjà vu

Compose complex recurring schedules from simple building blocks using set operations (union, intersection, difference).

## Why Déjà vu?

Scheduling rules often start simple ("every Monday") but quickly grow complex: "every weekday during summer, except the first Monday of each month." Cron expressions can't express these compositions. Calendar libraries focus on single events, not composable patterns.

Déjà vu models schedules as **sets of dates**. You define simple temporal expressions, then combine them with set operations to express rules that would otherwise require custom logic:

```
INTERSECTION(
  DAY_IN_WEEK(Monday),
  RANGE_EVERY_YEAR(June → August)
)
= Every Monday in summer
```

```
DIFFERENCE(
  UNION(Mon, Tue, Wed, Thu, Fri),
  DAY_IN_MONTH(1st Monday)
)
= Every weekday except the first Monday of the month
```

This makes it ideal for **appointment scheduling**, **availability windows**, **class timetables**, and any domain where recurring rules need to compose.

## Features

- **Composable temporal expressions**: build complex schedules from simple, reusable pieces
- **Set operations**: union (OR), intersection (AND), and difference (NOT) over date sets
- **Slot-aware scheduling**: attach capacity to any expression
- **Lazy date iteration**: generate future or past dates on demand without computing all dates upfront
- **JSON-driven configuration**: define schedules as serializable JSON, no code required

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

### A real-world example

Define a schedule for summer Monday classes with 30 available slots:

```typescript
import { ScheduleJsonParser, TemporalExpressionType, RangeEveryYearExpression } from 'deja-vu';

const config = {
  schedule: [
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
      slots: 30
    }
  ]
};

const scheduleElements = ScheduleJsonParser.parse(config);
scheduleElements[0].isOccurring(new Date('2025-06-02')); // true (it's a Monday in summer)
scheduleElements[0].isOccurring(new Date('2025-12-01')); // false (not in summer)
scheduleElements[0].slots(); // 30
```

## How It Works

Déjà vu uses a tree of expressions. **Leaf expressions** define base date sets, and **composite expressions** combine them using set operations.

### Leaf expressions

| Expression | What it matches |
|---|---|
| `DAY_IN_WEEK` | A specific weekday (e.g. every Monday) |
| `DAY_IN_MONTH` | The Nth weekday of a month (e.g. first Friday) |
| `RANGE_EVERY_YEAR` | A date range that recurs annually (e.g. June 1 – August 31) |

### Composite expressions

| Expression | Set operation | Meaning |
|---|---|---|
| `UNION` | A ∪ B | Date occurs in **any** child expression |
| `INTERSECTION` | A ∩ B | Date occurs in **all** child expressions |
| `DIFFERENCE` | A \ B | Date occurs in the included expression but **not** the excluded one |

These compose recursively: a union can contain intersections, a difference can exclude a union, and so on.

## Usage Guide

### Schedule Query Methods

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| `isOccurring` | date: Date | boolean | Checks if schedule occurs on given date |
| `slots` | date: Date | number | Gets available slots for given date |
| `datesInRange` | start: Date, end: Date | Date[] | Gets all scheduled dates in range |
| `nextOccurrence` | date: Date | Date | Gets next scheduled date after given date |
| `previousOccurrence` | date: Date | Date | Gets previous scheduled date before given date |
| `futureDates` | start: Date | Generator\<Date\> | Generates future scheduled dates |
| `pastDates` | start: Date | Generator\<Date\> | Generates past scheduled dates |

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

## Configuration Reference

### Day in Week Expression

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

### Day in Month Expression

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

### Range Every Year Expression

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

### Difference Expression

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

### Union Expression

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

### Intersection Expression

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
