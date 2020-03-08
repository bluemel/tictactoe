package de.martinbluemel.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class PriceCalculationTest {
	@Test
	public void test() {
		PriceCalculation.calculatePrice(8, 0.1, 0.5);
	}
}
