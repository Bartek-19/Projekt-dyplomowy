package pl.pollub.android.powerstrongapp.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "user_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@Ignore}))
public class UserRecordEntity {
    @PrimaryKey
    private int exerciseId;
    private String exerciseName;
    private Double currentOneRepMax;
    private boolean isBodyweight;
    private String lastUpdatedDate;
}
