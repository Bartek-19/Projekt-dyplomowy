package pl.pollub.android.powerstrongapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(tableName = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @PrimaryKey
    private int id;
    @NonNull
    private String username;
    private String email;
    private String createDate;
}