package nl.han.jefmk.entities.player;

import com.github.hanyaeger.api.entities.AnimationCallback;

public class PlayerJumpCallBack implements AnimationCallback {
    private final PlayerSprite playerSprite;

    public PlayerJumpCallBack(PlayerSprite playerSprite) {
        this.playerSprite = playerSprite;
    }

    @Override
    public void call() {
        playerSprite.isNoLongerJumping();
    }
}
