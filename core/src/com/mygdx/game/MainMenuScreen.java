package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenuScreen implements Screen {

    public Drop game;
    private OrthographicCamera camera;

    public MainMenuScreen(Drop game) {
        this.game = game;

        //Inicialitza y configurar la camara
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Esborra la pantalla amb color blanc
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Actualizar camara
        camera.update();

        game.getSpriteBatch().begin();

        game.getBitmapFont().draw(game.getSpriteBatch(),"Hacer clic a la pantalla",100,150);

        game.getSpriteBatch().end();

        //Comprovar si se hace clicl
        if(Gdx.input.isTouched()){
            game.setScreen(new MyGdxGame(game));
            dispose();
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

    }
}
