package helpers;

import com.badlogic.gdx.math.Circle;

import objects.Scrollable;
import utils.Methods;

public class Asteroid extends Scrollable {

    private float runTime;
    private Circle collisionCircle;

    public Asteroid(float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, velocity);
        runTime = Methods.randomFloat(0,1);

        // Creem el cercle
        collisionCircle = new Circle();
    }
}
