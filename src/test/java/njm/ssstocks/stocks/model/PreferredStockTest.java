package njm.ssstocks.stocks.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import lombok.val;
import njm.ssstocks.stocks.model.CommonStock;
import njm.ssstocks.stocks.model.PreferredStock;
import njm.ssstocks.stocks.model.Stock;

public class PreferredStockTest {

	@Rule public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testConstructor() {
		Stock actual = new PreferredStock("DUMMY", BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.ONE);
		assertThat(actual, is(notNullValue()));
	}
	
	@Test
	public void testConstructor_nullSymbol() {
		thrown.expect(NullPointerException.class);
		new PreferredStock(null, BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.ONE);
	}
	
	@Test
	public void testConstructor_nullLastDividend() {
		thrown.expect(NullPointerException.class);
		new PreferredStock("DUMMY", null, BigDecimal.TEN, BigDecimal.ONE);
	}
	
	@Test
	public void testConstructor_nullParValue() {
		thrown.expect(NullPointerException.class);
		new PreferredStock("DUMMY", BigDecimal.TEN, null, BigDecimal.ONE);
	}
	
	@Test
	public void testConstructor_nullFixedDivident() {
		thrown.expect(NullPointerException.class);
		new PreferredStock("DUMMY", BigDecimal.TEN, BigDecimal.TEN, null);
	}
	
	@Test
	public void calculateDividend() {
		val actual = new PreferredStock("DUMMY", BigDecimal.ONE, BigDecimal.TEN, new BigDecimal("2")).calculateDividend();
		assertThat(actual.compareTo(new BigDecimal("0.2")), is(0));
	}

	@Test
	public void testEquals() {
		Stock one = new PreferredStock("ONE", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ONE);
		Stock same = new PreferredStock("ONE", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ONE);
		Stock common = new CommonStock("ONE", BigDecimal.ONE, BigDecimal.TEN);
		Stock different = new PreferredStock("ONE", BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.ONE);
		assertThat(one.equals(same), is(true));
		assertThat(same.equals(one), is(true));
		assertThat(one.equals(common), is(false));
		assertThat(one.equals(different), is(false));
	}

	@Test
	public void testHashcode() {
		Stock one = new PreferredStock("ONE", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ONE);
		Stock same = new PreferredStock("ONE", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ONE);
		Stock common = new CommonStock("ONE", BigDecimal.ONE, BigDecimal.TEN);
		Stock different = new PreferredStock("ONE", BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.ONE);
		assertThat(one.hashCode() == same.hashCode(), is(true));
		assertThat(one.hashCode() == common.hashCode(), is(false));
		assertThat(one.hashCode() == different.hashCode(), is(false));
	}

}
