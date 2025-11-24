package pl.pollub.android.powerstrongapp.data.local;

import androidx.room.TypeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import pl.pollub.android.powerstrongapp.data.local.entity.enums.SyncStatus;

public class Converters {
    @TypeConverter
    public static SyncStatus toSyncStatus(Integer value) {
        if (value == null) {
            return SyncStatus.NOT_SYNCED;
        }
        for (SyncStatus status : SyncStatus.values()) {
            if (status.getCode() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Nieznany kod statusu: " + value);
    }

    @TypeConverter
    public static Integer fromSyncStatus(SyncStatus status) {
        return status == null ? null : status.getCode();
    }
    // Zapis: List<Integer> -> String ("1,2,3")
    @TypeConverter
    public static String fromIntegerList(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    // Odczyt: String ("1,2,3") -> List<Integer>
    @TypeConverter
    public static List<Integer> toIntegerList(String data) {
        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(data.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}