package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.StarGame;
import com.star.app.screen.ScreenManager;

public class Background {
    private class Star {
        private Vector2 position;
        private Vector2 velocity;
        private float scale;

        public Star() {
            this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200),
                    MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200));
            this.velocity = new Vector2(MathUtils.random(-40, -5), 0);
            this.scale = Math.abs(velocity.x) / 40f * 0.8f;
        }

        public void update(float dt) {
            position.x += (velocity.x - gc.getHero().getVelocity().x * 0.1) * dt;
            position.y += (velocity.y - gc.getHero().getVelocity().y * 0.1) * dt;
            if (position.x < -200) {
                position.x = ScreenManager.SCREEN_WIDTH + 200;
                position.y = MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200);
                scale = Math.abs(velocity.x) / 40f * 0.8f;
            }
        }
    }

    //3. Сделать астероид, который летает в произвольную сторону и пересекает экран( и появляется с другой стороны)
    // не уверен, что внутренний класс - лучший вариант
    private class Asteroid {
        private Vector2 position;
        private Vector2 velocity;
        private float scale;

        public Asteroid() {
            this.position = new Vector2(MathUtils.random(0, ScreenManager.SCREEN_WIDTH), MathUtils.random(0, ScreenManager.SCREEN_HEIGHT));
            this.velocity = new Vector2(MathUtils.random(-0.5f,0.5f), MathUtils.random(-0.5f,0.5f));
        }

        public void update(float dt) {
            position.x += velocity.x * dt;
            position.y += velocity.y * dt;
            if (position.x < -250) {
                position.x = ScreenManager.SCREEN_WIDTH + 50;
            }
            if (position.x > ScreenManager.SCREEN_WIDTH + 250) {
                position.x = ScreenManager.SCREEN_WIDTH - 1400;
            }
            if (position.y < -250) {
                position.y = ScreenManager.SCREEN_HEIGHT + 50;
            }
            if (position.y > ScreenManager.SCREEN_HEIGHT + 250){
                position.y = ScreenManager.SCREEN_HEIGHT - 850;
            }

        }
    }

    private final int STAR_COUNT = 1000;
    private GameController gc;
    private Texture textureCosmos;
    private Texture textureStar;
    private Texture textureAsteroid;
    private Asteroid asteroid;
    private Star[] stars;

    public Background(GameController gc) {
        this.textureCosmos = new Texture("bg.png");
        this.textureStar = new Texture("star16.png");
        this.textureAsteroid = new Texture("asteroid.png");
        this.stars = new Star[STAR_COUNT];
        this.gc = gc;
        this.asteroid = new Asteroid();
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
    }

    public void render (SpriteBatch batch) {
        batch.draw(textureCosmos, 0, 0);
        for (int i = 0; i < stars.length; i++) {
            batch.draw(textureStar, stars[i].position.x - 8, stars[i].position.y - 8,
                    8, 8, 16, 16, stars[i].scale, stars[i].scale, 0, 0, 0,
                    16, 16, false, false);

            if (MathUtils.random(0, 300) < 1) {
                batch.draw(textureStar, stars[i].position.x - 8, stars[i].position.y - 8,
                        8, 8, 16, 16, stars[i].scale * 2, stars[i].scale * 2,
                        0, 0, 0, 16, 16, false, false);
            }
        }
        //решил не заморачиваться с кучей параметров и просто напрямую указал размеры
        batch.draw(textureAsteroid, asteroid.position.x, asteroid.position.y, 64, 64);
    }

    public void update(float dt){
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(dt);
            asteroid.update(dt);
        }

    }
}
