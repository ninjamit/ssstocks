package njm.ssstocks.stocks.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Base Stock abstraction representing stock attributes
 */
@Data 
@RequiredArgsConstructor
public abstract class Stock {

	@NonNull private final String symbol;
	@NonNull private final BigDecimal lastDividend;
	@NonNull private final BigDecimal parValue;
	
	/**
	 * The appropriate
	 * @return the dividend for the stock.
	 */
	public abstract BigDecimal calculateDividend();
	
}
