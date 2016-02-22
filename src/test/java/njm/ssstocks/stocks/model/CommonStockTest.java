package njm.ssstocks.stocks.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import lombok.val;
import njm.ssstocks.stocks.model.CommonStock;
import njm.ssstocks.stocks.model.PreferredStock;
import njm.ssstocks.stocks.model.Stock;

public class CommonStockTest {

	@Rule public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testConstructor() {
		Stock actual = new CommonStock("DUMMY", BigDecimal.ZERO, BigDecimal.TEN);
		assertThat(actual, is(notNullValue()));
	}
	
	@Test
	public void testConstructor_nullSymbol() {
		thrown.expect(NullPointerException.class);
		new CommonStock(null, BigDecimal.ZERO, BigDecimal.TEN);
	}
	
	@Test
	public void testConstructor_nullLastDividend() {
		thrown.expect(NullPointerException.class);
		new CommonStock("DUMMY", null, BigDecimal.TEN);
	}
	
	@Test
	public void testConstructor_nullParValue() {
		thrown.expect(NullPointerException.class);
		new CommonStock("DUMMY", BigDecimal.TEN, null);
	}

	@Test
	public void testGetDividend() {
		val actual = new CommonStock("DUMMY", BigDecimal.ONE, BigDecimal.TEN).calculateDividend();
		assertThat(actual, is(BigDecimal.ONE));
	}

	@Test
	public void testEquals() {
		Stock one = new CommonStock("ONE", BigDecimal.ONE, BigDecimal.TEN);
		Stock same = new CommonStock("ONE", BigDecimal.ONE, BigDecimal.TEN);
		Stock pref = new PreferredStock("ONE", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ONE);
		Stock different = new CommonStock("ONE", BigDecimal.ONE, BigDecimal.ZERO);
		assertThat(one.equals(same), is(true));
		assertThat(same.equals(one), is(true));
		assertThat(one.equals(pref), is(false));
		assertThat(one.equals(different), is(false));
	}

	@Test
	public void testHashcode() {
		Stock one = new CommonStock("ONE", BigDecimal.ONE, BigDecimal.TEN);
		Stock same = new CommonStock("ONE", BigDecimal.ONE, BigDecimal.TEN);
		Stock pref = new PreferredStock("ONE", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ONE);
		Stock different = new CommonStock("ONE", BigDecimal.ZERO, BigDecimal.TEN);
		assertThat(one.hashCode() == same.hashCode(), is(true));
		assertThat(one.hashCode() == pref.hashCode(), is(false));
		assertThat(one.hashCode() == different.hashCode(), is(false));
	}
	
}
