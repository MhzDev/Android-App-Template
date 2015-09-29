package com.mhzdev.apptemplate.Controller.Home;

/**
 * Created by Michele Stefanelli (MhzDev) on 29/09/2015.
 */
public class GenericModel {
    private Long id;
    private String title;
    private String subtitle;
    private String image_url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
