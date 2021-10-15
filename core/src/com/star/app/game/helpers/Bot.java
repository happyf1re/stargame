package com.star.app.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.GameController;
import com.star.app.game.Hero;
import com.star.app.screen.utils.Assets;

public class Bot {

    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 tmpVec;
    private float angle;
    private float enginePower;
    private Circle hitArea;


    public Bot(GameController gc) {
        //super(gc);
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(100, 100);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.enginePower = 500.0f;
        this.hitArea = new Circle(position, 500);
        this.enginePower = 300.0f;
        this.tmpVec = new Vector2();
    }


    public Vector2 getPosition() {
        return position;
    }



    public Vector2 getVelocity() {
        return velocity;
    }


    public float getAngle() {
        return angle;
    }


    public Circle getHitArea() {
        return hitArea;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1,
                angle);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        float stopKoef = 1.0f - 1.0f * dt;
        if (stopKoef < 0) {
            stopKoef = 0;
        }
        velocity.scl(stopKoef);
        tmpVec.set(gc.getHero().getPosition()).sub(position).nor();
        angle = tmpVec.angleDeg();
    }


}
