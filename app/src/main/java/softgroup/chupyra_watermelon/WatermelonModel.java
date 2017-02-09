package softgroup.chupyra_watermelon;


import java.util.ArrayList;
import java.util.List;

public class WatermelonModel {
    String variety;
    int photoId;
//    int id;

    WatermelonModel(String variety, int photoId) {
        //this.id = id;
        this.variety = variety;
        this.photoId = photoId;
    }

    public String getVariety() {
        return this.variety;
    }

    public void setVariety(String newVariety) {
        this.variety = newVariety;
    }

    public int getPhotoId() {
        return this.photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}

