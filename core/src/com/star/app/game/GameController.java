package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;

public class GameController {
    private Background background;
    private Hero hero;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private Vector2 tmpVec;
    private ParticleController particleController;
    private PickUpsController pickUpsController;

    public PickUpsController getPickUpsController() {
        return pickUpsController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public Hero getHero() {
        return hero;
    }

    public Background getBackground() {
        return background;
    }

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController(this);
        this.particleController = new ParticleController();
        this.pickUpsController = new PickUpsController(this);
        this.asteroidController = new AsteroidController(this);
        this.tmpVec = new Vector2(0.0f, 0.0f);
        for (int i = 0; i < 3; i++) {
            asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200, 200), MathUtils.random(-200, 200), 1.0f);
        }
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        asteroidController.update(dt);
        particleController.update(dt);
        pickUpsController.update(dt);
        checkCollisions();
    }

    public void checkCollisions() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    particleController.setup(
                            b.getPosition().x + MathUtils.random(-4, 4),
                            b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-40, 40),
                            b.getVelocity().y * -0.3f + MathUtils.random(-40, 40),
                            0.2f,
                            2.5f, 1.2f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 0.0f
                    );
                    b.deactivate();
                    if (a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                    }
                    break;
                }
            }
        }
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (a.getHitArea().overlaps(hero.getHitArea())) {
                float dst = a.getPosition().dst(hero.getPosition());
                float halfOverLen = (a.getHitArea().radius + hero.getHitArea().radius - dst) / 2.0f;
                tmpVec.set(hero.getPosition()).sub(a.getPosition()).nor();
                hero.getPosition().mulAdd(tmpVec, halfOverLen);
                a.getPosition().mulAdd(tmpVec, -halfOverLen);

                float sumScl = hero.getHitArea().radius * 2 + a.getHitArea().radius;
                hero.getVelocity().mulAdd(tmpVec, 200.0f * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tmpVec, -200.0f * hero.getHitArea().radius / sumScl);

                hero.takeDamage(2);
                if (a.takeDamage(2)) {
                    hero.addScore(a.getHpMax() * 20);
                }
            }
        }


        //всё, как обычно
        for (int i = 0; i < pickUpsController.getActiveList().size(); i++) {
            PickUps p = pickUpsController.getActiveList().get(i);
            if (hero.getHitArea().contains(p.getPosition())){
                hero.reload(p);
                p.deactivate();
            }
        }
    }
}
