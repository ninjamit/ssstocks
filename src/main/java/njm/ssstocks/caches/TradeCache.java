package njm.ssstocks.caches;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import njm.ssstocks.trading.model.TradeData;
import njm.ssstocks.trading.model.TradeKey;

/**
 * In real scenario would use whatever caching solution is required and hide behind an interface.  
 * Sticking with ConcurrentHashMap for simplicity for this example.
 */
@Component
public class TradeCache extends ConcurrentHashMap<TradeKey, TradeData>{

	private static final long serialVersionUID = 4015105007819772717L;

}
