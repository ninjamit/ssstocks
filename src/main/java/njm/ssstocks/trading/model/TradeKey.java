package njm.ssstocks.trading.model;

import java.time.OffsetDateTime;

import lombok.NonNull;
import lombok.Value;

/**
 * Immutable class to act as a key in maps
 */
@Value 
public class TradeKey {
	@NonNull private final String tradeId;
	@NonNull private final String symbol;
	@NonNull private final OffsetDateTime timestamp;
}
