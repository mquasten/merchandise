package de.mq.merchandise.order.support;


import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;


import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;



class ItemImpl implements Item {
	@Equals()
	private final ItemSet itemSet;
	
	@Equals()
	private final String itemId; 
	
	private String productId;
	
    private String quality;
    
    private String unit;
    
    private Integer quantity; 

    private Money  pricePerUnit;
	
    private Collection<String>currencies=new HashSet<>(); 
    
    ItemImpl(final ItemSet itemSet, final String itemId ) {
		this.itemSet = itemSet;
		this.itemId=itemId;
		EntityUtil.notNullGuard(this.itemSet, "ItemSet");
    	EntityUtil.notNullGuard(this.productId, "itemId");
	}
    
    /* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.Item#itemId()
	 */
    @Override
	public String itemId() {
    	return itemId;
    }
    
    /* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.Item#itemSet()
	 */
    @Override
	public ItemSet itemSet() {
    	EntityUtil.notNullGuard(itemSet, "ItemSet");
    	return this.itemSet;
    }
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.Item#productId()
	 */
	@Override
	public String productId() {
		return this.productId;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.Item#quantity()
	 */
	@Override
	public Integer quantity() {
		return quantity;
	}
	
   /* (non-Javadoc)
 * @see de.mq.merchandise.order.support.Item#quality()
 */
@Override
public String quality() {
	   return quality;
   }
    
    /* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.Item#unit()
	 */
    @Override
	public String unit() {
    	return unit;
    }

   
    /* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.Item#assignPricePerUnit(de.mq.merchandise.order.support.Money)
	 */
    @Override
	public void assignPricePerUnit(final Money pricePerUnit) {
    	EntityUtil.notNullGuard(pricePerUnit, "PricePerUnit");
		this.pricePerUnit=pricePerUnit;
    }
    
    
    
    /* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.Item#assignProductId(java.lang.String)
	 */
    @Override
	public void assignProductId(final String productId) {
    	EntityUtil.notNullGuard(productId, "ProductId");
		this.productId=productId;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.Item#assignQuantity(java.lang.Integer)
	 */
	@Override
	public void assignQuantity(final Integer quantity) {
		EntityUtil.notNullGuard(quantity, "Quantity");
		this.quantity=quantity();
	}
	
   /* (non-Javadoc)
 * @see de.mq.merchandise.order.support.Item#assignQuality(java.lang.String)
 */
@Override
public void assignQuality(final String quality) {
	   EntityUtil.notNullGuard(quality, "Quality");
	   this.quality=quality;
   }
    
    /* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.Item#assignUnit(java.lang.String)
	 */
    @Override
	public void assignUnit(final String unit) {
    	EntityUtil.notNullGuard(unit, "Unit");
    	this.unit=unit;
    }

   
    /* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.Item#pricePerUnit()
	 */
    @Override
	public Money pricePerUnit() {
		return pricePerUnit;
    }
    
    /* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.Item#currencies()
	 */
    @Override
	public Collection<Currency> currencies() {
    	final Collection<Currency> results = new  HashSet<>();
    	for(String currency : this.currencies) {
    		results.add(Currency.getInstance(currency));
    	}
    	return Collections.unmodifiableCollection(results);
    }
    
    @Override
	public int hashCode() {
		return  EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
	    return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(ItemImpl.class).isEquals();
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.Item#amount()
	 */
	@Override
	public final Money amount() {
		EntityUtil.notNullGuard(pricePerUnit, "PricePerUnit");
		EntityUtil.notNullGuard(quality, "Quantity");
		return pricePerUnit.multiply(quantity);
		
	}
	
	
}
