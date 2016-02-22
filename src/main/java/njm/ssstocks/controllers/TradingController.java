package njm.ssstocks.controllers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Value;
import lombok.extern.java.Log;
import njm.ssstocks.service.SimpleStocksService;
import njm.ssstocks.trading.model.BuySellIndicator;
import njm.ssstocks.trading.model.TradeData;

/**
 * A REST controller exposing the ability to process a trade
 */
@Log
@RestController
public class TradingController {

	@Value
	public class TradeResponse {
		@NonNull private String status;
		private String detail;
	}
	
	/**
	 * The service to which processing will be delegated.
	 */
	@Getter @Setter(onMethod=@__({@Required, @Autowired})) private SimpleStocksService service;
	
	/**
	 * Processes a trade described by the given parameters.
	 * @param id      the unique trade id
	 * @param symbol  the stock symbol
	 * @param qty     the quantity of the stock to be traded
	 * @param buySell whether or not the trade is a BUY or a SELL
	 * @param price   the price at which the stock will be traded
	 * @return a <code>TradeResponse</code> indicating the success or failure of the call
	 */
	@RequestMapping("/doTrade")
	public TradeResponse processTrade(String id, String symbol, Integer qty, BuySellIndicator buySell, BigDecimal price) {
		TradeData trade = new TradeData(id, symbol, OffsetDateTime.now(), qty, buySell, price);
		TradeResponse response;
		try {
			if (service.processTrade(trade)) {
				response = new TradeResponse("SUCCESS", String.format("Trade %s processed successfully.", id));
			} else {
				response = new TradeResponse("FAILURE", String.format("Trade %s was not processed successfully.", id));
			}
		} catch (Exception e) {
			String message = String.format("Failed to process trade %s for symbol %s: %s", id, symbol, e.getMessage());
			response = new TradeResponse("ERROR", message); 
			log.log(Level.WARNING, message, e);
		}
		return response;
	}
}
