package blackjack.Client.Gui.GameState;

import java.awt.Graphics;

import blackjack.Client.Gui.Panels.MenuPanel;

/**
 * BetRequestState
 */
public class TurnRequestState extends GameState {

    private final String[] actions;

    public TurnRequestState(String[] actions) {
        super();
        this.actions = actions;
    }

	@Override
	public void draw(Graphics g) {

	}

	@Override
	public void initComponents(MenuPanel panel) {
	}
    
}
