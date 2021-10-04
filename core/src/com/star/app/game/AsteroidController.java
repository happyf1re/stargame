package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.helpers.ObjectPool;


//2. Заверните ваши астероиды в контроллер
public class AsteroidController extends ObjectPool<Asteroid> {
    private Texture asteroidTexture;

    @Override
    protected Asteroid newObject() {
        return new Asteroid();
    }

    public AsteroidController() {
        this.asteroidTexture = new Texture("asteroid.png");
    }

    //астероид рисуется не из центра
    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Asteroid asteroid = activeList.get(i);
            batch.draw(asteroidTexture, asteroid.getPosition().x, asteroid.getPosition().y,
                    64, 64);
       }
    }

    public void setup(float x, float y, float vx, float vy) {
        getActiveElement().activate(x, y, vx, vy);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
