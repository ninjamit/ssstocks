package njm.ssstocks.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.eq;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import njm.ssstocks.caches.StockCache;
import njm.ssstocks.caches.TradeCache;
import njm.ssstocks.service.SimpleStocksServiceImpl;
import njm.ssstocks.statistics.Statistics;
import njm.ssstocks.stocks.StockCalc;
import njm.ssstocks.stocks.model.Stock;
import njm.ssstocks.trading.TradeHandler;
import njm.ssstocks.trading.exceptions.InvalidTradeException;
import njm.ssstocks.trading.model.BuySellIndicator;
import njm.ssstocks.trading.model.TradeData;

@RunWith(MockitoJUnitRunner.class)
public class SimpleStocksServiceImplTest {

	@Mock TradeHandler mockTradeHandler1;
	@Mock TradeHandler mockTradeHandler2;

	@Spy private StockCache mockStockCache = new StockCache();
    @Spy private TradeCache mockTradeStore = new TradeCache();
	@Mock StockCalc mockStockCalc;
	@Mock Statistics mockStatistics;
	
	@InjectMocks SimpleStocksServiceImpl target;
	
	@Mock Stock mockStock;
	
	@Rule public ExpectedException expected = ExpectedException.none();
	
	@Before
	public void setup() {
		target.setTradeHandlers(Arrays.asList(mockTradeHandler1, mockTradeHandler2));
		mockStockCache.put("DUMMY", mockStock);
	}
	
	@Test
	public void testProcessTrade_OK() throws Exception {
		TradeData mockTrade = new TradeData("DUMMY", "DUMMY", OffsetDateTime.now(), 100, BuySellIndicator.BUY, BigDecimal.ONE);
		
		assertTrue(target.processTrade(mockTrade));
		
		verify(mockTradeHandler1).handleTrade(mockTrade);
		verify(mockTradeHandler2).handleTrade(mockTrade);
	}
	
	@Test
	public void testProcessTrade_Exception() throws Exception {
		TradeData mockTrade = new TradeData("DUMMY", "DUMMY", OffsetDateTime.now(), 100, BuySellIndicator.BUY, BigDecimal.ONE);
		
		doThrow(new InvalidTradeException("dummy")).when(mockTradeHandler1).handleTrade(mockTrade);
		
		assertFalse(target.processTrade(mockTrade));
		
		verify(mockTradeHandler1).handleTrade(mockTrade);
		verifyZeroInteractions(mockTradeHandler2);
	}

	@Test
	public void testCalculateDividendYield_OK() {

		String mockStockSymbol = "DUMMY";
		BigDecimal mockPrice = BigDecimal.TEN;
		BigDecimal mockResult =  mock(BigDecimal.class);
		
		when(mockStockCalc.calculateDividendYield(mockStock, mockPrice)).thenReturn(mockResult);
		
		assertThat(target.calculateDividendYield(mockStockSymbol, mockPrice), is(mockResult));
		
		verify(mockStockCalc).calculateDividendYield(mockStock, mockPrice);
	}

	@Test
	public void testCalculateDividendYield_UnknownStock() {
		String mockStockSymbol = "UNKNOWN";
		BigDecimal mockPrice = BigDecimal.TEN;
		
		expected.expect(IllegalArgumentException.class);
		
		target.calculateDividendYield(mockStockSymbol, mockPrice);
		
		verifyZeroInteractions(mockStockCalc);
	}

	@Test
	public void testCalculateDividendYield_Exception() {
		String mockStockSymbol = "DUMMY";
		BigDecimal mockPrice = BigDecimal.ZERO;
		
		when(mockStockCalc.calculateDividendYield(mockStock, mockPrice)).thenThrow(new IllegalArgumentException("ZERO"));
		
		expected.expect(IllegalArgumentException.class);
		
		target.calculateDividendYield(mockStockSymbol, mockPrice);
		
		verify(mockStockCalc).calculateDividendYield(mockStock, mockPrice);
	}

	@Test
	public void testCalculatePeRatio_OK() {

		String mockStockSymbol = "DUMMY";
		BigDecimal mockPrice = BigDecimal.TEN;
		BigDecimal mockResult =  mock(BigDecimal.class);
		
		when(mockStockCalc.calculatePeRatio(mockStock, mockPrice)).thenReturn(mockResult);
		
		assertThat(target.calculatePeRatio(mockStockSymbol, mockPrice), is(mockResult));
		
		verify(mockStockCalc).calculatePeRatio(mockStock, mockPrice);
	}

	@Test
	public void testCalculatePeRatio_UnknownStock() {
		String mockStockSymbol = "UNKNOWN";
		BigDecimal mockPrice = BigDecimal.TEN;
		
		expected.expect(IllegalArgumentException.class);
		
		target.calculatePeRatio(mockStockSymbol, mockPrice);
		
		verifyZeroInteractions(mockStockCalc);
	}

	@Test
	public void testCalculatePeRatio_Exception() {
		String mockStockSymbol = "DUMMY";
		BigDecimal mockPrice = BigDecimal.ZERO;
		
		when(mockStockCalc.calculatePeRatio(mockStock, mockPrice)).thenThrow(new RuntimeException("ZERO"));
		
		expected.expect(RuntimeException.class);
		
		target.calculatePeRatio(mockStockSymbol, mockPrice);
		
		verify(mockStockCalc).calculatePeRatio(mockStock, mockPrice);
	}
	
	@Test
	public void testCalculateVolumeWeightedStockPrice() {
		String mockStockSymbol = "DUMMY";
		target.calculateVolumeWeightedStockPrice(mockStockSymbol);
		
		verify(mockStatistics).calculateVolumeWeightedStockPrice(eq(mockStockSymbol), eq(Duration.ofMinutes(15L)));
	}

	@Test
	public void testCalculateGbceAllShareIndex() {
		target.calculateGbceAllShareIndex();
		
		verify(mockStatistics).calculateAllShareIndex();
	}

}
