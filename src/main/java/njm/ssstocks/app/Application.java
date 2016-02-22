package njm.ssstocks.app;

import java.math.BigDecimal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import njm.ssstocks.caches.StockCache;
import njm.ssstocks.stocks.model.CommonStock;
import njm.ssstocks.stocks.model.PreferredStock;

/**
 * The Spring Boot wrapper that starts the application. 
 */
@SpringBootApplication(scanBasePackages="njm.ssstocks")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	/**
	 * Define some default stocks.  In real life, this would be cached from some 
	 * reference source.
	 */
	@Bean
	public StockCache stockCache() {
		StockCache cache = new StockCache();

		cache.put("TEA", new CommonStock("TEA", BigDecimal.ZERO, new BigDecimal("100")));
		cache.put("POP", new CommonStock("POP", new BigDecimal("8"), new BigDecimal("100")));
		cache.put("ALE", new CommonStock("ALE", new BigDecimal("23"), new BigDecimal("60")));
		cache.put("GIN", new PreferredStock("GIN", new BigDecimal("23"), new BigDecimal("60"), new BigDecimal("2")));
		cache.put("JOE", new CommonStock("JOE", new BigDecimal("13"), new BigDecimal("250")));
		
		return cache;
	}
}
