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
    private Texture dropPoints;

    private Sound Soundpoints;
    private Sound Soundexplosion;
    private Music musicambient;

    private Rectangle nave;
    private Array<Rectangle> rainAsteroides;
    private Array<Rectangle> rainPoints;

    private long lastDropTime;

    int contador;
    int contadorVidas = 3;

    private boolean isStarCollected = true; // Variable que indica si se ha recogido la estrella actual

    float speedY, speedX;

    public MyGdxGame(Drop game) {
        this.game = game;

        //Cargar la nave y el asteroide
        dropNave = new Texture(Gdx.files.internal("nave.png"));
        dropAsteroide = new Texture(Gdx.files.internal("asteroide.png"));
        dropPoints = new Texture(Gdx.files.internal("estrella.png"));


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
        nave.x = 800 / 2 - 64 / 2;
        nave.y = 20;
        nave.width = 64;
        nave.height = 64;

        //Crear lluvia de asteorides en un array
        rainAsteroides = new Array<Rectangle>();
        rainPoints = new Array<Rectangle>();
        //spawnRainAsteorides();
        spawnRainAsteoridesArriba();
        spawnPoints();
    }

    //Funcion para hacer los asteroides
    private void spawnRainAsteorides() {
        // Generar asteroides desde la izquierda o la derecha aleatoriamente
        Rectangle rainAsteroide = new Rectangle();
        if (MathUtils.randomBoolean()) {
            rainAsteroide.x = 0;
        } else {
            rainAsteroide.x = 800 - 64;
        }
        rainAsteroide.y = MathUtils.random(0, 480 - 64);
        rainAsteroide.width = 64;
        rainAsteroide.height = 64;

        rainAsteroides.add(rainAsteroide);

        lastDropTime = TimeUtils.nanoTime();
    }

    private void spawnRainAsteoridesArriba() {

        // Generar asteroides desde arriba o abajo aleatoriamente
        Rectangle rainAsteroide2 = new Rectangle();
        if (MathUtils.randomBoolean()) {
            rainAsteroide2.x = MathUtils.random(0, 800 - 64);
            rainAsteroide2.y = 480;
        } else {
            rainAsteroide2.x = MathUtils.random(0, 800 - 64);
            rainAsteroide2.y = 0 - 64;
        }
        rainAsteroide2.width = 64;
        rainAsteroide2.height = 64;

        rainAsteroides.add(rainAsteroide2);
        lastDropTime = TimeUtils.nanoTime();

    }

    private void spawnPoints() {
        if (isStarCollected) {
            Rectangle rainP = new Rectangle();
            rainP.x = MathUtils.random(0, 800 - 64);
            rainP.y = MathUtils.random(0, 480 - 64);
            rainP.width = 64;
            rainP.height = 64;

            rainPoints.add(rainP);
            lastDropTime = TimeUtils.nanoTime();

            // Establecer isStarCollected en false ya que acabamos de generar una nueva estrella
            isStarCollected = false;
        }

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
        game.getSpriteBatch().draw(dropNave, nave.x, nave.y, nave.width, nave.height);

        //Imprimir los asteroides
        for (Rectangle rain : rainAsteroides) {
            game.getSpriteBatch().draw(dropAsteroide, rain.x, rain.y, rain.width, rain.height);
        }

        for (Rectangle rainP : rainPoints) {
            game.getSpriteBatch().draw(dropPoints, rainP.x, rainP.y, rainP.width, rainP.height);
        }
        game.getSpriteBatch().end();

        // procesar la entrada del usuario
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            nave.x = touchPos.x - 64 / 2;

            // Mover la nave hacia la posición tocada en el eje X
            nave.x = touchPos.x - 64 / 2;

            // Mover la nave hacia la posición tocada en el eje Y
            nave.y = touchPos.y - 64 / 2;

            //Para que la nave no se salga de los limites
            limitarPosicionNave();
        }

        //Movimiento de la nave
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            nave.x -= 200 * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            nave.x += 200 * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            nave.y += 200 * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            nave.y -= 200 * Gdx.graphics.getDeltaTime();
        }

        //Para que la nave no se salga de los limites
        limitarPosicionNave();


        // asegúrese de que el depósito permanezca dentro de los límites de la pantalla
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            //spawnRainAsteorides();
            spawnRainAsteoridesArriba();
            spawnPoints();
        }

        // mueve los asteroides, elimina las que estén debajo del borde inferior de
        // la pantalla o que golpeó el cubo. En este último caso reproducimos
        // un efecto de sonido también.
        for (Iterator<Rectangle> iter = rainAsteroides.iterator(); iter.hasNext(); ) {
            Rectangle rainAst = iter.next();


            if (rainAst.x == 0) {
                // Mover asteroides generados desde la izquierda hacia la derecha
                rainAst.x += 200 * Gdx.graphics.getDeltaTime();
            } else {
                // Mover asteroides generados desde la derecha hacia la izquierda
                rainAst.x -= 200 * Gdx.graphics.getDeltaTime();
            }

            if (rainAst.y == 0) {
                // Mover asteroides generados desde abajo hacia arriba
                rainAst.y += 200 * Gdx.graphics.getDeltaTime();
            } else {
                // Mover asteroides generados desde arriba hacia abajo
                rainAst.y -= 200 * Gdx.graphics.getDeltaTime();
            }

//            rainAst.y += 200 * Gdx.graphics.getDeltaTime(); // Cambiado de '-200' a '200'

            /*
            rainAst.x -= MathUtils.random(0, 800 - 64) * Gdx.graphics.getDeltaTime(); // Movimiento horizontal aleatorio
            rainAst.y -= MathUtils.random(0, 480 - 64) * Gdx.graphics.getDeltaTime();
            rainAst.x += MathUtils.random(0, 800 - 64) * Gdx.graphics.getDeltaTime();*/

            if (rainAst.y + 64 < 0) {
                rainAst.x = MathUtils.random(0, 800 - 64);
                iter.remove();
            }
            if (rainAst.overlaps(nave)) {
                contadorVidas--;
                Soundexplosion.play();
                iter.remove();
            }
        }

        for (Iterator<Rectangle> iter = rainPoints.iterator(); iter.hasNext(); ) {
            Rectangle rainP = iter.next();
            if (rainP.y + 64 < 0) {
                iter.remove();
                isStarCollected = true; // Establecer isStarCollected en true cuando la estrella desaparezca

            }
            if (rainP.overlaps(nave)) {
                contador++;
                Soundpoints.play();
                iter.remove();
                isStarCollected = true; // Establecer isStarCollected en true cuando la estrella desaparezca

            }

        }


    }

    private void limitarPosicionNave() {
        // Asegurarse de que la nave no se salga de los límites de la pantalla
        nave.x = MathUtils.clamp(nave.x, 0, 800 - nave.width);
        nave.y = MathUtils.clamp(nave.y, 0, 480 - nave.height);
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
