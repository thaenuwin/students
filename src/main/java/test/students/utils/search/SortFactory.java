package test.students.utils.search;


import test.students.utils.search.comp.SortCriteria;
import test.students.utils.search.comp.SortParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortFactory {

    private SortFactory(){}

    public static SortCriteria[] from(List<SortParam> inputs) {
        List<SortParam> sortParams = new ArrayList();
        sortParams.addAll(inputs);
        List<SortCriteria> list = new ArrayList<>();
        Collections.sort(sortParams, new Comparator<SortParam>() {
            @Override
            public int compare(SortParam arg0, SortParam arg1) {
                return Integer.valueOf(arg0.getOrder()).compareTo(arg1.getOrder());
            }
        });
        SortCriteria[] array = new SortCriteria[list.size()];
        for (SortParam param : sortParams) {
            list.add(from(param));
        }
        return list.toArray(array);
    }

    public static SortCriteria from(SortParam sortParam) {
        SortCriteria sc = new SortCriteria();
        sc.setKey(sortParam.getKey());
        sc.setType(sortParam.getSortType());
        return sc;
    }

}
