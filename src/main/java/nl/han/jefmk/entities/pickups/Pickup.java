package nl.han.jefmk.entities.pickups;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.YaegerEntity;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.player.Player;

public abstract class Pickup extends SpriteEntity {

    private final YaegerEntity pickupCollider;

    protected Pickup(String resource, Coordinate2D initialLocation, double colliderRadius) {
        this(resource, initialLocation, colliderRadius, ColliderPreset.CENTER);
    }

    protected Pickup(String resource, Coordinate2D initialLocation, double colliderRadius, ColliderPreset preset) {
        this(resource, initialLocation, colliderRadius, preset.toOffset(EleSlime.TILE_SIZE, colliderRadius));
    }

    protected Pickup(String resource, Coordinate2D initialLocation, double colliderRadius, Coordinate2D colliderOffset) {
        this(resource, initialLocation, new Size(EleSlime.TILE_SIZE), colliderRadius, colliderOffset);
    }

    protected Pickup(String resource, Coordinate2D initialLocation, Size spriteSize, double colliderRadius, Coordinate2D colliderOffset) {
        super(resource, initialLocation, spriteSize);
        Coordinate2D colliderLocation = new Coordinate2D(
                initialLocation.getX() + colliderOffset.getX(),
                initialLocation.getY() + colliderOffset.getY()
        );
        pickupCollider = new PickupCollider(this, colliderRadius, colliderLocation);
    }

    /** Constructor for subclasses that supply their own collider entity. */
    protected Pickup(String resource, Coordinate2D initialLocation, Size spriteSize, YaegerEntity collider) {
        super(resource, initialLocation, spriteSize);
        pickupCollider = collider;
    }

    public YaegerEntity getPickupCollider() {
        return pickupCollider;
    }

    public abstract void onPlayerCollision(Player player);
}
