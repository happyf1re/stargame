package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;

public class Hero {
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float enginePower;
    private GameController gc;
    private float fireTimer;

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = new Texture("ship.png");
        this.position = new Vector2(640, 360);
        this.velocity = new Vector2(0,0);
        this.angle = 0.0f;
        this.enginePower = 500.0f;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x-32, position.y-32, 32, 32, 64, 64, 1,
                1, angle, 0,0,64,64,false,false);
    }

    public void update(float dt) {
        fireTimer += dt;
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            if (fireTimer > 0.2) {
                fireTimer = 0.0f;
                gc.getBulletController().setup(position.x, position.y,
                        MathUtils.cosDeg(angle) * 500 + velocity.x, MathUtils.sinDeg(angle) * 500 + velocity.y);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180.0f * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle += -180.0f * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
            velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;
        }

        position.mulAdd(velocity, dt); //складывает вектора и скалярно умножает на нужную величину
        float stopKoef = 1.0f - 1.0f * dt;

        //непонятно, зачем нужно это условие
        if (stopKoef < 0) {
            stopKoef = 0;
        }
        velocity.scl(stopKoef);

        //2. Сделать по кнопке S движение назад с уменьшенной скоростью
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.x += -MathUtils.cosDeg(angle) * 120.0f * dt;
            position.y += -MathUtils.sinDeg(angle) * 120.0f * dt;
            velocity.set(-MathUtils.cosDeg(angle) * 120.0f * dt, -MathUtils.sinDeg(angle) * 120.0f * dt);
        }

        if(position.x < 32f) {
            position.x = 32f;
            velocity.x *= -1;
        }
        if(position.x > ScreenManager.SCREEN_WIDTH - 32f) {
            position.x = ScreenManager.SCREEN_WIDTH - 32f;
            velocity.x *= -1;
        }
        if(position.y < 32f) {
            position.y = 32f;
            velocity.y *= -1;
        }
        if(position.y > ScreenManager.SCREEN_HEIGHT - 32f) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32f;
            velocity.y *= -1;
        }



    }
}
