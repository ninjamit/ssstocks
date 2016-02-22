package njm.ssstocks.caches;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import njm.ssstocks.stocks.model.Stock;

/**
 * In real scenario would use whatever caching solution is required and hide behind an interface. 
 * Sticking with ConcurrentHashMap for simplicity for this example.
 */
@Component
public class StockCache extends ConcurrentHashMap<String, Stock> {

	private static final long serialVersionUID = -1507060843745197098L;

}
