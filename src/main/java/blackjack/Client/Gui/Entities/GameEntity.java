package blackjack.Client.Gui.Entities;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import blackjack.Client.Gui.Panels.*;

/**
 * GameComponents
 */
public class GameEntity extends Component {

    private BufferedImage image = null;

    private int x;
    private int y;
    private int width;
    private int height;
    private GameEntityType type;


    public GameEntity(int[] position, int[] dimensions) {
        super();
        this.x = position[0];
        this.y = position[1];
        this.width = dimensions[0];
        this.height = dimensions[1];
        this.type = GameEntityType.RECT;
    }

    public GameEntity(int[] position, int[] dimensions, BufferedImage image) {
        super();
        this.x = position[0];
        this.y = position[1];
        this.width = dimensions[0];
        this.height = dimensions[1];
        this.image = image;
        this.type = GameEntityType.IMAGE;
    }

    /**
     * Will draw entity using Graphics on Panel provided.
     *
     * @param g
     * @param panel
     */
    public void draw(Graphics g, GamePanel panel) {
        g.drawImage(image, x, y, panel);
    }

    public int[] position() {
        return new int[] { x, y };
    }

    public void x(int x) {
        this.x = x;
    }

    public void y(int y) {
        this.y = y;
    }

    public int y() {
        return y;
    }

    public int x() {
        return x;
    }

    public int height() {
        return height;
    }

    public void height(int newHeight) {
        this.height = newHeight;
    }

    public void width(int newWidth) {
        this.width = newWidth;
    }

    public int width() {
        return width;
    }

    public void image(BufferedImage newImage) {
        this.image = newImage;
    }

    public BufferedImage image() {
        return image;
    }

    public GameEntityType type() {
        return type;
    }

}
