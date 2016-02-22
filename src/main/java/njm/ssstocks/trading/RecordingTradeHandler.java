package njm.ssstocks.trading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import njm.ssstocks.caches.StockCache;
import njm.ssstocks.caches.TradeCache;
import njm.ssstocks.trading.exceptions.InvalidTradeException;
import njm.ssstocks.trading.exceptions.TradeProcessingException;
import njm.ssstocks.trading.model.TradeData;
import njm.ssstocks.trading.model.TradeKey;

/**
 * A trade handler that performs basic validation upon the given trade and 
 * persists in the trade store.
 */
@Log
@Component
public class RecordingTradeHandler implements TradeHandler {

	@Getter @Setter(onMethod=@__({@Required, @Autowired})) 
	private StockCache stockCache;
	@Getter @Setter(onMethod=@__({@Required, @Autowired})) 
	private TradeCache tradeStore;

	/**
	 * Confirms the stock associated with the trade is known and adds the trade to the trade store.
	 * @param the TradeData encapsulating the trade to be handled
	 * @return the status of the trade
	 */
	@Override
	public void handleTrade(TradeData trade) 
			throws InvalidTradeException, TradeProcessingException 
	{
		if (trade == null) {
			throw new InvalidTradeException("null trades cannot be recorded.");
		}
		// don't need to do non-null validation on trade details due to the TradeData definition 
		// and it's immutable nature.
		log.info(String.format("Recording trade: %s", trade.toString()));
		
		if (!stockCache.containsKey(trade.getStockSymbol())) {
			throw new TradeProcessingException(
					String.format("Stock symbol %s not found in stock cache", 
							      trade.getStockSymbol()));
		}
	
		TradeKey key = new TradeKey(trade.getTradeId(), trade.getStockSymbol(), trade.getTradeTime());
		
		tradeStore.putIfAbsent(key, trade);
	} 
}
