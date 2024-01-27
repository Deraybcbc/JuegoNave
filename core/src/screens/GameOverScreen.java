package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Drop;
import com.mygdx.game.MyGdxGame;

import helpers.AssetManager;
import utils.Settings;

public class GameOverScreen implements Screen {

    private Drop game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    private float timeSinceTouch = 0f;
    private final float touchCooldown = 1.5f;

    private GlyphLayout textLayout;

    public GameOverScreen(Drop game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        font = new BitmapFont();

        textLayout = new GlyphLayout();
        textLayout.setText(AssetManager.font, "GameOver");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // Limpiar la pantalla
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizar la cámara y el lote de sprites
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Iniciar el lote de sprites
        batch.begin();

        // Mostrar el mensaje de Game Over en el centro de la pantalla
        font.draw(batch, "Dar click para volver a empezar", 290, 250);
        font.draw(batch, "Game Over", 350, 350);


        // Aquí puedes agregar más información, como puntajes finales, opciones de reinicio, etc.

        // Finalizar el lote de sprites
        batch.end();

        // Si se toca la pantalla, reiniciar el juego después de la espera
        if (Gdx.input.isTouched() && timeSinceTouch > touchCooldown) {
            game.setScreen(new MyGdxGame(game)); // Cambia MyGdxGame a la clase principal de tu juego
            dispose();
        }

        // Incrementar el tiempo desde el último toque
        timeSinceTouch += delta;

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Libera los recursos cuando la pantalla ya no es necesaria
        batch.dispose();
        font.dispose();
    }
}
