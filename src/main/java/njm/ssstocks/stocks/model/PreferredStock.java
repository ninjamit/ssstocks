package njm.ssstocks.stocks.model;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a preferred stock type
 */
@EqualsAndHashCode(callSuper=true)
public class PreferredStock extends Stock {

	@Getter @NonNull private final BigDecimal fixedDividend;
	
	private final static BigDecimal ONE_HUNDRED = new BigDecimal("100");
	
	public PreferredStock(String symbol, BigDecimal lastDividend, BigDecimal parValue, @NonNull BigDecimal fixedDividend) {
		super(symbol, lastDividend, parValue);
		this.fixedDividend = fixedDividend;
	}
	
	/**
	 * The dividend for a preferred stock is the fixed dividend percentage of the par value.
	 */
	@Override
	public BigDecimal calculateDividend() {
		return getParValue().multiply(fixedDividend).divide(ONE_HUNDRED);
	}
}
