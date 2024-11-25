package blackjack.Client.Gui.GameState;

import java.awt.Graphics;

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
    
}
