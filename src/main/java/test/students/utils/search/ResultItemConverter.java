package test.students.utils.search;

public abstract class ResultItemConverter<I, O> {

    public abstract O from(I i);
}
