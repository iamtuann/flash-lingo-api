package dev.iamtuann.flashlingo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PageDto<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;

    public PageDto(List<T> content, Page<?> page) {
        this.content = content;
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }
}
