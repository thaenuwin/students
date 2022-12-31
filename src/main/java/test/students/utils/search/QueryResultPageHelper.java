package test.students.utils.search;


import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAQueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.CollectionUtils;
import test.students.domain.StudentQueryParam;
import test.students.persistence.entity.QStudents;
import test.students.utils.search.comp.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public final class QueryResultPageHelper {


    private static final Class TAG = QueryResultPageHelper.class;

    private QueryResultPageHelper() {
    }

    public static <Q, I, O> QueryResultPage<O> query(QueryParam queryParam,
                                                     EntityPathBase<Q> epb, EntityManager em, ResultItemConverter<I, O> converter) {
        PaginationParam paginationParam = queryParam.getPaginationParam();
        int pageNumber = paginationParam.getPageNumber();
        int pageSize = paginationParam.getPageSize();

        validatePagination(pageNumber, pageSize);

        BooleanExpression parentEntityCriteria = null;
        Filter filter = queryParam.getFilter();

        if (filter != null) {
            parentEntityCriteria = BooleanExpressionHelper.processExpression(FilterFactory.from(filter), epb, filter.getFilterLogic());
        }
        JPAQueryFactory factory = new JPAQueryFactory(em);

        int offset = (pageNumber - 1) * pageSize;

        JPAQuery query = factory.selectFrom(epb)
                .offset(offset).limit(pageSize).distinct();

        List<SortParam> sortParams = queryParam.getSortingParams();
        if (sortParams != null) {
            OrderSpecifier[] orders = OrderSpecifierFactory.
                    createOrderSpecifiers(SortFactory.from(sortParams), epb);
            query.orderBy(orders);
        }

        if (queryParam instanceof StudentQueryParam) {
            prepareStudentDataQuery((StudentQueryParam) queryParam, query);
        }

        if (parentEntityCriteria != null) {
            log.info( "Predicate:" + parentEntityCriteria);
            query.where(parentEntityCriteria);
        }

        JPAQueryBase qb = query.fetchAll();
        long fetchCount = qb.fetchCount();
        QueryResults results = qb.fetchResults();
        List<I> queryItems = results.getResults();

        List<O> items = new ArrayList<>();
        for (I itm : queryItems) {
            items.add(converter.from(itm));
        }

        QueryResultPage<O> resultPage = new QueryResultPage<>();
        resultPage.setPageNumber(pageNumber);
        resultPage.setPageSize(pageSize);
        resultPage.setItems(items);
        resultPage.setNumberOfElements(items.size());
        resultPage.setTotalElements(fetchCount);
        int pageCount = (int) (fetchCount / pageSize);
        int sub = (int) (fetchCount % pageSize);
        if (sub > 0) {
            pageCount++;
        }
        resultPage.setTotalPages(pageCount);
        return resultPage;
    }

    private static void prepareStudentDataQuery(StudentQueryParam queryParam, JPAQuery query) {
        String stuId = queryParam.getStudentId();
        String stuMajor = queryParam.getStudentMajor();
        String stuName = queryParam.getStudentName();
        String stuGrade = queryParam.getStudentGrade();
        if(stuId!=null){
            query.where(QStudents.students.studentPk.studentId.
                    contains(stuId));
        }
        if(stuMajor!=null){
            query.where(QStudents.students.studentPk.studentMajor.
                    contains(stuMajor));
        }
        if(stuName!=null){
            query.where(QStudents.students.studentPk.studentName.
                    contains(stuName));
        }
        if(stuGrade!=null){
            query.where(QStudents.students.studentPk.studentGrade.
                    contains(stuGrade));
        }
    }


    private static void validatePagination(int pageNumber, int pageSize) {
        if (pageNumber < 1) {
            throw new IllegalArgumentException("Invalid input. Page Number less than 1 is not allowed.");
        }

        if (pageSize < 1) {
            throw new IllegalArgumentException("Invalid input. Minimum Page Size must be 1.");
        }

        if (pageSize > 10000) {
            throw new IllegalArgumentException("Invalid input. Maximum page size allow is 10000.");
        }
    }
}
