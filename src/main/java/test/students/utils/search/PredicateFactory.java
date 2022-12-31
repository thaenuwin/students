package test.students.utils.search;


import com.querydsl.core.types.dsl.*;
import org.springframework.util.StringUtils;
import test.students.utils.search.comp.*;

import java.sql.Time;
import java.util.*;

public class PredicateFactory {

    public static final String TEXT_FILTER = "text";
    public static final String TEXT_ARRAY_FILTER = "textArray";
    public static final String DATE_RANGE_FILTER = "dateRange";
    public static final String DATE_FILTER = "date";
    public static final String TIME_RANGE_FILTER = "timeRange";
    public static final String TIME_FILTER = "time";

    public static final String INTEGER_RANGE_FILTER = "integerRange";


    private PredicateFactory() {
    }

    public static BooleanExpression createBooleanExpression(FilterCriterion criterion, Object q) {

        if (criterion == null || criterion.getKey() == null || criterion.getOperation() == null) {
            throw new IllegalArgumentException("Invalid filter object!");
        }

        if (criterion instanceof StringFilterCriterion) {
            return createBooleanExpressionForString((StringFilterCriterion) criterion, q);
        }
        if (criterion instanceof ListOfStringsFilterCriterion) {
            return createBooleanExpressionForListOfString((ListOfStringsFilterCriterion) criterion, q);
        }

        if (criterion instanceof DateFilterCriterion) {
            return createBooleanExpressionForDate((DateFilterCriterion) criterion, q);
        }

        if (criterion instanceof DateRangeFilterCriterion) {
            return createBooleanExpressionForDateRange((DateRangeFilterCriterion) criterion, q);
        }

        if (criterion instanceof TimeRangeFilterCriterion) {
            return createBooleanExpressionForTimeRange((TimeRangeFilterCriterion) criterion, q);
        }

        if (criterion instanceof TimeFilterCriterion) {
            return createBooleanExpressionForTime((TimeFilterCriterion) criterion, q);
        }

        if (criterion instanceof IntegerRangeFilterCriteria) {
            return createBooleanExpressionForIntegerRange((IntegerRangeFilterCriteria) criterion, q);
        }
        return null;

    }

    private static BooleanExpression createBooleanExpressionForIntegerRange(IntegerRangeFilterCriteria criteria, Object q) {
        String key = criteria.getKey();
        String operation = criteria.getOperation();
        Integer start = criteria.getStart();
        Integer end = criteria.getEnd();
        NumberPath path = PathFactory.retireveIntegerPath(key, q);

        if (path == null) {
            return createBooleanExpressionForIntegerRange(PathFactory.retireveIntegerPath(key, q), operation, start, end);
        }

        return createBooleanExpressionForIntegerRange(path, operation, start, end);
    }

    private static BooleanExpression createBooleanExpressionForIntegerRange(NumberPath path, String operation, Integer start, Integer end) {
        if (start != null && end != null) {
            if ("between".equalsIgnoreCase(operation)) {
                return path.between(start, end);
            } else if ("notBetween".equalsIgnoreCase(operation)) {
                return path.between(start, end).not();
            } else {
                throw new IllegalArgumentException("Invalid operator for date range filter.");
            }
        }


        throw new IllegalArgumentException("Invalid start date or end date");
    }


    public static BooleanExpression createBooleanExpressionForJoinOfEntity(JoinFilterCriterion criterion, Object queryEntity) {
        String key = criterion.getKey();
        ListPath path = PathFactory.retrieveListPath(key, queryEntity);
        return path.any().in(criterion.getEntities());
    }

    public static BooleanExpression createBooleanExpressionForListOfString(ListOfStringsFilterCriterion criteria, Object q) {
        String key = criteria.getKey();
        StringPath path = PathFactory.retireveStringPath(key, q);
        String operation = criteria.getOperation();
        List<String> value = criteria.getValues();
        return createBooleanExpressionForListOfString(path, operation, value);
    }

    public static BooleanExpression createBooleanExpressionForListOfString(StringPath path, String operation, List<String> values) {

        if (!StringUtils.isEmpty(values)) {
            if ("in".equalsIgnoreCase(operation)) {
                if (values.size() == 1) {
                    values.add("");
                }
                return path.in(values);
            } else if ("notIn".equalsIgnoreCase(operation)) {
                if (values.size() == 1) {
                    values.add("");
                }
                return path.notIn(values);
            } else {
                throw new IllegalArgumentException("Invalid operator for text array filter");
            }
        } else {
            throw new IllegalArgumentException("Empty text array is not allowed.");
        }
    }

    public static Map<String, List<String>> expressionMap() {
        Map<String, List<String>> map = new TreeMap<>();
        String[] textFilterExpressions = {
                "startsWithIgnoreCase", "endsWithIgnoreCase", "containsIgnoreCase",
                "equalsIgnoreCase", "startsWith", "endsWith", "contains", "equals", "ne"
        };
        map.put(TEXT_FILTER, Arrays.asList(textFilterExpressions));


        String[] dateRangeFilterExpressions = {
                "between", "notBetween"
        };
        map.put(DATE_RANGE_FILTER, Arrays.asList(dateRangeFilterExpressions));

        String[] dateExpressions = {
                "before", "after"
        };
        map.put(DATE_FILTER, Arrays.asList(dateExpressions));


        return map;
    }

    public static BooleanExpression createBooleanExpressionForString(StringFilterCriterion criteria, Object q) {
        String key = criteria.getKey();
        StringPath path = PathFactory.retireveStringPath(key, q);
        String operation = criteria.getOperation();
        String value = criteria.getValue();
        return createBooleanExpressionForString(path, operation, value);
    }

    public static BooleanExpression createBooleanExpressionForString(StringPath path, String operation, String value) {

        if ("startsWithIgnoreCase".equalsIgnoreCase(operation)) {
            return path.startsWithIgnoreCase(value);
        } else if ("endsWithIgnoreCase".equalsIgnoreCase(operation)) {
            return path.endsWithIgnoreCase(value);
        } else if ("containsIgnoreCase".equalsIgnoreCase(operation)) {
            return path.containsIgnoreCase(value);
        } else if ("equalsIgnoreCase".equalsIgnoreCase(operation)) {
            return path.equalsIgnoreCase(value);
        } else if ("startsWith".equalsIgnoreCase(operation)) {
            return path.startsWith(value);
        } else if ("endsWith".equalsIgnoreCase(operation)) {
            return path.endsWith(value);
        } else if ("contains".equalsIgnoreCase(operation)) {
            return path.contains(value);
        } else if ("equals".equalsIgnoreCase(operation)) {
            return path.matches(value);
        } else if ("ne".equalsIgnoreCase(operation)) {
            if (value == null) {
                return path.isNotNull();
            }
            return path.ne(value);
        } else {
            throw new IllegalArgumentException("Invalid operator for text filter.");
        }
    }

    public static BooleanExpression createBooleanExpressionForDate(DateFilterCriterion criteria, Object q) {
        String key = criteria.getKey();
        String operation = criteria.getOperation();
        DateTimePath<Date> path = PathFactory.retirieveDateTimePath(key, q);
        Date date = criteria.getDate();

        return createBooleanExpressionForDate(path, operation, date);
    }

    public static BooleanExpression createBooleanExpressionForDate(DateTimePath<Date> path, String operation, Date date) {

        if (date != null) {
            if ("before".equalsIgnoreCase(operation)) {
                return path.before(date);
            } else if ("after".equalsIgnoreCase(operation)) {
                return path.after(date);
            } else {
                throw new IllegalArgumentException("Invalid operator for date filter.");
            }
        }

        throw new IllegalArgumentException("Invalid date.");
    }


    public static BooleanExpression createBooleanExpressionForDateRange(DateRangeFilterCriterion criteria, Object q) {
        String key = criteria.getKey();
        String operation = criteria.getOperation();
        Date start = criteria.getStart();
        Date end = criteria.getEnd();
        DatePath<Date> path = PathFactory.retireveDatePath(key, q);

        if (path == null) {
            return createBooleanExpressionForDateRange(PathFactory.retirieveDateTimePath(key, q), operation, start, end);
        }

        return createBooleanExpressionForDateRange(path, operation, start, end);
    }

    private static BooleanExpression createBooleanExpressionForDateRange(DatePath<Date> path, String operation, Date start, Date end) {

        if (start != null && end != null) {
            if ("between".equalsIgnoreCase(operation)) {
                return path.between(start, end);
            } else if ("notBetween".equalsIgnoreCase(operation)) {
                return path.between(start, end).not();
            } else {
                throw new IllegalArgumentException("Invalid operator for date range filter.");
            }
        }

        throw new IllegalArgumentException("Invalid start date or end date");
    }



    public static BooleanExpression createBooleanExpressionForTime(TimeFilterCriterion criteria, Object q) {
        String key = criteria.getKey();
        String operation = criteria.getOperation();
        TimePath<Time> path = PathFactory.retrieveTimePath(key, q);
        Time time = criteria.getTime();

        return createBooleanExpressionForTime(path, operation, time);
    }

    public static BooleanExpression createBooleanExpressionForTime(TimePath<Time> path, String operation, Time time) {

        if (time != null) {
            if ("before".equalsIgnoreCase(operation)) {
                return path.loe(time);
            } else if ("after".equalsIgnoreCase(operation)) {
                return path.goe(time);
            } else {
                throw new IllegalArgumentException("Invalid operator for date filter.");
            }
        }

        throw new IllegalArgumentException("Invalid time.");
    }


    public static BooleanExpression createBooleanExpressionForTimeRange(TimeRangeFilterCriterion criteria, Object q) {
        String key = criteria.getKey();
        String operation = criteria.getOperation();
        Time start = criteria.getStart();
        Time end = criteria.getEnd();
        TimePath<Time> path = PathFactory.retrieveTimePath(key, q);
        return createBooleanExpressionForTimeRange(path, operation, start, end);
    }

    private static BooleanExpression createBooleanExpressionForTimeRange(TimePath<Time> path, String operation, Time start, Time end) {

        if (start != null && end != null) {
            if ("between".equalsIgnoreCase(operation)) {
                return path.between(start, end);
            } else if ("notBetween".equalsIgnoreCase(operation)) {
                return path.between(start, end).not();
            } else {
                throw new IllegalArgumentException("Invalid operator for time range filter.");
            }
        }


        throw new IllegalArgumentException("Invalid start time or end time");
    }

    public static BooleanExpression createBooleanExpressionForDateRange(DateTimePath<Date> path, String operation, Date start, Date end) {

        if (start != null && end != null) {
            if ("between".equalsIgnoreCase(operation)) {
                return path.between(start, end);
            } else if ("notBetween".equalsIgnoreCase(operation)) {
                return path.between(start, end).not();
            } else {
                throw new IllegalArgumentException("Invalid operator for date range filter.");
            }
        }


        throw new IllegalArgumentException("Invalid start date or end date");
    }


    protected static <E extends Number & Comparable<Number>> NumberPath<E> defineNumberPath(String key, Class<E> cls, PathBuilder<?> entityPath) {
        return entityPath.getNumber(key, cls);
    }

    protected static boolean isDate(Object object) {
        return object instanceof Date;
    }
}
