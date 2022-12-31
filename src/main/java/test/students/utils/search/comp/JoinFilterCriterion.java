package test.students.utils.search.comp;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JoinFilterCriterion<E> extends FilterCriterion{
    List<E> entities;
}
