package test.students.utils.search;


import test.students.utils.DateUtil;
import test.students.utils.search.comp.*;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilterFactory {

    private FilterFactory() {
    }

    public static List<FilterCriterion> from(Filter filter) {
        List<FilterParam> filterParams = filter.getFilterParams();
        List<FilterCriterion> filterCriteria = new ArrayList<>();
        for (FilterParam param : filterParams) {
            filterCriteria.add(from(param));
        }
        return filterCriteria;
    }

    public static FilterCriterion from(FilterParam filterParam) {

        String filterKey = filterParam.getKey();
        String filterType = filterParam.getFilterType();
        String expression = filterParam.getFilterExpression();
        if (filterType.equalsIgnoreCase(PredicateFactory.DATE_FILTER)) {
            try {
                DateFilterCriterion dfc = new DateFilterCriterion();
                dfc.setKey(filterKey);
                dfc.setOperation(expression);
                dfc.setDate(convertToDate(filterParam.getDateValue().getDate()));
                return dfc;
            } catch (ParseException pe) {
                throw new IllegalArgumentException("Cannot translate input date. Date pattern must be " + DateUtil.dateFormatterPattern() + ".");
            }
        } else if (filterType.equalsIgnoreCase(PredicateFactory.DATE_RANGE_FILTER)) {
            try {
                DateRangeValue dateRange = filterParam.getDateRangeValue();
                Date start = convertToDate(dateRange.getStart());
                Date end = convertToDate(dateRange.getEnd());
                DateRangeFilterCriterion drfc = new DateRangeFilterCriterion();
                drfc.setStart(start);
                drfc.setEnd(end);
                drfc.setKey(filterKey);
                drfc.setOperation(expression);
                return drfc;
            } catch (ParseException pe) {
                throw new IllegalArgumentException("Cannot translate input date. Date pattern must be " + DateUtil.dateFormatterPattern() + ".");
            }
        }else if (filterType.equalsIgnoreCase(PredicateFactory.INTEGER_RANGE_FILTER)) {
            IntegerRangeValue priceRange = filterParam.getPriceRangeValue();
            Integer start = priceRange.getStart();
            Integer end = priceRange.getEnd();
            IntegerRangeFilterCriteria irfc = new IntegerRangeFilterCriteria();
            irfc.setStart(start);
            irfc.setEnd(end);
            irfc.setKey(filterKey);
            irfc.setOperation(expression);
            return irfc;
        }
        else if (filterType.equalsIgnoreCase(PredicateFactory.TIME_FILTER)) {
            try {
                TimeValue timeValue = filterParam.getTimeValue();
                Time time = convertToTime(timeValue.getTime());
                TimeFilterCriterion tfc = new TimeFilterCriterion();
                tfc.setTime(time);
                tfc.setKey(filterKey);
                tfc.setOperation(expression);
                return tfc;
            } catch (ParseException pe) {
                throw new IllegalArgumentException("Cannot translate input date. Time pattern must be HH:mm");
            }
        } else if (filterType.equalsIgnoreCase(PredicateFactory.TIME_RANGE_FILTER)) {
            try {
                TimeRangeValue dateRange = filterParam.getTimeRangeValue();
                Time start = convertToTime(dateRange.getStart());
                Time end = convertToTime(dateRange.getEnd());
                TimeRangeFilterCriterion drfc = new TimeRangeFilterCriterion();
                drfc.setStart(start);
                drfc.setEnd(end);
                drfc.setKey(filterKey);
                drfc.setOperation(expression);
                return drfc;
            } catch (ParseException pe) {
                throw new IllegalArgumentException("Cannot translate input date. Time pattern must be HH:mm");
            }
        } else if (filterType.equalsIgnoreCase(PredicateFactory.TEXT_FILTER)) {
            TextValue textValue = filterParam.getTextValue();
            StringFilterCriterion sfc = new StringFilterCriterion();
            sfc.setKey(filterKey);
            sfc.setOperation(expression);
            sfc.setValue(textValue.getValue());
            return sfc;
        } else if (filterType.equalsIgnoreCase(PredicateFactory.TEXT_ARRAY_FILTER)) {
            ListOfStringsFilterCriterion listOfStringsFilterCriteria = new ListOfStringsFilterCriterion();
            listOfStringsFilterCriteria.setKey(filterKey);
            listOfStringsFilterCriteria.setOperation(expression);
            listOfStringsFilterCriteria.setValues(filterParam.getTextArrayValue().getList());
            return listOfStringsFilterCriteria;
        }
        throw new IllegalArgumentException("Invalid filter type " + filterType + ".");
    }

    private static Date convertToDate(String dateString) throws ParseException {
        SimpleDateFormat format = DateUtil.createDefaultDateFormatter();
        return format.parse(dateString);
    }

    private static Time convertToTime(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        return new Time(format.parse(dateString).getTime());
    }

}
