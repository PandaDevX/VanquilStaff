package com.vanquil.staff.utility;

import java.util.List;

public class PaginatedList {

    private List<?> list;
    private int perPage;

    public PaginatedList(List<?> list, int perPage) {
        this.list = list;
        this.perPage = perPage;
    }

    public <T> List<T> getPage(int page) {
        if(page > getMaximumPage()) {
            page = getMaximumPage();
        }
        int start = (page - 1) * perPage;
        int end = Math.min(page * perPage, list.size());
        return (List<T>) list.subList(start, end);
    }

    public int getMaximumPage() {
        return list.size() / perPage + (Math.min(list.size() % perPage, 1));
    }
}
