package test.students.utils.search.comp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortParam {

    public static final String DESC="DESC";
    public static final String ASC="ASC";

    private int order;
    private String key;
    private String sortType;
}
