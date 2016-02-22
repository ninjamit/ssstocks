package njm.ssstocks.trading;

import static njm.ssstocks.trading.model.BuySellIndicator.BUY;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import njm.ssstocks.caches.StockCache;
import njm.ssstocks.caches.TradeCache;
import njm.ssstocks.trading.RecordingTradeHandler;
import njm.ssstocks.trading.exceptions.InvalidTradeException;
import njm.ssstocks.trading.exceptions.TradeProcessingException;
import njm.ssstocks.trading.model.TradeData;
import njm.ssstocks.trading.model.TradeKey;

@RunWith(MockitoJUnitRunner.class)
public class RecordingTradeHandlerTest {

	@Mock StockCache mockStockCache;
	@Spy TradeCache mockTradeStore = new TradeCache();
	
	RecordingTradeHandler target;
	
	@Before
	public void setup() {
		target = new RecordingTradeHandler();
		
		target.setStockCache(mockStockCache);
		target.setTradeStore(mockTradeStore);
	}
	
	@Test(expected=TradeProcessingException.class)
	public void handleTrade_stockNotAvailable() throws InvalidTradeException, TradeProcessingException {
		when(mockStockCache.containsKey("STOCK")).thenReturn(false);
		target.handleTrade(new TradeData("testtrade1", "NOTAVAILABLE", OffsetDateTime.now(), 100, BUY, new BigDecimal("100")));
		verify(mockStockCache).containsKey("NOTAVAILABLE");
		verifyZeroInteractions(mockTradeStore);
	}
	
	@Test
	public void handleTrade_stockAvailable() throws InvalidTradeException, TradeProcessingException {
		when(mockStockCache.containsKey("STOCK")).thenReturn(true);
		OffsetDateTime tradeTime = OffsetDateTime.now();
		
		TradeData trade = new TradeData("testtrade1", "STOCK", tradeTime, 100, BUY, new BigDecimal("100"));
		
		target.handleTrade(trade);
		
		TradeKey expectedKey = new TradeKey("testtrade1", "STOCK", tradeTime);
		
		verify(mockStockCache).containsKey("STOCK");
		assertThat(mockTradeStore.keySet(), hasItem(expectedKey));
		assertThat(mockTradeStore.get(expectedKey), is(trade));
	}
}
