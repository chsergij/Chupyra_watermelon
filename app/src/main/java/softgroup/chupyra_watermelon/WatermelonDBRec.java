package softgroup.chupyra_watermelon;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "WatermelonDBRec")

public class WatermelonDBRec extends Model {

    @Column(name = "variety")
    public String variety;

    @Column(name = "photoId")
    public int photoId;

}