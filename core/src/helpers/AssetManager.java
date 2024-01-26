package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {

    private static Texture dropAsteroide;
    private static Texture dropNave;
    private static Texture dropPoints;
    public static Texture fondo;

    public static TextureRegion background;

    private static Sound Soundpoints;
    private static Sound Soundexplosion ;
    public static Music musicambient;

    public static void load(){

        //Cargar la nave y el asteroide
        dropNave = new Texture(Gdx.files.internal("nave.png"));
        dropAsteroide = new Texture(Gdx.files.internal("asteroide.png"));
        dropPoints = new Texture(Gdx.files.internal("estrella.png"));
        fondo = new Texture(Gdx.files.internal("fondo.png"));

        // Fons de pantalla
        background = new TextureRegion(fondo, 0, 177, 480, 135);
        background.flip(false, true);


        //Cargar sonido del juego
        //Puntos
        Soundpoints = Gdx.audio.newSound(Gdx.files.internal("Puntos.mp3"));
        //Choque
        Soundexplosion = Gdx.audio.newSound(Gdx.files.internal("choques.mp3"));
        musicambient = Gdx.audio.newMusic(Gdx.files.internal("Ambiente.mp3"));


        // Música del joc
        musicambient = Gdx.audio.newMusic(Gdx.files.internal("Ambiente.mp3"));
        musicambient.setVolume(0.2f);
        musicambient.setLooping(true);

    }

    public static void dispose(){
        dropAsteroide.dispose();
        dropNave.dispose();
        Soundpoints.dispose();
        Soundexplosion.dispose();
        musicambient.dispose();
    }
}
