package njm.ssstocks.stocks.model;

import java.math.BigDecimal;

/**
 * Represents a common stock. 
 */
public class CommonStock extends Stock {
	public CommonStock(String symbol, BigDecimal lastDividend, BigDecimal parValue) {
		super(symbol, lastDividend, parValue);
	}	

	/**
	 * The dividend for a common stock is the last dividend allocated for the stock.
	 */
	@Override
	public BigDecimal calculateDividend() {
		return getLastDividend();
	}
}
