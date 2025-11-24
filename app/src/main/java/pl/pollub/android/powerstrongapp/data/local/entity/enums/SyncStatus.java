package pl.pollub.android.powerstrongapp.data.local.entity.enums;

import lombok.Getter;

@Getter
public enum SyncStatus {
    NOT_SYNCED(0),
    SYNCED(1),
    FAILED(2);

    private final int code;

    SyncStatus(int code) {
        this.code = code;
    }

}
