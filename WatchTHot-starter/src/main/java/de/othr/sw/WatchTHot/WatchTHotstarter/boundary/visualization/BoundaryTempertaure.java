package de.othr.sw.WatchTHot.WatchTHotstarter.boundary.visualization;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class BoundaryTempertaure {
    private String temp;
    public String getTemp() {
        return this.temp;
    }
}
