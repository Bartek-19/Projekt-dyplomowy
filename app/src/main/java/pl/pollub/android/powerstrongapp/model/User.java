package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "user")
public class User {
    @PrimaryKey
    private int id;
    private String username;
    private String password;
    private String email;
    private boolean activeStatus;
    private Date createDate;
}