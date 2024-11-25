package blackjack.Client.Exceptions;

/**
 * UnsuccessfulConnectionException occurs when a connection cannot be made
 */
public class InvalidGameStateException extends Exception {

    public InvalidGameStateException() {
        super();
    }
    
    public InvalidGameStateException(String msg) {
        super(msg);
    }
}
