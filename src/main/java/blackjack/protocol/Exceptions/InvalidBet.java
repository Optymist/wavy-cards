package blackjack.protocol.Exceptions;

public class InvalidBet extends Exception {
    public InvalidBet() {
        super();
    }

    public InvalidBet(String message) {
        super(message);
    }
}
