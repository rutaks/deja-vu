# Documentation

### Structure
    ```json
    
    {
      "scheduleCode": "",
      "scheduleConfig": {
        "schedule": [
          {
            "slots": 100,
            "type": "DIFFERENCE",
            "includedDate": { ... },
            "excludedDate": { ... }
          }
        ]
      }
    }
    ```


### **Properties**

1. `**scheduleCode**` (String):
    - **Description:** An identifier for the event associated with the schedule.
    - **Example:** **`"scheduleCode": "TEAM_SPRINT_PLANNING"`**
2. **`scheduleConfig`** (Object):
    - **Description:** Contains the schedule details.

### Schedule Array (schedule)

- **Description:** An array of schedule elements, each specifying the number of slots and date expressions.

### Schedule Element Properties

1. **`slots`** ***(Integer)***:
    - **Description:** The number of slots available for the schedule.
    - **Example:** **`"slots": 100`**
2. **`type`** ***(String)***:
    - **Description:** Specifies the type of date expression used for inclusion/exclusion.
    - **Example**
        - **`"type": "DIFFERENCE"`**
    - **Supported Format Expressions**
        - **Singular Expressions**
            - **`DAY_IN_MONTH`**: Specifies specific days of the month to include/exclude.
                - Required Properties to provide:
                    - **`day`** : Mentions the day of the week.
                        - Possible values: `**MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY**`
                    - `**ordinal`:** Number mentioning the number of the week
                        - Possible values: `**-5, -4, -3, -2, -1, 1, 2, 3, 4, 5**`
                            - We can have up to 5 weeks in a month hence the max number 5
                            - Negative numbers mean the `x`  week of the month in reverse starting from the end. Example:  `-1` would be the last week of the month, `1` would be the first week of the month, and `-2` would be the second to last week of the month
                - **Example**

                    ```jsx
                    { "type": "DAY_IN_MONTH", "day": "MONDAY", "ordinal": 1 }
                    ```

            - **`RANGE_EVERY_YEAR`**: Specifies date ranges that repeat every year.
                - Required Properties to provide:
                    - `**of**`: The type of date range to use
                        - Possible Values: `**START_DAY_TO_END_DAY, START_MONTH_TO_END_MONTH**`
                        - `**START_DAY_TO_END_DAY`:** This means that the date will range from a specific day of a month specific to another a specific day of a month.
                            - Mandatory Fields for this operation are `**startDate**` and `**endDate`.** These two fields follow the format of `MM-DD`. Example: `12-31` is the 31st of December
                        - `**START_MONTH_TO_END_MONTH`:** This means that the date will range from a month specific to another month.
                            - Mandatory Fields for this operation are **`startMonth`** and `**endMonth`.** These two fields follow the format of the full month name in caps. Example: `**DECEMBER**`
                - **Example**

                    ```jsx
                    // START_MONTH_TO_END_MONTH example (from April to October)
                    {
                      "type": "RANGE_EVERY_YEAR",
                      "of": "START_MONTH_TO_END_MONTH",
                      "startMonth": "APRIL",
                      "endMonth": "OCTOBER"
                    }
                    // START_DAY_TO_END_DAY example (from 00:00:00 of 31st Dec to 23:59:59 of 31st Dec)
                    {
                      "type": "RANGE_EVERY_YEAR",
                      "of": "START_DAY_TO_END_DAY",
                      "startDate": "12-31",
                      "endDate": "12-31"
                    },
                    ```

            - **`DAY_IN_WEEK`**: Specifies specific days of the week to include/exclude.
                - Required Properties to provide:
                    - **`day`** : Mentions the day of the week.
                        - Possible values: `**MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY**`
                - **Example**

                    ```jsx
                    { "type": "DAY_IN_WEEK", "day": "MONDAY" }
                    ```

        - **Combinatory Expressions**
            - **`UNION`**: Specifies a schedule with included date expressions, it is a way to group dates together
                - Required Properties to provide:
                    - `**expressions`:** Representing the list of other expressions to group together
                - **Example**

                    ```json
                    {
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
                    	  }
                    	]
                    }
                    ```

            - **`DIFFERENCE`**: Specifies a schedule with both included and excluded dates.
                - When this prop is defined, it will depend on two mandatory properties to be provided, **`[includedDate](https://www.notion.so/Schedule-Configuration-JSON-Object-Documentation-7fef9f46442f4972b0e44671d6790e46?pvs=21)` & `[excludedDate](https://www.notion.so/Schedule-Configuration-JSON-Object-Documentation-7fef9f46442f4972b0e44671d6790e46?pvs=21)`**
                - Required Properties to provide:
                    - **`includedDate`** (Object):
                        - Description: Contains date expressions for inclusion in the schedule.
                    - **`excludedDate`** (Object):
                        - Description: Contains date expressions for exclusion from the schedule.
                - **Example**

                    ```json
                    {
                      "schedule": [
                        {
                          "slots": 500,
                          "type": "DIFFERENCE",
                          "includedDate": {
                            "type": "DAY_IN_WEEK",
                            "day": "THURSDAY"
                          },
                          "excludedDate": {
                            "type": "RANGE_EVERY_YEAR",
                            "of": "START_MONTH_TO_END_MONTH",
                            "startMonth": "JANUARY",
                            "endMonth": "MARCH"
                          }
                        }
                      ]
                    }
                    ```
