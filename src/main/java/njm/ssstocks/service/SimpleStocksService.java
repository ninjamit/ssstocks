package njm.ssstocks.service;

import java.math.BigDecimal;

import njm.ssstocks.trading.model.TradeData;

/**
 * A single service for wrapping all the functionality exposed by the application 
 */
public interface SimpleStocksService {
	
	/**
	 * Processes a trade described by the given <code>TradeData</code> 
	 * @param trade
	 * @return boolean indicating success or failure
	 */
	boolean processTrade(TradeData trade);

	/**
	 * Given a stock symbol and a market price, will calculate the dividend yield 
	 * based on the stock's calculated dividend.
	 * 
	 * @param symbol      the symbol identifying the stock
	 * @param marketPrice the market price
	 * @return the dividend yield
	 */
	BigDecimal calculateDividendYield(String stockSymbol, BigDecimal marketPrice);

	/**
	 * Given a stock symbol and a market price, will calculate the P/E ratio
	 * based on the stock's calculated dividend.
	 * 
	 * @param symbol      the symbol identifying the stock
	 * @param marketPrice the market price
	 * @return the dividend yield
	 */
	BigDecimal calculatePeRatio(String stockSymbol, BigDecimal marketPrice);

	/**
	 * Calculates the Volume Weighted Stock Price (VWSP) for trades on the given stock
	 * over the last 15 minutes.  
	 * 
	 * @param stockSymbol The stock symbol for this the VWSP is required.
	 * @return the volume weighted stock price.
	 */
	BigDecimal calculateVolumeWeightedStockPrice(String stockSymbol);
	
	/**
	 * Calculate the GBCE All Share Index using the geometric mean of prices for all stocks
	 * 
	 * @return the all share index
	 */
	BigDecimal calculateGbceAllShareIndex();
	
}
