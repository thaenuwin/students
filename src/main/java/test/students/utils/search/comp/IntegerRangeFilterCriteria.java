package test.students.utils.search.comp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntegerRangeFilterCriteria  extends FilterCriterion {
    private Integer start;
    private Integer end;
}
