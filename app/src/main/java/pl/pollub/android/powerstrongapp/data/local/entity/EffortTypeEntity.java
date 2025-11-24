package pl.pollub.android.powerstrongapp.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "effort_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@Ignore}))
public class EffortTypeEntity {
    @PrimaryKey
    private int id;
    private String name;
    private String description;

    @Override
    public String toString() { return name; }
}