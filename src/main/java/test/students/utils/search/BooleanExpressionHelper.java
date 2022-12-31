package test.students.utils.search;


import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.CollectionUtils;
import test.students.utils.search.comp.FilterCriterion;
import test.students.utils.search.comp.FilterLogic;

import java.util.List;

public class BooleanExpressionHelper {

    private BooleanExpressionHelper(){}

    public static BooleanExpression processExpression(List<FilterCriterion> filterCriteria, Object clz, FilterLogic filterLogic){
        BooleanExpression allFilter=null;
        if(!CollectionUtils.isEmpty(filterCriteria)){
            for(FilterCriterion filterCriterion: filterCriteria){
                BooleanExpression booleanExpression = PredicateFactory.createBooleanExpression(filterCriterion, clz);
                if(allFilter != null){
                    if(filterLogic == FilterLogic.FILTER_LOGIC_AND){
                        allFilter = allFilter.and(booleanExpression);
                    }else if(filterLogic == FilterLogic.FILTER_LOGIC_OR) {
                        allFilter = allFilter.or(booleanExpression);
                    }
                }else{
                    allFilter = booleanExpression;
                }
            }
        }
        return allFilter;
    }
}
