package nl.han.jefmk.entities.pickups;

import com.github.hanyaeger.api.Coordinate2D;

public enum ColliderPreset {
    CENTER {
        @Override
        public Coordinate2D toOffset(double tileSize, double radius) {
            return new Coordinate2D(tileSize / 2.0 - radius, tileSize / 2.0 - radius);
        }
    },
    BOTTOM_CENTER {
        @Override
        public Coordinate2D toOffset(double tileSize, double radius) {
            return new Coordinate2D(tileSize / 2.0 - radius, tileSize - 2 * radius);
        }
    },
    BOTTOM_LEFT {
        @Override
        public Coordinate2D toOffset(double tileSize, double radius) {
            return new Coordinate2D(0, tileSize - 2 * radius);
        }
    },
    BOTTOM_RIGHT {
        @Override
        public Coordinate2D toOffset(double tileSize, double radius) {
            return new Coordinate2D(tileSize - 2 * radius, tileSize - 2 * radius);
        }
    },
    TOP_CENTER {
        @Override
        public Coordinate2D toOffset(double tileSize, double radius) {
            return new Coordinate2D(tileSize / 2.0 - radius, 0);
        }
    };

    public abstract Coordinate2D toOffset(double tileSize, double radius);
}
