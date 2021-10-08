package com.star.app.game;

import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;

public class PickUps implements Poolable {
    private GameController gc;
    private Vector2 position;
    private boolean active;
    private float time;

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public PickUps(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0,0);
        this.active = false;
    }

    public void deactivate(){
        active = false;
    }

    public void activate(float x, float y) {
        position.set(x, y);
        this.time = 0f;
        active = true;
    }

    public void update (float dt) {
        time += dt;
        if (time >= 10f) {
            deactivate();
        }

    }
}
