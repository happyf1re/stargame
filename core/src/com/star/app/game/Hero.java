package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;
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
    private StringBuilder stringBuilder;
    private Weapon currentWeapon;

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

    public void addScore(int amount) {
        this.score += amount;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(640, 360);
        this.velocity = new Vector2(0, 0);
        this.hp = 100;
        this.angle = 0.0f;
        this.enginePower = 500.0f;
        this.stringBuilder = new StringBuilder();
        this.hitArea = new Circle(position, 26);
        this.currentWeapon = new Weapon(
                gc, this, "Laser", 0.2f, 1, 600, 100,
                new Vector3[]{
                        new Vector3(20, 0, 0),
                        new Vector3(20, 90, 20),
                        new Vector3(20, -90, -20)
                }
        );
    }

    public void takeDamage(int amount) {
        hp -= amount;
    }

    public void reload(PickUps p) {
        currentWeapon.addBullets(10);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1,
                1, angle);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        stringBuilder.clear();
        stringBuilder.append("SCORE: ").append(scoreView).append("\n");
        stringBuilder.append("HP: ").append(hp).append("\n");
        stringBuilder.append("BULLETS: ").append(currentWeapon.getCurBullets()).append(" / ")
                .append(currentWeapon.getMaxBullets()).append("\n");
        font.draw(batch, stringBuilder, 20, 700);
    }

    public void update(float dt) {
        fireTimer += dt;
        updateScore(dt);
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            tyrToFire();
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
        hitArea.setPosition(position);
        float stopKoef = 1.0f - 1.0f * dt;

        //непонятно, зачем нужно это условие
        if (stopKoef < 0) {
            stopKoef = 0;
        }
        velocity.scl(stopKoef);
        if (velocity.len() > 50.0f) {
            float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(
                        bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * -0.3f + MathUtils.random(-20, 20), velocity.y * -0.3f + MathUtils.random(-20, 20),
                        0.5f,
                        1.2f, 0.2f,
                        1.0f, 0.3f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 1.0f

                );
            }
        }
        checkSpaceBorders();

        //2. Сделать по кнопке S движение назад с уменьшенной скоростью
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.x += -MathUtils.cosDeg(angle) * 120.0f * dt;
            position.y += -MathUtils.sinDeg(angle) * 120.0f * dt;
            velocity.set(-MathUtils.cosDeg(angle) * 120.0f * dt, -MathUtils.sinDeg(angle) * 120.0f * dt);
        }

    }

    private void updateScore(float dt) {
        if (scoreView < score) {
            scoreView += 1000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }

    private void tyrToFire() {
        if (fireTimer > 0.2) {
            fireTimer = 0.0f;
            currentWeapon.fire();
//            float wx;
//            float wy;
//            wx = position.x + MathUtils.cosDeg(angle + 90) * 20;
//            wy = position.y + MathUtils.sinDeg(angle + 90) * 20;
//            gc.getBulletController().setup(wx, wy,
//                    MathUtils.cosDeg(angle) * 500 + velocity.x, MathUtils.sinDeg(angle) * 500 + velocity.y);
//
//            wx = position.x + MathUtils.cosDeg(angle - 90) * 20;
//            wy = position.y + MathUtils.sinDeg(angle - 90) * 20;
//            gc.getBulletController().setup(wx, wy,
//                    MathUtils.cosDeg(angle) * 500 + velocity.x, MathUtils.sinDeg(angle) * 500 + velocity.y);
//
        }
    }

    public void checkSpaceBorders() {
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
