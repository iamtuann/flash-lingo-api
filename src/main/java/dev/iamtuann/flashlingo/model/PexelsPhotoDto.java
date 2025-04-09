package dev.iamtuann.flashlingo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PexelsPhotoDto {
    private Long id;
    private Integer width;
    private Integer height;
    private String url;
    private String photographer;
    private String photographer_url;
    private Long photographer_id;
    private String src;
    private String alt;

    public PexelsPhotoDto(PexelsPhoto photo) {
        this.id = photo.getId();
        this.width = photo.getWidth();
        this.height = photo.getHeight();
        this.url = photo.getUrl();
        this.photographer = photo.getPhotographer();
        this.photographer_url = photo.getPhotographer_url();
        this.photographer_id = photo.getPhotographer_id();
        this.src = photo.getSrc().getMedium();
        this.alt = photo.getAlt();
    }
}
