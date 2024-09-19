package blackjack.Client.Gui.Panels;


import java.awt.Dimension;
import java.awt.Label;

import javax.swing.JPanel;

/**
 * TitlePanel
 */
public class TitlePanel extends JPanel {

    private final String title;

    public TitlePanel(String title) {
        this(title, new Dimension(800, 400));
    }


    public TitlePanel(String title, Dimension dimension) {
        super();
        this.setSize(dimension);
        this.title = title;
        addTitle();
    }
    

    private void addTitle() {
        Label titleLabel = new Label(title);
        this.add(titleLabel);
    }

    public int[] getMiddle(Dimension panelDimension) {

        return null;
    }

}
