package test.students.utils.search;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import test.students.utils.search.comp.*;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecifierFactory {

    private OrderSpecifierFactory(){}

    public static OrderSpecifier<?>[] createOrderSpecifiers(SortCriteria[] sortCriteriaList, Object clz) {



        List<OrderSpecifier<?>> orderSpecs = new ArrayList<>();
        for (SortCriteria sortCriteria : sortCriteriaList) {
            String prop = sortCriteria.getKey();
            if (prop == null) {
                continue;
            }
            String sortType = sortCriteria.getType();
            if (sortType != null && "ASC".equalsIgnoreCase(sortType)) {
                OrderSpecifier<?> spec = new OrderSpecifier<>(Order.ASC, PathFactory.retrievePath(prop, clz));
                orderSpecs.add(spec);
            } else {
                OrderSpecifier<?> spec = new OrderSpecifier<>(Order.DESC, PathFactory.retrievePath(prop, clz));
                orderSpecs.add(spec);
            }

            if (!orderSpecs.isEmpty()) {
                OrderSpecifier<?>[] orderArray = new OrderSpecifier<?>[orderSpecs.size()];
                return orderSpecs.toArray(orderArray);

            }
        }
        return null;
    }
}
