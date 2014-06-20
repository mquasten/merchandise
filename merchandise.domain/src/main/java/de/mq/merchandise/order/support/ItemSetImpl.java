package de.mq.merchandise.order.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.Opportunity;
import de.mq.merchandise.order.Item;
import de.mq.merchandise.order.ItemSet;
import de.mq.merchandise.order.Money;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

public class ItemSetImpl implements ItemSet  {


	@Equals
	private final Opportunity opportunity;
	
	@Equals
	private final Customer tradingPartner;
	
	private Date submitted; 
	
	@Equals
	private final Date created = new Date(); 
	
	private final List<Item> items = new ArrayList<>();
	
	
	public ItemSetImpl(final Customer tradingPartner, final Opportunity opportunity) {
		this.tradingPartner = tradingPartner;
		this.opportunity = opportunity;
	}
	
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#submitted()
	 */
	@Override
	public final Date submitted() {
		EntityUtil.notNullGuard(submitted, "SubmitDate");
		return submitted;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#submitted()
	 */
	@Override
	public final Date created() {
		EntityUtil.notNullGuard(created, "CreatedDate");
		return created;
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#isSubmitted()
	 */
	@Override
	public boolean  isSubmitted() {
		return (submitted != null);
	}

	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#tradingPartner()
	 */
	@Override
	public Customer tradingPartner() {
		return tradingPartner;
	}

	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#opportunity()
	 */
	@Override
	public Opportunity opportunity() {
		return opportunity;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#amount()
	 */
	@Override
	public Money amount() {
		if(items.isEmpty()){
			throw new IllegalArgumentException("No items aware, amount can't be calculated");
		}
		
		Money amount = new MoneyImpl(0, items.iterator().next().amount().currency());
		for(final Item item : items){
			amount=amount.add(item.amount());
		}
		return amount;
	}
		

	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#assign(java.lang.String)
	 */
	@Override
	public void assign(final Item item) {
		items.add(item);
	}
	
	

	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#items()
	 */
	@Override
	public Collection<Item> items() {
		return Collections.unmodifiableList(items);
	}
	
	@Override
	public int hashCode() {
		return  EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
	    return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(ItemSet.class).isEquals();
	}



	


	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#item(java.util.UUID)
	 */
	@Override
	public Item item(final UUID itemId) {
		final Item result = findItem(itemId);
		if ( result == null) {
			throw new IllegalArgumentException("Item not found in itemSet id: " + itemId);
		}
		return result;
	}



	private Item findItem(final UUID itemId) {
		for(final Item item : items) {
			if( item.itemId().equals(itemId)){
			   return item;	
			}
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ItemSet#remove(java.util.UUID)
	 */
	@Override
	public void remove(UUID itemId) {
		final Item item = findItem(itemId);
		if (item==null){
			return;
		}
		remove(itemId);
		
	}



	
}
