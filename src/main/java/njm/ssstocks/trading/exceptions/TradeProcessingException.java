package njm.ssstocks.trading.exceptions;

/**
 * Exception indicating an issue has occurred during the processing of a trade.
 */
public class TradeProcessingException extends Exception {

	public TradeProcessingException(String string) {
		super(string);
	}

	public TradeProcessingException(Throwable cause) {
		super(cause);
	}

	public TradeProcessingException(String string, Throwable cause) {
		super(string, cause);
	}

	private static final long serialVersionUID = 4544051151878062000L;

}
