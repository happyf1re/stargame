package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.star.app.screen.ScreenManager;

public class GameController {
    private Background background;
    private Hero hero;
    private BulletController bulletController;
    private AsteroidController asteroidController;

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
        this.bulletController = new BulletController();
        this.asteroidController = new AsteroidController(this);
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
        checkCollisions();
    }

    //3.* Сделайте унижтожение астероидов с одного попадания
    public void checkCollisions() {
//        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
//            Asteroid a = asteroidController.getActiveList().get(i);
//            for (int j = 0; j < bulletController.getActiveList().size(); j++) {
//                Bullet b = bulletController.getActiveList().get(j);
//                //40 просто для удобства, у меня астероид рендерится не с центра, так что иногда может насквозь пролететь
//                if (b.getPosition().dst(a.getPosition()) < 40.0f) {
//                    a.deactivate();
//                    b.deactivate();
//                }
//            }
//        }
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if(a.getHitArea().contains(b.getPosition())) {
                    b.deactivate();
                    if(a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < asteroidController.getActiveList().size(); i++){
            Asteroid a = asteroidController.getActiveList().get(i);
                if (a.getHitArea().contains(hero.getPosition())){
                    a.takeDamage(1);
                    hero.takeDamage(1);
            }
        }
    }
}
