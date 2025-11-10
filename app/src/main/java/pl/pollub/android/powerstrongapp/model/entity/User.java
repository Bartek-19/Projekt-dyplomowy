package pl.pollub.android.powerstrongapp.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @PrimaryKey
    private int id;
    private String username;
    private String password;
    private String email;
    private boolean activeStatus;
    private Date createDate;
}