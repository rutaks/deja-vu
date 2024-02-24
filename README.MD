# Deja Vu
![test-report](https://camo.githubusercontent.com/e8db08657c794851cc5cadddf3fec69fdf746eee261fb6dacff981d933029154/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f74657374732d38322532307061737365642d73756363657373)

This project in Java helps you work with and generate recurring events like specific dates or patterns. You can describe events using simple phrases like:

- On October 2nd.
- Every day in September.
- Every day from January 1st to Marc 20th.
- The second Monday of any month.

These descriptions work for any year.

You can also combine these descriptions in different ways, like adding them together, finding their overlap, or excluding some dates. For example:

- Find the first Monday and last Friday of every month from Jan. 21st to June 20th, except in March.

This package is for generating re-occurring events as schedules that follows explained pattern through a JSON object

### **Overview**

The Schedule Configuration JSON object is used to define schedules for events. Each schedule can specify the number of slots and includes/excludes specific dates according to complex date expression rules.

<aside>
💡 To Get a set of possible examples, go to the **[Examples]()** section in this document

</aside>

### Getting Started
#### Installation
1. Add the following in your pom.xml
```xml
<dependency>
    <groupId>rw.rutaks</groupId>
    <artifactId>deja_vu</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
2. Run the following locally
```bash
 mvn clean install -DskipTests
```

#### Usage
```java
List<ScheduleElement> elements = ScheduleJsonParser.parse(jsonConfig);
Schedule schedule = Schedule.of(elements);
LocalDate localDate = LocalDate.of(2023, 10, 2);
schedule.futureDates(localDate).limit(10).toList(); // get future dates in schedules with schedule pattern 

```

In depth documentation can be found [here](./DOCUMENTATION.MD)

## JSON Examples

- **Sprint Planning (every Mondays)**

    ```jsx
    {
      "scheduleCode": "TEAM_SPRINT_PLANNING",
      "scheduleConfig": {
        "schedule": [
          {
            "slots": 50,
            "type": "DAY_IN_WEEK",
            "day": "MONDAY"
          }
        ]
      }
    }
    ```

- **Christmas Day (25th Dec.)**

    ```jsx
    {
      "scheduleCode": "CHRISTMAS_DAY",
      "scheduleConfig": {
        "schedule": [
          {
            "slots": 0,
            "type": "RANGE_EVERY_YEAR",
            "of": "START_DAY_TO_END_DAY",
            "startDate": "12-25",
            "endDate": "12-25"
          }
        ]
      }
    }
    ```

- **Summer Season**(**June to August)**

    ```jsx
    {
      "scheduleCode": "JAN_TO_MAR",
      "scheduleConfig": {
        "schedule": [
          {
            "slots": 0,
    				"type": "RANGE_EVERY_YEAR",
            "of": "START_MONTH_TO_END_MONTH",
            "startMonth": "JUNE",
            "endMonth": "AUGUST"
          }
        ]
      }
    }
    ```

- **Company Town Hall (last Friday of the month)**

    ```jsx
    {
      "scheduleCode": "COMPANY_TOWN_HALL",
      "scheduleConfig": {
        "schedule": [
          {
            "slots": 50,
            "type": "DAY_IN_MONTH",
            "day": "FRIDAY",
            "ordinal": -1
          }
        ]
      }
    }
    ```

- **Rwanda National Holidays (Days can be found [here](https://www.gov.rw/holidays)) *[Good Friday, Easter Monday & Eid excluded]***

    ```json
    {
      "scheduleCode": "RWANDA_NATIONAL_HOLIDAYS",
      "scheduleConfig": {
        "schedule": [
          {
            "slots": 50,
            "type": "UNION",
            "expressions": [
              {
                "type": "RANGE_EVERY_YEAR",
                "of": "START_DAY_TO_END_DAY",
                "startDate": "01-01",
                "endDate": "01-02"
              },
              {
                "type": "RANGE_EVERY_YEAR",
                "of": "START_DAY_TO_END_DAY",
                "startDate": "02-01",
                "endDate": "02-01"
              },
              {
                "type": "RANGE_EVERY_YEAR",
                "of": "START_DAY_TO_END_DAY",
                "startDate": "04-07",
                "endDate": "04-07"
              },
              {
                "type": "RANGE_EVERY_YEAR",
                "of": "START_DAY_TO_END_DAY",
                "startDate": "05-01",
                "endDate": "05-01"
              },
              {
                "type": "RANGE_EVERY_YEAR",
                "of": "START_DAY_TO_END_DAY",
                "startDate": "07-01",
                "endDate": "07-01"
              },
              {
                "type": "RANGE_EVERY_YEAR",
                "of": "START_DAY_TO_END_DAY",
                "startDate": "07-04",
                "endDate": "07-04"
              },
              {
                "type": "RANGE_EVERY_YEAR",
                "of": "START_DAY_TO_END_DAY",
                "startDate": "08-04",
                "endDate": "08-04"
              },
              {
                "type": "RANGE_EVERY_YEAR",
                "of": "START_DAY_TO_END_DAY",
                "startDate": "08-15",
                "endDate": "08-15"
              },
              {
                "type": "RANGE_EVERY_YEAR",
                "of": "START_DAY_TO_END_DAY",
                "startDate": "08-25",
                "endDate": "08-26"
              }
            ]
          }
        ]
      }
    }
    ```

- **Marriage Declaration Booking Days (Every Thursday, except if the Thursday is a national holiday)**

    ```json
    {
      "scheduleCode": "MARRIAGE_DECLARATION_SCHEDULES",
      "scheduleConfig": {
        "schedule": [
          {
            "slots": 500,
            "type": "DIFFERENCE",
            "includedDate": {
              "type": "DAY_IN_WEEK",
              "day": "THURSDAY"
            },
            "excludedDate": {
              "type": "UNION",
              "expressions": [
                {
                  "type": "RANGE_EVERY_YEAR",
                  "of": "START_DAY_TO_END_DAY",
                  "startDate": "01-01",
                  "endDate": "01-02"
                },
                {
                  "type": "RANGE_EVERY_YEAR",
                  "of": "START_DAY_TO_END_DAY",
                  "startDate": "02-01",
                  "endDate": "02-01"
                },
                {
                  "type": "RANGE_EVERY_YEAR",
                  "of": "START_DAY_TO_END_DAY",
                  "startDate": "04-07",
                  "endDate": "04-07"
                },
                {
                  "type": "RANGE_EVERY_YEAR",
                  "of": "START_DAY_TO_END_DAY",
                  "startDate": "05-01",
                  "endDate": "05-01"
                },
                {
                  "type": "RANGE_EVERY_YEAR",
                  "of": "START_DAY_TO_END_DAY",
                  "startDate": "07-01",
                  "endDate": "07-01"
                },
                {
                  "type": "RANGE_EVERY_YEAR",
                  "of": "START_DAY_TO_END_DAY",
                  "startDate": "07-04",
                  "endDate": "07-04"
                },
                {
                  "type": "RANGE_EVERY_YEAR",
                  "of": "START_DAY_TO_END_DAY",
                  "startDate": "08-04",
                  "endDate": "08-04"
                },
                {
                  "type": "RANGE_EVERY_YEAR",
                  "of": "START_DAY_TO_END_DAY",
                  "startDate": "08-15",
                  "endDate": "08-15"
                },
                {
                  "type": "RANGE_EVERY_YEAR",
                  "of": "START_DAY_TO_END_DAY",
                  "startDate": "08-25",
                  "endDate": "08-26"
                }
              ]
            }
          }
        ]
      }
    }
    ```
