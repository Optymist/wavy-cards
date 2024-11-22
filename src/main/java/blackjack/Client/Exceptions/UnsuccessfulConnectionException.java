package blackjack.Client.Exceptions;

/**
 * UnsuccessfulConnectionException occurs when a connection cannot be made
 */
public class UnsuccessfulConnectionException extends Exception {

    public UnsuccessfulConnectionException() {
        super();
    }
    
    public UnsuccessfulConnectionException(String msg) {
        super(msg);
    }
}
