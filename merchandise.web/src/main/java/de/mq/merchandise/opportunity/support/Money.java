package de.mq.merchandise.opportunity.support;

import java.util.Currency;
/**
 * Money has an amount and a currency. (Quantity: currency is the unit, amount the scalar value)
 * Based on Martin Fowlers Pattern of Enterprise Application-Architecture Money Sample.
 * @author mquastem
 *
 */
public interface Money {

	/**
	 * The amount rounded to the default fration digists of the currency
	 * @return the rounded amount (to the fraction digits of the currency)
	 */
    double amountRounded();
    
    /**
     * The not rounded amount, with the full digits that are used internally.
     * @return the not rounded amount
     */
    double amount();

    /**
     * The related currency.
     * @return the currency
     */
    Currency currency();

    /**
     * Checks if the given money object has the same currency
     * @param money the money that currency will be checked
     * @return true if it has the same currency
     */
    boolean sameCurrency(final Money money);

    /**
     * Adds the given money object.
     * @param money the money object that will be added
     * @return the sum of the two money objects
     */
    Money add(final Money money);

    /**
     * Subtracts the money object
     * @param money subtract the given money object 
     * @return the difference 
     */
    Money subtract(final Money money);

    /**
     * Multiply the aount of the money object with the given value. 
     * @param scalar multiply the money object with the scalar
     * @return the product
     */
    Money multiply(final double scalar);

    /**
     * Divide the money object through the given value
     * @param scalar the value through that the mony object will be divide
     * @return quotient 
     */
    Money divide(final double scalar);

    /**
     * Checks if the given money value is greater than the money object
     * @param money the money that will be compared to the object
     * @return true if the given money is greater than the money object
     */
    boolean greaterThan(final Money money);

    /**
     * Checks if the given money value is greater or equals than the money object
     * @param money  the money that will be compared to the object
     * @return  true if the given money is greater or equals than the money object
     */
    boolean greaterOrEqualsThan(final Money money);

    /**
     * Checks if the given money value is less than the money object
     * @param money  the money that will be compared to the object
     * @return  true if the given money is less than the money object
     */
    boolean lessThan(final Money money);

    /**
     * Checks if the given money value is less or equals than the money object
     * @param money the money that will be compared to the object
     * @return  true if the given money is less or equals than the money object
     */
    boolean lessOrEqualsThan(final Money money);

    

}
