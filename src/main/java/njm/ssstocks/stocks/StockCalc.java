package njm.ssstocks.stocks;

import java.math.BigDecimal;
import java.math.MathContext;

import org.springframework.stereotype.Component;

import lombok.NonNull;
import njm.ssstocks.stocks.model.Stock;

/**
 * Utility class for calculating stock-specific values.
 *
 */
@Component
public class StockCalc {

	/**
	 * Given a stock and a market price, will calculate the dividend yield 
	 * based on the stock's calculated dividend.
	 * 
	 * @param stock       the stock for which to perform the calculation
	 * @param marketPrice the market price
	 * @return the dividend yield
	 */
	public BigDecimal calculateDividendYield(@NonNull Stock stock, @NonNull BigDecimal marketPrice) {
		if (marketPrice.compareTo(BigDecimal.ZERO) == 0) {
			throw new IllegalArgumentException("Cannot determine dividend yield for zero price.");
		}
		return stock.calculateDividend().divide(marketPrice, MathContext.DECIMAL64);
	}

	/**
	 * Given a stock and a market price, will calculate the P/E ratio
	 * based on the stock's calculated dividend.
	 * 
	 * @param stock       the stock for which to perform the calculation
	 * @param marketPrice the market price
	 * @return the dividend yield
	 */
	public BigDecimal calculatePeRatio(@NonNull Stock stock, @NonNull BigDecimal marketPrice) {
		if (stock.calculateDividend().compareTo(BigDecimal.ZERO) == 0) {
			throw new RuntimeException("Stock dividend is zero. Cannot determine P/E ratio.");
		}
		return marketPrice.divide(stock.calculateDividend(), MathContext.DECIMAL64);
	}

}
