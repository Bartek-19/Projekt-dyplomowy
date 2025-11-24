package pl.pollub.android.powerstrongapp.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EffortTypeDto {
    private Integer id;
    private String name;
    private String description;
}
