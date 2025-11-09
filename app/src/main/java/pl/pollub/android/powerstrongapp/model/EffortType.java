package pl.pollub.android.powerstrongapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "effort_type")
public class EffortType {
    @PrimaryKey
    private int id;
    @NotNull
    private String effortTypeName;
    private String description;
}