package njm.ssstocks.trading.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.NonNull;
import lombok.Value;

/**
 * Immutable class to represent an individual trade.
 */
@Value 
public class TradeData {

	@NonNull private final String tradeId;
	@NonNull private final String stockSymbol;
	@NonNull private final OffsetDateTime tradeTime;
	@NonNull private final Integer quantity;
	@NonNull private final BuySellIndicator buySell;
	@NonNull private final BigDecimal tradePrice;
	
}
