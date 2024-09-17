package blackjack.actions;

import blackjack.Play;
import blackjack.player.Player;
import blackjack.protocol.Exceptions.InvalidAction;


public abstract class BlackJackAction {
    protected String actionName;

    public BlackJackAction(String actionName) {
        this.actionName = actionName;
    }

    public abstract void execute(Player player, Play game);

    public String getActionName() {
        return actionName;
    }

    public static BlackJackAction create(String actionName) throws InvalidAction {
        return switch (actionName) {
            case "double" -> new DoubleAction();
            case "hit" -> new HitAction();
            case "split" -> new SplitAction();
            case "stand" -> new StandAction();
            case "surrender" -> new SurrenderAction();
            default -> throw new InvalidAction(actionName + " is not a valid action.");
        };
    }
}
