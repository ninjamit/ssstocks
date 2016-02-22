package njm.ssstocks.statistics;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import njm.ssstocks.caches.StockCache;
import njm.ssstocks.caches.TradeCache;
import njm.ssstocks.stocks.model.Stock;
import njm.ssstocks.trading.model.TradeData;

/**
 * Statistics provides derivations for stocks based on the trades carried out 
 * over a given period of time.
 */
@Component
public class Statistics {

	/**
	 * The cache of known stock definitions.
	 */
	@Getter @Setter(onMethod=@__({@Required, @Autowired})) private StockCache stockCache;
	
	/**
	 * The trades recorded during the lifetime of the app.
	 */
	@Getter @Setter(onMethod=@__({@Required, @Autowired})) private TradeCache tradeStore;

	/**
	 * Calculates the Volume Weighted Stock Price (VWSP) for trades on the given stock
	 * over the last 15 minutes.  
	 * 
	 * @param stockSymbol The stock symbol for this the VWSP is required.
	 * @return the volume weighted stock price.
	 */
	public BigDecimal calculateVolumeWeightedStockPrice(String stockSymbol) {
		return calculateVolumeWeightedStockPrice(stockSymbol, Duration.ofMinutes(15L));
	}

	/**
	 * Calculates the Volume Weighted Stock Price (VWSP) for trades on the given 
	 * stock over the specified duration.
	 * 
	 * @param stockSymbol The stock symbol for this the VWSP is required.
	 * @param timePeriod The duration specifying the time period covered. 
	 * @return the volume weighted stock price.
	 */
	public BigDecimal calculateVolumeWeightedStockPrice(@NonNull String stockSymbol, Duration timePeriod) {
		
		if (!stockCache.containsKey(stockSymbol)) {
			throw new IllegalArgumentException("Unknown stock Symbol "+stockSymbol);
		}
		
		Stock stock = stockCache.get(stockSymbol);
		
		OffsetDateTime start = OffsetDateTime.now().minus(timePeriod);

		Collection<TradeData> applicableTrades =  
			tradeStore.entrySet().parallelStream()
				.filter(e -> e.getKey().getSymbol().equals(stock.getSymbol()) 
						&& (timePeriod.isZero() || e.getKey().getTimestamp().isAfter(start)))
				.map(e -> e.getValue())
				.collect(Collectors.toList());
		
		int totalQty = 0;
		BigDecimal totalPrice = BigDecimal.ZERO;
		
		// use a single loop rather than reductions due to multiple outputs required
		for (TradeData t : applicableTrades) {
			totalQty += t.getQuantity();
			totalPrice = totalPrice.add(t.getTradePrice().multiply(new BigDecimal(t.getQuantity().toString())));
		}
		
		BigDecimal result = BigDecimal.ZERO;
		
		if (totalQty != 0) {
			result = totalPrice.divide(new BigDecimal(totalQty));
		}

		return result;
	}
	
	/**
	 * "Calculate the GBCE All Share Index using the geometric mean of prices for all stocks"
	 * Assumption: The prices mentioned refers to the volume-weighted stock price (VWSP).  We 
	 * therefore determine all non-zero VWSP for each known stock.
	 * <p>
	 * Another option would be to maintain a price ticker (last traded price) for each stock 
	 * and use the that to derive.
	 * 
	 * @return the all share index
	 */
	public BigDecimal calculateAllShareIndex() {
		List<BigDecimal> allWeightedPrices = 
				stockCache.keySet().stream()
				.map(s -> calculateVolumeWeightedStockPrice(s, Duration.ZERO))
				.filter(p -> p.compareTo(BigDecimal.ZERO) != 0)
				.collect(Collectors.toList());
		return calculateGeometricMean(allWeightedPrices);
	}
	
	/**
	 * Utility method to calculate the geometric mean of a collection of <code>Number</code>s
	 * <p>
	 * For the given collection <i>p</i> of <i>n</i> numbers, takes the <i>n<sup>th</sup></i> 
	 * root of the product of the numbers:
	 * <div style="margin:20px">
	 *   <i>geometric mean = (p<sub>1</sub>p<sub>2</sub>...p<sub>n</sub>)<sup>1/n</sup></i>
	 * </div>
	 * 
	 * @param numbers the input collection of Numbers
	 * @return the geometric mean of the input
	 */
	public BigDecimal calculateGeometricMean(@NonNull Collection<? extends Number> numbers) 
	{
		if (numbers.isEmpty()) {
			throw new IllegalArgumentException("Unable to take the geometric mean of an empty collection");
		}
		
		BigDecimal product = numbers.parallelStream()          // safe to parallelise since multiplication is commutative 
				.map(n -> new BigDecimal(n.toString()))        // convert to common type
				.reduce(BigDecimal.ONE, BigDecimal::multiply); // reduce to product
		
		/* 
		 * TODO: implement a BigDecimal nth-root calc, as-is, this implicitly 
		 * limits the product to Double.MAX_VALUE, which I think is suitable 
		 * for the purposes of this exercise.
		 */		
		return new BigDecimal(Math.pow(product.doubleValue(), 1.0d/(double)numbers.size()), MathContext.DECIMAL64);
	}
	
}
