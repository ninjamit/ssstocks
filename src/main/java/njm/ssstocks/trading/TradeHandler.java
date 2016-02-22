package njm.ssstocks.trading;

import njm.ssstocks.trading.exceptions.InvalidTradeException;
import njm.ssstocks.trading.exceptions.TradeProcessingException;
import njm.ssstocks.trading.model.TradeData;

/**
 * An interface to be used for all trade handling activities.
 */
public interface TradeHandler {

	/**
	 * Handles the specified trade
	 * @param trade a <code>TradeData</code> encapsulating the trade details
	 * @throws InvalidTradeException if the trade is invalid
	 * @throws TradeProcessingException if an issue occurs
	 */
	public void handleTrade(TradeData trade) throws InvalidTradeException, TradeProcessingException;
	
}
