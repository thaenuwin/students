package test.students.utils.search.comp;

import lombok.Data;

import java.util.List;

@Data
public class QueryParam {
    private PaginationParam paginationParam;
    private Filter filter;
    private List<SortParam> sortingParams;
}
