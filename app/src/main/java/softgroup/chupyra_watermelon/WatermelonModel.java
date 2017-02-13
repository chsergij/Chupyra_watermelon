package softgroup.chupyra_watermelon;


public class WatermelonModel {
    String variety;
    int photoId;
    long id;

    WatermelonModel(String variety, int photoId) {
        this.id = id;
        this.variety = variety;
        this.photoId = photoId;
    }

    WatermelonModel(long id, String variety, int photoId) {
        this.id = id;
        this.variety = variety;
        this.photoId = photoId;
    }

    public long getId() { return this.id;}

    public void setId(Long id) {
        this.id = id;
    }

    public String getVariety() {
        return this.variety;
    }

    public void setVariety(String newVariety) {
        this.variety = newVariety;
    }

    public long getPhotoId() {
        return this.photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}

