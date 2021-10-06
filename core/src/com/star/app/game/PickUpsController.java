package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.screen.utils.Assets;

public class PickUpsController extends ObjectPool<PickUps> {
    private GameController gc;
    private TextureRegion ammoTexture;

    @Override
    protected PickUps newObject() {
        return new PickUps(gc);
    }

    public PickUpsController(GameController gc) {
        this.gc = gc;
        this.ammoTexture = Assets.getInstance().getAtlas().findRegion("bullets");
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            PickUps ammo = activeList.get(i);
            batch.draw(ammoTexture, ammo.getPosition().x - 16, ammo.getPosition().y - 16);
        }
    }

    public void setup(float x, float y) {
        getActiveElement().activate(x, y);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
