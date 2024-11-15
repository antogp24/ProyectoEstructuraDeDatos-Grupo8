package ec.edu.espol;

import java.io.Serializable;

public class Foto implements Serializable {
    String path;
    int[] pixels;
    int width;
    int height;
    
    public Foto(String path) {
        this.path = path;
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
