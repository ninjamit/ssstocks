package njm.ssstocks.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.java.Log;
import njm.ssstocks.caches.StockCache;
import njm.ssstocks.caches.TradeCache;
import njm.ssstocks.statistics.Statistics;
import njm.ssstocks.stocks.StockCalc;
import njm.ssstocks.trading.TradeHandler;
import njm.ssstocks.trading.model.TradeData;

/**
 * Implementation of the {@link SimpleStocksService} that provides the ability 
 * for a number of {@link TradeHandler}s to be invoked for each call to 
 * {@link #processTrade}
 */
@Log
@Service
public class SimpleStocksServiceImpl implements SimpleStocksService {

	@Autowired @NonNull @Getter @Setter private List<TradeHandler> tradeHandlers;
	@Autowired @NonNull @Getter @Setter private Statistics statistics;
	@Autowired @NonNull @Getter @Setter private StockCalc stockCalc;
	@Autowired @NonNull @Getter @Setter private StockCache stockCache;
	@Autowired @NonNull @Getter @Setter private TradeCache tradeStore;
	
	/**
	 * @see SimpleStocksService#processTrade(TradeData)
	 */
	@Override
	public boolean processTrade(TradeData trade) {
		boolean success = false;
		try {
			for(TradeHandler handler : tradeHandlers) {
				handler.handleTrade(trade);
			}
			success = true;
		} catch (Exception e) {
			log.log(Level.WARNING, String.format("Failed to process trade %s for symbol %s", trade.getTradeId(), trade.getStockSymbol()), e);
		}
		return success;
	}

	/**
	 * @see SimpleStocksService#calculateDividendYield(String, BigDecimal)
	 */
	@Override
	public BigDecimal calculateDividendYield(String stockSymbol, BigDecimal marketPrice) {
		if (!stockCache.containsKey(stockSymbol)) {
			throw new IllegalArgumentException("Unknown stockSymbol");
		}
		return stockCalc.calculateDividendYield(stockCache.get(stockSymbol), marketPrice);
	}

	/**
	 * @see SimpleStocksService#calculatePeRatio(String, BigDecimal)
	 */
	@Override
	public BigDecimal calculatePeRatio(String stockSymbol, BigDecimal marketPrice) {
		if (!stockCache.containsKey(stockSymbol)) {
			throw new IllegalArgumentException("Unknown stockSymbol");
		}
		return stockCalc.calculatePeRatio(stockCache.get(stockSymbol), marketPrice);
	}

	/**
	 * @see SimpleStocksService#calculateVolumeWeightedStockPrice(String)
	 */
	@Override
	public BigDecimal calculateVolumeWeightedStockPrice(String stockSymbol) {
		return statistics.calculateVolumeWeightedStockPrice(stockSymbol, Duration.ofMinutes(15L));
	}

	/**
	 * @see SimpleStocksService#calculateGbceAllShareIndex()
	 */
	@Override
	public BigDecimal calculateGbceAllShareIndex() {
		return statistics.calculateAllShareIndex();
	}

}
