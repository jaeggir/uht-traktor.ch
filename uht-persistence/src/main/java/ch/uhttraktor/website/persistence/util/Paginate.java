package ch.uhttraktor.website.persistence.util;

import java.util.List;

public class Paginate {

    public static <T> Page<T> paginate(List<T> list, Integer pageSize, Integer page) {
        Page<T> result = new Page();

        if (list.size() > 0) {
            // Ensure page is in sane range
            if ((page-1) * pageSize > list.size()) {
                page = new Double(Math.floor(list.size() / pageSize)).intValue();
            } else if (page < 1) {
                page = 1;
            }

            // Compute indexes to sub-select
            int fromIndex = (page - 1) * pageSize;
            int toIndex = page * pageSize;
            if (toIndex > list.size()) {
                toIndex = list.size();
            }
            List<T> subList = list.subList(fromIndex, toIndex);
            result.setItems(subList);
        } else {
            // Empty list, let's just return it
            result.setItems(list);
        }

        result.setTotalItems(list.size());
        result.setPage(page);

        return result;
    }

}
