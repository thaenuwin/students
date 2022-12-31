package test.students.utils.search.comp;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterParam {
    private int order;
    private String key;
    private String filterType;
    private String filterExpression;
    private TextValue textValue;
    private DateValue dateValue;
    private TimeValue timeValue;
    private TimeRangeValue timeRangeValue;
    private DateRangeValue dateRangeValue;
    private IntegerRangeValue priceRangeValue;
    private TextArrayValue textArrayValue;
}
