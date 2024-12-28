package vttp.ssf.mini_project.models;

public class Ingredients {

    private String image;
    private String originalName;

    public Ingredients() {
    }

    public Ingredients(String image, String originalName) {
        this.image = image;
        this.originalName = originalName;
    }

    public String getImage() {
        return image;
    }

    public void setImages(String images) {
        this.image = images;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "originalName='" + originalName + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
