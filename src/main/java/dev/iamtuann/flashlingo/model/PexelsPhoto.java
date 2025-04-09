package dev.iamtuann.flashlingo.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PexelsPhoto {
    private Long id;
    private Integer width;
    private Integer height;
    private String url;
    private String photographer;
    private String photographer_url;
    private Long photographer_id;
    private String avg_color;
    private PhotoSrc src;
    private Boolean liked;
    private String alt;
}

@Getter
@Setter
class PhotoSrc {
    private String original;
    private String large2x;
    private String large;
    private String medium;
    private String small;
    private String portrait;
    private String landscape;
    private String tiny;
}
