package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Bot extends Ship {
    private int critical;
    private Vector2 tempVector;

    public int getCritical() {
        return critical;
    }

    public Bot(GameController gc) {
        super(gc, 50,
                MathUtils.random(ScreenManager.SCREEN_WIDTH - 200, ScreenManager.SCREEN_WIDTH - 100),
                MathUtils.random(100, ScreenManager.SCREEN_HEIGHT - 100));
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.enginePower = 200.0f;
        this.critical = 30;
        this.ownerType = OwnerType.BOT;
        this.tempVector = new Vector2();
        createWeapons();
        this.weaponNum = 0;
        this.currentWeapon = weapons[weaponNum];
    }

    public void update(float dt) {
        super.update(dt);
        tempVector.set(gc.getHero().getPosition()).sub(position).nor();
        angle = tempVector.angleDeg();
        if (gc.getHero().getPosition().dst(position) > 200) {
            accelerate(dt);
        }
        if (gc.getHero().getPosition().dst(position) < 300) {
            tryToFire();
        }
        if (velocity.len() > 50.0f) {
            float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(
                        bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * -0.3f + MathUtils.random(-20, 20), velocity.y * -0.3f + MathUtils.random(-20, 20),
                        0.5f,
                        1.2f, 0.2f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 0.0f
                );
            }
        }
    }

    private void createWeapons() {
        weapons = new Weapon[]{
                new Weapon(
                        gc, this, "GreenLaser", 0.3f, 1, 600, 30000,
                        new Vector3[]{
                                new Vector3(28, 90, 0),
                                new Vector3(28, -90, 0)
                        }),
        };
    }
}
