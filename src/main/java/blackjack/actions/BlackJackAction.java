package blackjack.actions;

import blackjack.Play;
import blackjack.player.Hand;
import blackjack.player.Player;
import blackjack.protocol.Exceptions.InvalidAction;

import java.util.Objects;


public abstract class BlackJackAction {
    protected String actionName;

    public BlackJackAction(String actionName) {
        this.actionName = actionName;
    }

    public abstract void execute(Hand playingHand, Player player, Play game);

    public String getActionName() {
        return actionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlackJackAction that = (BlackJackAction) o;
        return Objects.equals(actionName, that.actionName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(actionName);
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
