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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
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
    private Sound Soundexplosion ;
    private Music musicambient;

    private Rectangle nave;
    private ArrayList<Rectangle> rainAsteroides;
    private ArrayList<Rectangle> rainPoints;

    private long lastDropTime;

    int contador;
    int contadorVidas = 3;

    private boolean isStarCollected = true; // Variable que indica si se ha recogido la estrella actual

    private float spawnInterval = 0.35f; // Intervalo de tiempo entre la generación de asteroides en segundos
    private float timeSinceLastSpawn = 0;

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
        rainAsteroides = new ArrayList<Rectangle>();
        rainPoints = new ArrayList<Rectangle>();

        spawnRainAsteroides();
        spawnPoints();

    }

    //Funcion para hacer los asteroides


    private void spawnRainAsteroides() {
        timeSinceLastSpawn += Gdx.graphics.getDeltaTime();

        // Verificar si ha pasado el tiempo suficiente para generar un nuevo asteroide
        if (timeSinceLastSpawn > spawnInterval) {
            // Generar un asteroide aleatoriamente en una dirección
            Rectangle rainAsteroide = new Rectangle();
            rainAsteroide.width = 32;
            rainAsteroide.height = 32;

            int direction = MathUtils.random(0, 3); // 0: Arriba, 1: Abajo, 2: Derecha, 3: Izquierda

            switch (direction) {
                case 0: // Arriba
                    rainAsteroide.x = MathUtils.random(0, 800 - rainAsteroide.width);
                    rainAsteroide.y = 480;
                    break;
                case 1: // Abajo
                    rainAsteroide.x = MathUtils.random(0, 800 - rainAsteroide.width);
                    rainAsteroide.y = -rainAsteroide.height;
                    break;
                case 2: // Derecha
                    rainAsteroide.x = -rainAsteroide.width;
                    rainAsteroide.y = MathUtils.random(0, 480 - rainAsteroide.height);
                    break;
                case 3: // Izquierda
                    rainAsteroide.x = 800;
                    rainAsteroide.y = MathUtils.random(0, 480 - rainAsteroide.height);
                    break;
            }

            rainAsteroide.x += speedX * Gdx.graphics.getDeltaTime();
            rainAsteroide.y += speedY * Gdx.graphics.getDeltaTime();


            rainAsteroides.add(rainAsteroide);
            timeSinceLastSpawn = 0; // Reiniciar el contador de tiempo

        }
    }

    private void spawnPoints() {
        if (isStarCollected) {
            Rectangle rainP = new Rectangle();
            rainP.x = MathUtils.random(0, 800 - 64);
            rainP.y = MathUtils.random(0, 480 - 64);
            rainP.width = 42;
            rainP.height = 42;

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
        game.getBitmapFont().draw(game.getSpriteBatch(), "Vidas: " + contadorVidas, 0, 470);


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
        if (TimeUtils.nanoTime() - lastDropTime > 1000000) {
            spawnRainAsteroides();
            spawnPoints();
        }

        // mueve los asteroides, elimina las que estén debajo del borde inferior de
        // la pantalla o que golpeó el cubo. En este último caso reproducimos
        // un efecto de sonido también.
        for (Iterator<Rectangle> iter = rainAsteroides.iterator(); iter.hasNext(); ) {
            Rectangle rainAst = iter.next();

            // Mover asteroides según la dirección desde la cual cayeron
            rainAst.x += -200 * Gdx.graphics.getDeltaTime();
            rainAst.y += 200 * Gdx.graphics.getDeltaTime();

            // Verificar si el asteroide ha salido completamente de la pantalla
            // Verificar si el asteroide ha salido completamente de la pantalla
            if ((speedX > 0 && rainAst.x > 800) || (speedX < 0 && rainAst.x + rainAst.width < 0) ||
                    (speedY > 0 && rainAst.y > 480) || (speedY < 0 && rainAst.y + rainAst.height < 0)) {
                iter.remove(); // Eliminar asteroides que han salido completamente de la pantalla
            }


            if (rainAst.overlaps(nave)) {
                // Manejar colisión con la nave
                contadorVidas--;
                Soundexplosion.play();
                System.out.println("COLISION");
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
                System.out.println("OBTENIDO");
                iter.remove();
                isStarCollected = true; // Establecer isStarCollected en true cuando la estrella desaparezca

                // Verificar si se agotaron las vidas
                if (contadorVidas <= 0) {
                    // Implementar lógica para cuando se quedan sin vidas
                    //game.setScreen(new GameOverScreen(game)); // Por ejemplo, cambiar a una pantalla de Game Over
                }
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
