package taiga.models.sprint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserStoryProjectExtraInfo {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("logo_small_url")
    @Expose
    private Object logoSmallUrl; // TODO figure out what this is

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("slug")
    @Expose
    private String slug;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getLogoSmallUrl() {
        return logoSmallUrl;
    }

    public void setLogoSmallUrl(Object logoSmallUrl) {
        this.logoSmallUrl = logoSmallUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
