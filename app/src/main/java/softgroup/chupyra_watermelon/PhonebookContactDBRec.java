package softgroup.chupyra_watermelon;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "PhonebookDBRec")
public class PhonebookContactDBRec extends Model {
    @Column(name = "name")
    public String name;

    @Column(name = "phone")
    public String phone;

    @Column(name = "readonly")
    public boolean readonly;
}
