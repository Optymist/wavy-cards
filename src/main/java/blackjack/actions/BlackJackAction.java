package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;

import java.util.ArrayList;

public abstract class BlackJackAction {
    private String[] validActions = {"hit", "stand", "double", "split", "surrender"};
    protected String actionName;

    public BlackJackAction(String actionName) {
        this.actionName = actionName;
    }

    public abstract void execute(Player player, Play game);

    public String getActionName() {
        return actionName;
    }
}
