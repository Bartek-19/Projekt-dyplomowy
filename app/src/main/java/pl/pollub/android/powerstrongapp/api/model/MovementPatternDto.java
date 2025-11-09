package pl.pollub.android.powerstrongapp.api.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovementPatternDto implements Serializable {
    private Integer id;
    private String name;
}