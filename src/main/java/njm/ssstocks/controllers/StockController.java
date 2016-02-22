package njm.ssstocks.controllers;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import njm.ssstocks.service.SimpleStocksService;

/**
 * a REST controller exposing Stock-specific calculations.
 */
@RestController
public class StockController {
	
	/**
	 * The service to which processing will be delegated.
	 */
	@Getter @Setter(onMethod=@__({@Required, @Autowired})) private SimpleStocksService service;

	/**
	 * Given a stock symbol and a market price, will calculate the dividend yield 
	 * based on the stock's calculated dividend.
	 * 
	 * @param symbol      the symbol identifying the stock
	 * @param marketPrice the market price
	 * @return the dividend yield
	 */
	@RequestMapping("/calculateDividendYield")
	public BigDecimal calculateDividendYield(@NonNull String symbol, BigDecimal marketPrice) {
		return service.calculateDividendYield(symbol, marketPrice);
	}

	/**
	 * Given a stock symbol and a market price, will calculate the P/E ratio
	 * based on the stock's calculated dividend.
	 * 
	 * @param symbol      the symbol identifying the stock
	 * @param marketPrice the market price
	 * @return the dividend yield
	 */
	@RequestMapping("/calculatePeRatio")
	public BigDecimal calculatePeRatio(String symbol, BigDecimal marketPrice) {
		return service.calculatePeRatio(symbol, marketPrice);
	}

	/**
	 * Calculates the Volume Weighted Stock Price (VWSP) for trades on the given stock
	 * over the last 15 minutes.  
	 * 
	 * @param stockSymbol The stock symbol for this the VWSP is required.
	 * @return the volume weighted stock price.
	 */
	@RequestMapping("/calculateVWSP")
	public BigDecimal calculateVolumeWeightedStockPrice(String symbol) {
		return service.calculateVolumeWeightedStockPrice(symbol);
	}
	
	/**
	 * Calculate the GBCE All Share Index using the geometric mean of prices for all stocks
	 * 
	 * @return the all share index
	 */
	@RequestMapping("/calculateGbceAllShareIndex")
	public BigDecimal calculateGbceAllShareIndex() {
		return service.calculateGbceAllShareIndex();
	}
}
