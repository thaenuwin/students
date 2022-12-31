package test.students.utils.search.comp;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListOfStringsFilterCriterion extends FilterCriterion {
    private List<String> values;
}
