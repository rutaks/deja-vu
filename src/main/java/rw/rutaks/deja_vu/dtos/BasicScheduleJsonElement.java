package rw.rutaks.deja_vu.dtos;

import rw.rutaks.deja_vu.enums.RangeEveryYearExpression;
import rw.rutaks.deja_vu.enums.TemporalExpressionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BasicScheduleJsonElement {

    private TemporalExpressionType type;

    private RangeEveryYearExpression of;

    private String startDate;

    private String endDate;

    private String day;

    private int ordinal;

    private String startMonth;

    private String endMonth;

    private BasicScheduleJsonElement includedDate;

    private BasicScheduleJsonElement excludedDate;

    private List<BasicScheduleJsonElement> expressions;
}
