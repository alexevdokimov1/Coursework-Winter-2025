package Levels;

import Drawable.*;
import org.joml.Vector3f;

public class Level extends Scene {

    public Level(){
    }

    @Override
    public void update(float dt) {
        for(Drawable each : actors){
            each.draw();
        }
    }

    @Override
    public void init() {
        actors.add(new Waves(0.1f, new Vector3f(0,0,1), true, 1.f));
    }
}