package utils;

public class Settings {
    // Mida del joc, s'escalarà segons la necessitat
    public static final int GAME_WIDTH = 240;
    public static final int GAME_HEIGHT = 135;

    // Propietats de la nau
    public static final float NAU_VELOCITY = 50;
    public static final int NAU_WIDTH = 36;
    public static final int NAU_HEIGHT = 15;
    public static final float NAU_STARTX = 20;
    public static final float NAU_STARTY = GAME_HEIGHT/2 - NAU_HEIGHT/2;

    // Rang de valors per canviar la mida de l'asteroide
    public static final float MAX_ASTEROID = 32;
    public static final float MIN_ASTEROID = 32;

    // Configuració scrollable
    public static final int ASTEROID_SPEED = -150;
    public static final int ASTEROID_GAP = 75;
    public static final int BG_SPEED = -100;
}
