package nl.han.jefmk.surfaces;

import com.github.hanyaeger.api.Coordinate2D;

public interface SurfaceOwner {
    Coordinate2D getAnchorLocation();
    double getWidth();
    double getHeight();

    default double getDeltaX() {
        return 0;
    }

    default double getDeltaY() {
        return 0;
    }
}
