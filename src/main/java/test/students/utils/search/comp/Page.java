package test.students.utils.search.comp;

import java.util.List;

public class Page<E> {

    private int pageNumber;
    private int totalPages;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private int pageSize;
    private long totalNumberOfElements;
    private long numberOfElements;
    private List<E> items;
    private Filter filter;
    private List<SortParam> sortParam;
}
