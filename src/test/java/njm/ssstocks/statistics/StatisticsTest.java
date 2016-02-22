package njm.ssstocks.statistics;

import static njm.ssstocks.trading.model.BuySellIndicator.BUY;
import static njm.ssstocks.trading.model.BuySellIndicator.SELL;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Spy;

import lombok.val;
import njm.ssstocks.caches.StockCache;
import njm.ssstocks.caches.TradeCache;
import njm.ssstocks.stocks.model.CommonStock;
import njm.ssstocks.stocks.model.PreferredStock;
import njm.ssstocks.trading.model.TradeData;
import njm.ssstocks.trading.model.TradeKey;

public class StatisticsTest {

	@Spy private StockCache mockStockCache = new StockCache();
    @Spy private TradeCache mockTradeStore = new TradeCache();
	
	@Rule public ExpectedException thrown = ExpectedException.none();

	private Statistics target;
	
	@Before
	public void setup() {
		target = new Statistics();
		target.setStockCache(mockStockCache);
		target.setTradeStore(mockTradeStore);

		setupMockStocks();
		setupMockTrades();
	}

	private void setupMockStocks() {
		mockStockCache.put("TEA", new CommonStock("TEA", BigDecimal.ZERO, new BigDecimal("100")));
		mockStockCache.put("POP", new CommonStock("POP", new BigDecimal("8"), new BigDecimal("100")));
		mockStockCache.put("ALE", new CommonStock("ALE", new BigDecimal("23"), new BigDecimal("60")));
		mockStockCache.put("GIN", new PreferredStock("GIN", new BigDecimal("23"), new BigDecimal("60"), new BigDecimal("2")));
		mockStockCache.put("JOE", new CommonStock("JOE", new BigDecimal("13"), new BigDecimal("250")));
	}

	private void setupMockTrades() {
		storeTrade(new TradeData("1", "TEA", OffsetDateTime.now().minusMinutes(5), 3, BUY, new BigDecimal("99")));
		storeTrade(new TradeData("2", "TEA", OffsetDateTime.now().minusMinutes(14), 3, BUY, new BigDecimal("101")));
		storeTrade(new TradeData("3", "TEA", OffsetDateTime.now().minusMinutes(16), 6, BUY, new BigDecimal("110")));
		storeTrade(new TradeData("4", "POP", OffsetDateTime.now().minusMinutes(6), 10, BUY, new BigDecimal("101")));
		storeTrade(new TradeData("5", "POP", OffsetDateTime.now().minusMinutes(16), 10, SELL, new BigDecimal("99")));
	}
	
	private void storeTrade(TradeData trade) {
		TradeKey key = new TradeKey(trade.getTradeId(), trade.getStockSymbol(), trade.getTradeTime());
		mockTradeStore.put(key,  trade);
	}
	
	@Test
	public void calculateVolumeWeightedStockPrice_invalidStock() {
		thrown.expect(IllegalArgumentException.class);
		target.calculateVolumeWeightedStockPrice("N/A");
	}
	
	@Test
	public void calculateVolumeWeightedStockPrice_defaultDuration() {
		BigDecimal actual = 
				target.calculateVolumeWeightedStockPrice("TEA");
		assertThat(actual.compareTo(new BigDecimal("100.0")), is(0));
	}
	
	@Test
	public void calculateVolumeWeightedStockPrice_10minutes() {
		BigDecimal actual = 
				target.calculateVolumeWeightedStockPrice("TEA", Duration.ofMinutes(10));
		assertThat(actual.compareTo(new BigDecimal("99.0")), is(0));
	}
	
	@Test
	public void calculateVolumeWeightedStockPrice_20minutes() {
		BigDecimal actual = 
				target.calculateVolumeWeightedStockPrice("TEA", Duration.ofMinutes(20));
		assertThat(actual.compareTo(new BigDecimal("105.0")), is(0));
	}

	
	@Test
	public void calculateGeometricMean_normal() {
		List<Number> input = Arrays.asList(1, 2.0d, 4l, new BigDecimal("2.0"), new BigInteger("2"));
		val actual = target.calculateGeometricMean(input);
		assertThat(actual.compareTo(new BigDecimal("2.0")), is(0));
	}
	
	@Test
	public void calculateGeometricMean_nullList() {
		thrown.expect(NullPointerException.class);
		target.calculateGeometricMean(null);
	}
	
	@Test
	public void calculateGeometricMean_emptyList() {
		thrown.expect(IllegalArgumentException.class);
		target.calculateGeometricMean(new ArrayList<>());
	}
	
	@Test(expected=NumberFormatException.class)
	public void calculateGeometricMean_tooLargeForDouble() {
		List<Number> input = Arrays.asList(Double.MAX_VALUE, Double.MAX_VALUE);
		target.calculateGeometricMean(input);
	}
	
	@Test
	public void calculateAllShareIndex() {
		val actual = target.calculateAllShareIndex();
		BigDecimal expected = new BigDecimal(Math.pow(10500.0d, 0.5d), MathContext.DECIMAL64);
		assertThat(actual.compareTo(expected), is(0));
	}
}
