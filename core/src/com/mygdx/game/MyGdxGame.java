package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyGdxGame implements Screen {
    SpriteBatch batch;
    Texture img;
    private Drop game;
    private OrthographicCamera camera;

    private Texture dropAsteroide;
    private Texture dropNave;

    private Sound Soundpoints;
    private Sound Soundexplosion;
    private Music musicambient;

    private Rectangle nave;
    private Array<Rectangle> rainAsteroides;
    private Array<Rectangle> rainPoints;

    private long lastDropTime;

    int contador;
    int contadorVidas = 3;


    public MyGdxGame(Drop game) {
        this.game = game;

        //Cargar la nave y el asteroide
        dropNave = new Texture(Gdx.files.internal("nave.png"));
        dropAsteroide = new Texture(Gdx.files.internal("asteroide.png"));

        //Cargar sonido del juego
        //Sonido Ambiente
        musicambient = Gdx.audio.newMusic(Gdx.files.internal("Ambiente.mp3"));
        //Puntos
        Soundpoints = Gdx.audio.newSound(Gdx.files.internal("Puntos.mp3"));
        //Choque
        Soundexplosion = Gdx.audio.newSound(Gdx.files.internal("choques.mp3"));

        //Poner la musica de ambiente a sonar inmediatamente
        musicambient.setLooping(true);
        musicambient.play();

        //crear la camara
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        //Crear el rectangulo que representa el bucket
        nave = new Rectangle();
        nave.x = 800 / 2 -  64 /2;
        nave.y = 20;
        nave.width = 64;
        nave.height = 64;

        //Crear lluvia de asteorides en un array
        rainAsteroides = new Array<Rectangle>();
        spawnRainAsteorides();

    }

    //Funcion para hacer los asteroides
    private void spawnRainAsteorides() {
        float asteroidSpawnX;
        float asteroidSpawnY;

        // Generar asteroides en posiciones iniciales diferentes
        int spawnType = MathUtils.random(2); // 0: derecha, 1: izquierda, 2: abajo

        switch (spawnType) {
            case 0: // Derecha
                asteroidSpawnX = 800; // Posición inicial a la derecha
                asteroidSpawnY = MathUtils.random(0, 480 - 64); // Posición vertical aleatoria
                break;
            case 1: // Izquierda
                asteroidSpawnX = -64; // Posición inicial a la izquierda
                asteroidSpawnY = MathUtils.random(0, 480 - 64); // Posición vertical aleatoria
                break;
            case 2: // Abajo
                asteroidSpawnX = MathUtils.random(0, 800 - 64); // Posición horizontal aleatoria
                asteroidSpawnY = 480; // Posición inicial arriba
                break;
            default:
                asteroidSpawnX = 0;
                asteroidSpawnY = 0;
                break;
        }

        Rectangle rainAsteroide = new Rectangle();
        rainAsteroide.x = asteroidSpawnX;
        rainAsteroide.y = asteroidSpawnY;
        rainAsteroide.width = 64;
        rainAsteroide.height = 64;
        rainAsteroides.add(rainAsteroide);
        lastDropTime = TimeUtils.nanoTime();

    }

    private void spawnPoints() {
        Rectangle rainP = new Rectangle();
        rainP.x = MathUtils.random(0, 800 - 64);
        rainP.y = 480;
        rainP.width = 64;
        rainP.height = 64;
        rainPoints.add(rainP);
        lastDropTime = TimeUtils.nanoTime();

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Poner la pantalla de color azul oscuro
        ScreenUtils.clear(0, 0, 0.2f, 1);

        //Actulizar camara
        camera.update();

        // le decimos al SpriteBatch que renderice en el
        // sistema de coordenadas especificado por la cámara.
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        // comenzamos un nuevo lote y sacamos la nave y
        // todos los asteroides
        game.getSpriteBatch().begin();

        //Imprimir en pantalla los puntos que va obteniendo
        game.getBitmapFont().draw(game.getSpriteBatch(), "Puntos: " + contador, 0, 470);

        //Imprimir la nave
        game.getSpriteBatch().draw(dropNave, nave.x, nave.y, nave.width ,nave.height);

        //Imprimir los asteroides
        for (Rectangle rain : rainAsteroides) {
            game.getSpriteBatch().draw(dropAsteroide, rain.x, rain.y, rain.width, rain.height);
        }
        game.getSpriteBatch().end();

        // procesar la entrada del usuario
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            nave.x = touchPos.x - 64 / 2;
        }

        //Movimiento de la nave
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            nave.x -= 200 * Gdx.graphics.getDeltaTime();
        }
        ;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            nave.x += 200 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.BUTTON_A)) {
            nave.y += 200 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            nave.y -= 200 * Gdx.graphics.getDeltaTime();
        }

        // asegúrese de que el depósito permanezca dentro de los límites de la pantalla
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            spawnRainAsteorides();
        }
        // mueve los asteroides, elimina las que estén debajo del borde inferior de
        // la pantalla o que golpeó el cubo. En este último caso reproducimos
        // un efecto de sonido también.
        for (Iterator<Rectangle> iter = rainAsteroides.iterator(); iter.hasNext(); ) {
            Rectangle rainAst = iter.next();

            float speedX = MathUtils.random(-200, 200); // Velocidad horizontal aleatoria
            float speedY = MathUtils.random(-200, 200); // Velocidad vertical aleatoria

            rainAst.y += speedY * Gdx.graphics.getDeltaTime();
            rainAst.x += speedX * Gdx.graphics.getDeltaTime();
            //rainAst.x += MathUtils.random(-200, 200) * Gdx.graphics.getDeltaTime(); // Movimiento horizontal aleatorio

            if (rainAst.y + 64 < 0) {
                iter.remove();
            }
            if (rainAst.overlaps(nave)) {
                contadorVidas--;
                iter.remove();
            }
        }




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
        batch.dispose();
        img.dispose();
        dropAsteroide.dispose();
        dropNave.dispose();
        Soundpoints.dispose();
        Soundexplosion.dispose();
        musicambient.dispose();
    }
}
