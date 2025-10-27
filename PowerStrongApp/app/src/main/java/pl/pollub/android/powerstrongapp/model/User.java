package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    public int id;

    public String username;
    public String password;
    public String email;
    public boolean activeStatus;
    public Date createDate;
}


