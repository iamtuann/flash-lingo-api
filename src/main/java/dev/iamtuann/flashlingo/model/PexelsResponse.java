package dev.iamtuann.flashlingo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PexelsResponse {
    private Integer total_results;
    private Integer page;
    private Integer per_page;
    private List<PexelsPhoto> photos;
    private String next_page;
}
