package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {

    public static Texture dropAsteroide;
    public static Texture dropNave;
    public static Texture dropPoints;
    public static Texture fondo;

    public static TextureRegion background;

    public static Sound Soundpoints;
    public static Sound Soundexplosion ;
    public static Music musicambient;
    public static Sound muerte;

    public  static BitmapFont font ;

    public static void load(){

        //Cargar la nave y el asteroide
        dropNave = new Texture(Gdx.files.internal("nave.png"));
        dropAsteroide = new Texture(Gdx.files.internal("asteroide.png"));
        dropPoints = new Texture(Gdx.files.internal("estrella.png"));
        fondo = new Texture(Gdx.files.internal("fondo.jpg"));
/*
        // Fons de pantalla
        background = new TextureRegion(fondo, 0, 177, 800, 480);
        background.flip(false, true);*/


        //Cargar sonido del juego
        //Puntos
        Soundpoints = Gdx.audio.newSound(Gdx.files.internal("Puntos.mp3"));

        //Choque
        Soundexplosion = Gdx.audio.newSound(Gdx.files.internal("choques.mp3"));

        // Música del joc
        musicambient = Gdx.audio.newMusic(Gdx.files.internal("Ambiente.mp3"));
        musicambient.setVolume(1.0f);
        musicambient.setLooping(true);

        //Sonido Muerte
        muerte = Gdx.audio.newSound(Gdx.files.internal("Muerte.mp3"));
        musicambient.setVolume(1.0f);
        musicambient.setLooping(true);

        // Font space
        FileHandle fontFile = Gdx.files.internal("fonts/space.fnt");
        font = new BitmapFont(fontFile, true);
        font.getData().setScale(0.4f);

    }

    public static void dispose(){
        dropAsteroide.dispose();
        dropNave.dispose();
        dropPoints.dispose();
        Soundpoints.dispose();
        Soundexplosion.dispose();
        musicambient.dispose();
    }
}
