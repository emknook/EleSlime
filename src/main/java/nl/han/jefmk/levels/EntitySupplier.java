package nl.han.jefmk.levels;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.YaegerEntity;

@FunctionalInterface
public interface EntitySupplier {
    YaegerEntity create(Coordinate2D location);
}
