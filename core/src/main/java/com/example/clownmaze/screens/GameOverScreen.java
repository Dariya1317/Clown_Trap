package com.example.clownmaze.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {

    private final Game        game;
    private final GameScreen  gameScreen;
    private final SpriteBatch batch;
    private final Texture     gameOverTex;
    private final Texture     returnDefaultTex;
    private final Texture     returnHoverTex;

    private final Rectangle returnBounds = new Rectangle();
    private boolean         returnHovered;

    public GameOverScreen(Game game, GameScreen gameScreen) {
        this.game        = game;
        this.gameScreen  = gameScreen;
        this.batch       = new SpriteBatch();
        this.gameOverTex = new Texture(Gdx.files.internal("ui/game_over.png"));
        returnDefaultTex = loadTexture("images/ReturnToMainMenu_button.png");
        returnHoverTex   = loadTexture("images/ReturnToMainMenu_button_hover.png");

        computeButtonBounds(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private static Texture loadTexture(String path) {
        if (!Gdx.files.internal(path).exists()) {
            Gdx.app.error("GameOverScreen", "Файл не найден: assets/" + path);
            return null;
        }
        return new Texture(Gdx.files.internal(path));
    }

    private void computeButtonBounds(int w, int h) {
        if (returnDefaultTex == null) return;

        float maxW  = w * 0.35f;
        float maxH  = h * 0.20f;
        float scale = Math.min(1f, Math.min(maxW / returnDefaultTex.getWidth(),
                                            maxH / returnDefaultTex.getHeight()));
        float bw = returnDefaultTex.getWidth()  * scale;
        float bh = returnDefaultTex.getHeight() * scale;
        returnBounds.set((w - bw) / 2f, h * 0.10f, bw, bh);
    }

    @Override
    public void render(float delta) {
        int   w  = Gdx.graphics.getWidth();
        int   h  = Gdx.graphics.getHeight();
        float mx = Gdx.input.getX();
        float my = h - Gdx.input.getY();

        returnHovered = returnBounds.contains(mx, my);

        if (returnHovered && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            returnToMenu();
            return;
        }

        ScreenUtils.clear(0f, 0f, 0f, 1f);
        batch.begin();

        batch.draw(gameOverTex, 0, 0, w, h);

        if (returnDefaultTex != null) {
            Texture t = (returnHovered && returnHoverTex != null) ? returnHoverTex : returnDefaultTex;
            batch.draw(t, returnBounds.x, returnBounds.y, returnBounds.width, returnBounds.height);
        }

        batch.end();
    }

    private void returnToMenu() {
        gameScreen.dispose();
        game.setScreen(new MainMenuScreen(game));
    }

    @Override public void show() {}

    @Override
    public void resize(int w, int h) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, w, h);
        computeButtonBounds(w, h);
    }

    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        batch.dispose();
        gameOverTex.dispose();
        if (returnDefaultTex != null) returnDefaultTex.dispose();
        if (returnHoverTex   != null) returnHoverTex.dispose();
    }
}
