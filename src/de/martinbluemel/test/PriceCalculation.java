package de.martinbluemel.test;

public class PriceCalculation {

	public static double calculatePrice(final int numberOfItems, final double startPrice,
			final double alternativeFixPrice) {
		double totalPrice = 0.0;
		double totalAlternativePrice = 0.0;
		double currentPrice = startPrice;
		double totalPriceCheaper = 0.0;
		int numberOfItemsCheaperWithAltPrice = 0;
		for (int i = 0; i < numberOfItems; i++) {
			totalPrice += currentPrice;
			if ((totalAlternativePrice + alternativeFixPrice) > totalPrice) {
				totalAlternativePrice += alternativeFixPrice;
				totalPriceCheaper = totalPrice;
				numberOfItemsCheaperWithAltPrice++;
			}
			currentPrice *= 2;
		}
		System.out.println(String.format("Total price for %d items with start price %f: %f", numberOfItems, startPrice, totalPrice));
		System.out.println(String.format("Total price for %d items with start price %f: %f", numberOfItemsCheaperWithAltPrice, startPrice, totalPriceCheaper));
		System.out.println(String.format("Total price for %d items with alternative fixed price %f: %f", numberOfItemsCheaperWithAltPrice, alternativeFixPrice, totalAlternativePrice));
		return totalPrice;
	}
}
