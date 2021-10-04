package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero {
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float enginePower;
    private GameController gc;
    private float fireTimer;
    private int score;
    private int scoreView;
    private int hp;
    private Circle hitArea;
    private Asteroid asteroid;

    private final float BASE_SIZE = 64.0f;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    public int getHp() {
        return hp;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public int getScore() {
        return score;
    }

    public int getScoreView() {
        return scoreView;
    }

    public void addScore(int amount) {
        this.score += amount;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(640, 360);
        this.velocity = new Vector2(0, 0);
        this.hp = 100;
        //this.hitArea.setPosition(position);
        //this.hitArea.setRadius(BASE_RADIUS * 0.9f);
        this.angle = 0.0f;
        this.enginePower = 500.0f;
    }

    public void takeDamage(int amount) {
        hp -= amount;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1,
                1, angle);
    }

    public void update(float dt) {
        fireTimer += dt;
        if (scoreView < score) {
            scoreView += 1000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            if (fireTimer > 0.2) {
                fireTimer = 0.0f;
                float wx;
                float wy;
                wx = position.x + MathUtils.cosDeg(angle + 90) * 20;
                wy = position.y + MathUtils.sinDeg(angle + 90) * 20;
                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * 500 + velocity.x, MathUtils.sinDeg(angle) * 500 + velocity.y);

                wx = position.x + MathUtils.cosDeg(angle - 90) * 20;
                wy = position.y + MathUtils.sinDeg(angle - 90) * 20;
                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * 500 + velocity.x, MathUtils.sinDeg(angle) * 500 + velocity.y);

            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle += -180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
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
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.x += -MathUtils.cosDeg(angle) * 120.0f * dt;
            position.y += -MathUtils.sinDeg(angle) * 120.0f * dt;
            velocity.set(-MathUtils.cosDeg(angle) * 120.0f * dt, -MathUtils.sinDeg(angle) * 120.0f * dt);
        }

        if (position.x < 32f) {
            position.x = 32f;
            velocity.x *= -1;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32f) {
            position.x = ScreenManager.SCREEN_WIDTH - 32f;
            velocity.x *= -1;
        }
        if (position.y < 32f) {
            position.y = 32f;
            velocity.y *= -1;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32f) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32f;
            velocity.y *= -1;
        }


    }
}
