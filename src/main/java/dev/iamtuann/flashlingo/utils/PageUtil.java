package dev.iamtuann.flashlingo.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageUtil {
    public Pageable getPageable(int pageIndex, int pageSize, String key, String orderBy) {
        Sort sort = getSort(key, orderBy);

        if (pageSize != -1) {
            return PageRequest.of(pageIndex - 1, pageSize, sort);
        } else {
            return PageRequest.of(0, Integer.MAX_VALUE, sort);
        }

    }
    private Sort getSort(String key, String orderBy) {
        if (key != null && !key.isEmpty()) {
            return "asc".equalsIgnoreCase(orderBy)
                    ? Sort.by(key).ascending()
                    : Sort.by(key).descending();
        }
        return Sort.unsorted().ascending();
    }
}
