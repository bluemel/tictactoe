package de.martinbluemel.test;

import org.junit.Assert;
import org.junit.Test;

public class PriceCalculationTest {
	@Test
	public void test() {
		double price = PriceCalculation.calculatePrice(8, 0.1, 0.5);
		Assert.assertEquals(25.5, price, 0.001);
	}
}
