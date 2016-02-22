package njm.ssstocks.trading.exceptions;

/**
 * Exception indicating that a given trade is invalid
 */
public class InvalidTradeException extends Exception {

	public InvalidTradeException(String string) {
		super(string);
	}

	public InvalidTradeException(Throwable cause) {
		super(cause);
	}

	public InvalidTradeException(String string, Throwable cause) {
		super(string, cause);
	}

	private static final long serialVersionUID = 4591781523054668157L;

}
