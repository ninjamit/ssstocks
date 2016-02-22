package njm.ssstocks.stocks;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import lombok.val;
import njm.ssstocks.stocks.StockCalc;
import njm.ssstocks.stocks.model.Stock;

@RunWith(MockitoJUnitRunner.class)
public class StockCalcTest {

	private StockCalc target;
	
	@Mock private Stock mockStock;

	@Rule public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		target = new StockCalc();
	}
	
	@Test
	public void getDividendYield_normal() {
		when(mockStock.calculateDividend()).thenReturn(new BigDecimal("10"));
		val actual = target.calculateDividendYield(mockStock, new BigDecimal("100"));
		assertThat(actual.compareTo(new BigDecimal("0.1")), is(0));
	}
	
	@Test
	public void getDividendYield_zeroPrice() {
		thrown.expect(IllegalArgumentException.class);
		when(mockStock.calculateDividend()).thenReturn(new BigDecimal("10"));
		target.calculateDividendYield(mockStock, BigDecimal.ZERO);
	}
	
	@Test
	public void getPeRatio_zeroDividend() {
		thrown.expect(RuntimeException.class);
		when(mockStock.calculateDividend()).thenReturn(BigDecimal.ZERO);
		target.calculatePeRatio(mockStock, new BigDecimal("123"));
	}
	
	@Test
	public void getPeRatio_normal() {
		when(mockStock.calculateDividend()).thenReturn(new BigDecimal("10"));
		val actual = target.calculatePeRatio(mockStock, new BigDecimal("100"));
		assertThat(actual.compareTo(new BigDecimal("10")), is(0));
	}
}
