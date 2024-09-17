package blackjack.Client.Gui.Util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Atlas is class that will load an Atlas stored in the resource folder.
 * It is small and has little functionality as well as poor performance.
 * For better long-life programability, use Preloaded Atlas.
 */
public class Atlas {

    private BufferedImage atlas;

    public Atlas(String path) throws IOException {
        atlas = ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));
        
    }

    public BufferedImage getSprite(int x, int y, int width, int height) {
        if (0 <= x && x + width <= atlas.getWidth() && 0 <= y && y + height <= atlas.getHeight()) {
            return atlas.getSubimage(x, y, width, height);
        } else {

            throw new IllegalArgumentException("Sprite out side of atlas");
        }
    }
}
