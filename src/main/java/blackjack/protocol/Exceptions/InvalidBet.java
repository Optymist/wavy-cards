package blackjack.protocol.Exceptions;

/**
 * InvalidBet exception
 */
public class InvalidBet extends Exception {
    public InvalidBet() {
        super();
    }

    public InvalidBet(String message) {
        super(message);
    }
}
