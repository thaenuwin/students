package test.students.utils.search.comp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryResultPage<E> {
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long numberOfElements;
    private long totalElements;
    private List<E> items;
}
