package njm.ssstocks.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import lombok.val;
import njm.ssstocks.controllers.TradingController;
import njm.ssstocks.service.SimpleStocksService;
import njm.ssstocks.trading.model.BuySellIndicator;
import njm.ssstocks.trading.model.TradeData;

@RunWith(MockitoJUnitRunner.class)
public class TradingControllerTest {

	@Mock SimpleStocksService mockService;
	
	@InjectMocks TradingController target;

	ArgumentCaptor<TradeData> captor = ArgumentCaptor.forClass(TradeData.class);

	@Test
	public void testProcessTrade_TradeDataConstruction() {
		when(mockService.processTrade(any(TradeData.class))).thenReturn(true);
		val actual = target.processTrade("id", "symbol", 100, BuySellIndicator.BUY, new BigDecimal("123.456"));
		
		assertThat(actual.getStatus(), is("SUCCESS"));
		
		verify(mockService).processTrade(captor.capture());
		TradeData actualTradeData = captor.getValue();
		assertEquals("id", actualTradeData.getTradeId());
		assertEquals("symbol", actualTradeData.getStockSymbol());
		assertEquals(new Integer(100), actualTradeData.getQuantity());
		assertEquals(BuySellIndicator.BUY, actualTradeData.getBuySell());
		assertEquals(new BigDecimal("123.456"), actualTradeData.getTradePrice());
		assertNotNull(actualTradeData.getTradeTime());	
	}
	@Test
	public void testProcessTrade_success() {
		when(mockService.processTrade(any(TradeData.class))).thenReturn(true);
		val actual = target.processTrade("id", "symbol", 100, BuySellIndicator.BUY, new BigDecimal("123.456"));
		
		assertThat(actual.getStatus(), is("SUCCESS"));
		
		verify(mockService).processTrade(any(TradeData.class));
	}
	
	@Test
	public void testProcessTrade_failure() {
		when(mockService.processTrade(any(TradeData.class))).thenReturn(false);
		val actual = target.processTrade("id", "symbol", 100, BuySellIndicator.BUY, new BigDecimal("123.456"));
		
		assertThat(actual.getStatus(), is("FAILURE"));
		
		verify(mockService).processTrade(any(TradeData.class));
	}
	
	@Test
	public void testProcessTrade_exception() {
		when(mockService.processTrade(any(TradeData.class))).thenThrow(new RuntimeException("dummy"));
		val actual = target.processTrade("id", "symbol", 100, BuySellIndicator.BUY, new BigDecimal("123.456"));

		assertThat(actual.getStatus(), is("ERROR"));
		assertThat(actual.getDetail(), containsString("dummy"));

		verify(mockService).processTrade(any(TradeData.class));
	}

}
