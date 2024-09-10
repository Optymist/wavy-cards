package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;


public abstract class BlackJackAction {
    protected String actionName;

    public BlackJackAction(String actionName) {
        this.actionName = actionName;
    }

    public abstract void execute(Player player, Play game);

    public String getActionName() {
        return actionName;
    }
}
