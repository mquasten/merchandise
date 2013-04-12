package de.mq.merchandise.model.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import javax.faces.model.DataModel;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.primefaces.model.SelectableDataModel;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.support.CustomerAO;

public class SimpleMapDataModelTest {
	
	
	
	
	
	private static final int INDEX = 4711;
	private static final String ID = "19680528";
	@Test
	public final void constructor () {
		 
		final Collection<CustomerAO> rows = new ArrayList<>();
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		Mockito.when(customer.getId()).thenReturn(ID);
		rows.add(customer);
		
		final List<CustomerAO> model = new SimpleMapDataModel<>(rows);
		@SuppressWarnings("unchecked")
		final Map<UUID,CustomerAO> mapResult = (Map<UUID, CustomerAO>) ReflectionTestUtils.getField(model, "map");
		Assert.assertEquals(1, mapResult.size());
		Assert.assertEquals(UUID.nameUUIDFromBytes(customer.getId().getBytes()), mapResult.keySet().iterator().next());
		Assert.assertEquals(customer, mapResult.values().iterator().next());
		@SuppressWarnings("unchecked")
		Collection<CustomerAO> rowResult = (Collection<CustomerAO>) ReflectionTestUtils.getField(model, "rows");
		Assert.assertEquals(1, rowResult.size());
		Assert.assertEquals(rows, rowResult);
		
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public final void setWrappedDataWrongArgument() {
		final DataModel<String> model = new SimpleMapDataModel<>();
		model.setWrappedData("DontLetMeGetMe");
	}
	@Test(expected=IllegalArgumentException.class)
	public final void setWrappedDataReflectionException() {
		final Collection<CustomerAO> rows = new ArrayList<>();
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		Mockito.when(customer.getId()).thenThrow(new IllegalStateException("Don't woory, expected for test"));
		rows.add(customer);
		new SimpleMapDataModel<>(rows);
	}
	@Test
	public final void getRowKeyWithoutExistingId() {
		final SelectableDataModel<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		Assert.assertEquals(UUID.nameUUIDFromBytes(Long.valueOf(System.identityHashCode(customer)).toString().getBytes()), model.getRowKey(customer));
		
		Mockito.when(customer.getId()).thenReturn(ID);
		Assert.assertEquals(UUID.nameUUIDFromBytes(customer.getId().getBytes()), model.getRowKey(customer));
	}
	
	@Test
	public final void getRowData(){
		final Collection<CustomerAO> rows = new ArrayList<>();
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		Mockito.when(customer.getId()).thenReturn(ID);
		rows.add(customer);
		final SelectableDataModel<CustomerAO> model = new SimpleMapDataModel<>(rows);
		
		Assert.assertEquals(customer, model.getRowData(UUID.nameUUIDFromBytes(ID.getBytes()).toString()));
		
		
	}

	@Test
	public final void size() {
		final Collection<CustomerAO> rows = new ArrayList<>();
		rows.add(Mockito.mock(CustomerAO.class));
		Assert.assertEquals(1, new SimpleMapDataModel<>(rows).size());
		Assert.assertEquals(0, new SimpleMapDataModel<>().size());
	}
	
	@Test
	public final void empty() {
		final Collection<CustomerAO> rows = new ArrayList<>();
		rows.add(Mockito.mock(CustomerAO.class));
		Assert.assertFalse(new SimpleMapDataModel<>(rows).isEmpty());
		Assert.assertTrue(new SimpleMapDataModel<>().isEmpty());
	}
	
	@Test
	public final void contains() {
		final Collection<CustomerAO> rows = new ArrayList<>();
		CustomerAO row = Mockito.mock(CustomerAO.class);;
		rows.add(row);
		Assert.assertTrue(new SimpleMapDataModel<>(rows).contains(row));
		Assert.assertFalse(new SimpleMapDataModel<>(rows).contains(Mockito.mock(CustomerAO.class)));
	}
	
	@Test
	public final void containsIllegalArgument() {
		Assert.assertFalse(new SimpleMapDataModel<>().contains(new Date()));
	}
	
	
	@Test
	public final void iterator() {
		@SuppressWarnings("rawtypes")
		final Collection rows = Mockito.mock(List.class);
		@SuppressWarnings("rawtypes")
		final Iterator iterator = Mockito.mock(Iterator.class);
		Mockito.when(rows.iterator()).thenReturn(iterator);
		
		final SimpleMapDataModel<CustomerAO> model = new SimpleMapDataModel<>();
		ReflectionTestUtils.setField(model, "rows", rows);
		Assert.assertEquals(iterator, model.iterator());
		Mockito.verify(rows).iterator();
	}
	
	@Test
	public final void toArray() {
		@SuppressWarnings("rawtypes")
		final Collection rows = Mockito.mock(List.class);
		CustomerAO[] result = new CustomerAO[]{ }; 
		Mockito.when(rows.toArray()).thenReturn(result);
		final SimpleMapDataModel<CustomerAO> model = new SimpleMapDataModel<>();
		ReflectionTestUtils.setField(model, "rows", rows);
		
		Assert.assertEquals(result, model.toArray());
		Mockito.verify(rows).toArray();
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void toArray2() {
		@SuppressWarnings("rawtypes")
		final Collection rows = Mockito.mock(List.class);
		CustomerAO[] result = new CustomerAO[]{Mockito.mock(CustomerAO.class) }; 
		Mockito.when(rows.toArray(result)).thenReturn(result);
		final SimpleMapDataModel<CustomerAO> model = new SimpleMapDataModel<>();
		ReflectionTestUtils.setField(model, "rows", rows);
		
		Assert.assertEquals(result, model.toArray(result));
		Mockito.verify(rows).toArray(result);
		
	}
	
	
	@Test
	public final void add() {
		
		@SuppressWarnings("unchecked")
		final List<CustomerAO> rows = Mockito.mock(List.class);
		@SuppressWarnings("unchecked")
		final Map<UUID,Object> map = Mockito.mock(Map.class);
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = prepareMocksWithID(rows, map, model);
		Mockito.when(rows.add(customer)).thenReturn(true);
		Assert.assertTrue(model.add(customer));
		Mockito.verify(rows).add(customer);
		Mockito.verify(map).containsKey(UUID.nameUUIDFromBytes(ID.getBytes()));
		Mockito.verify(map).put(UUID.nameUUIDFromBytes(ID.getBytes()), customer);
		
	}
	
	
	
	

	private CustomerAO prepareMocksWithID(final List<CustomerAO> rows, final Map<UUID, Object> map, final List<CustomerAO> model) {
		ReflectionTestUtils.setField(model, "rows", rows);
		ReflectionTestUtils.setField(model, "map", map);
		
		
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		Mockito.when(customer.getId()).thenReturn(ID);
		
		return customer;
	}
	
	@Test
	public final void addExists(){
		@SuppressWarnings("unchecked")
		final List<CustomerAO> rows = Mockito.mock(List.class);
		@SuppressWarnings("unchecked")
		final Map<UUID,Object> map = Mockito.mock(Map.class);
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = prepareMocksWithID(rows, map, model);
		
		Assert.assertFalse(model.add(customer));
		Mockito.verify(rows).add(customer);
		Mockito.verify(map).containsKey(UUID.nameUUIDFromBytes(ID.getBytes()));
		Mockito.verifyNoMoreInteractions(map);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void addDupplicateKey() {
		@SuppressWarnings("unchecked")
		final List<CustomerAO> rows = Mockito.mock(List.class);
		@SuppressWarnings("unchecked")
		final Map<UUID,Object> map = Mockito.mock(Map.class);
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = prepareMocksWithID(rows, map, model);
		Mockito.when(rows.add(customer)).thenReturn(true);
		Mockito.when(map.containsKey(UUID.nameUUIDFromBytes(ID.getBytes()))).thenReturn(true);
		model.add(customer);
		
	}
	
	@Test
	public final void remove() {
		@SuppressWarnings("unchecked")
		final List<CustomerAO> rows = Mockito.mock(List.class);
		@SuppressWarnings("unchecked")
		final Map<UUID,Object> map = Mockito.mock(Map.class);
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = prepareMocksWithID(rows, map, model);
		Mockito.when(map.containsKey(UUID.nameUUIDFromBytes(ID.getBytes()))).thenReturn(true);
		Mockito.when(rows.remove(customer)).thenReturn(true);
		Mockito.when(map.get(UUID.nameUUIDFromBytes(ID.getBytes()))).thenReturn(customer);
		Assert.assertTrue(model.remove(customer));
		
		Mockito.verify(map).containsKey(UUID.nameUUIDFromBytes(ID.getBytes()));
		Mockito.verify(map).get(UUID.nameUUIDFromBytes(ID.getBytes()));
		Mockito.verify(rows).remove(customer);
		
		
	}
	
	@Test
	public final void removeNotExists() {
		@SuppressWarnings("unchecked")
		final List<CustomerAO> rows = Mockito.mock(List.class);
		@SuppressWarnings("unchecked")
		final Map<UUID,Object> map = Mockito.mock(Map.class);
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = prepareMocksWithID(rows, map, model);
		
		
		Assert.assertFalse(model.remove(customer));
		
		Mockito.verify(map).containsKey(UUID.nameUUIDFromBytes(ID.getBytes()));
		Mockito.verifyNoMoreInteractions(map);
		Mockito.verifyNoMoreInteractions(rows);
		
	}
	
	@Test
	public final void containsAll() {
		@SuppressWarnings("unchecked")
		final List<CustomerAO> rows = Mockito.mock(List.class);
		@SuppressWarnings("unchecked")
		final Map<UUID,Object> map = Mockito.mock(Map.class);
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = prepareMocksWithID(rows, map, model);
		Mockito.when(map.containsKey(UUID.nameUUIDFromBytes(ID.getBytes()))).thenReturn(true, false);
		Collection<CustomerAO> customers = new ArrayList<>();
		customers.add(customer);
		
	   Assert.assertTrue(model.containsAll(customers));
	   Assert.assertFalse(model.containsAll(customers));
	   Mockito.verify(map, Mockito.times(2)).containsKey(UUID.nameUUIDFromBytes(ID.getBytes()));
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void addAllWithIndex() {
		
		final List<CustomerAO> rows = Mockito.mock(List.class);
		
		final Map<UUID,Object> map = Mockito.mock(Map.class);
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = prepareMocksWithID(rows, map, model);
		final Collection<CustomerAO> customers = new ArrayList<>();
		customers.add(customer);
		
		
		model.addAll(4711, customers);
		
		Mockito.verify(rows).addAll(4711, customers);
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Map> argumentCaptor = ArgumentCaptor.forClass(Map.class);
		
		Mockito.verify(map).putAll(argumentCaptor.capture());
		
		Assert.assertEquals(1, argumentCaptor.getValue().size());
		Assert.assertEquals(customer, argumentCaptor.getValue().values().iterator().next());
		Assert.assertEquals(UUID.nameUUIDFromBytes(ID.getBytes()), argumentCaptor.getValue().keySet().iterator().next());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void removeAll() {
        final List<CustomerAO> rows = Mockito.mock(List.class);
		
		final Map<UUID,Object> map = Mockito.mock(Map.class);
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = prepareMocksWithID(rows, map, model);
		final Collection<CustomerAO> customers = new ArrayList<>();
		customers.add(customer);
		Mockito.when(map.containsKey(UUID.nameUUIDFromBytes(ID.getBytes()))).thenReturn(true, false);
		
		Assert.assertTrue(model.removeAll(customers));
		Mockito.verify(rows).remove(customer);
		Mockito.verify(map).remove(UUID.nameUUIDFromBytes(ID.getBytes()));
		
		
		Assert.assertFalse(model.removeAll(customers));
		
		Mockito.verify(map,Mockito.times(2)).containsKey(UUID.nameUUIDFromBytes(ID.getBytes()));
		Mockito.verifyNoMoreInteractions(map,rows);
		
	}
	
	
	@Test
	public final void rentainAll() {
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer1 = Mockito.mock(CustomerAO.class);
		final CustomerAO customer2 = Mockito.mock(CustomerAO.class);
		final CustomerAO customer3 = Mockito.mock(CustomerAO.class);
		final CustomerAO customer4 = Mockito.mock(CustomerAO.class);
		final Collection<CustomerAO> customers = new ArrayList<>();
		customers.add(customer2);
		customers.add(customer4);
		model.add(customer1);
		model.add(customer2);
		model.add(customer3);
		
		model.retainAll(customers);
		
		@SuppressWarnings("unchecked")
		final Collection<CustomerAO> rowsResult = (Collection<CustomerAO>) ReflectionTestUtils.getField(model, "rows");
		Assert.assertEquals(1, rowsResult.size());
		Assert.assertEquals(customer2, rowsResult.iterator().next());
		@SuppressWarnings("unchecked")
		final Map<UUID, CustomerAO> mapResult = (Map<UUID, CustomerAO>) ReflectionTestUtils.getField(model, "map");
		Assert.assertEquals(1, mapResult.size());
		Assert.assertEquals(customer2, mapResult.values().iterator().next());
		Assert.assertEquals(UUID.nameUUIDFromBytes(Long.valueOf(System.identityHashCode(customer2)).toString().getBytes()), mapResult.keySet().iterator().next());
	}
	
	@Test
	public final void clear() {
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		model.add(customer);
		Assert.assertEquals(1, model.size());
		model.clear();
		Assert.assertEquals(0, model.size());
		
	}
	
	@Test
	public final void get() {
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		model.add(customer);
		Assert.assertEquals(customer, model.get(0));
		
	}
	@Test
	public final void set() {
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		final CustomerAO newCustomer = Mockito.mock(CustomerAO.class);
		Mockito.when(customer.getId()).thenReturn(ID);
		Mockito.when(newCustomer.getId()).thenReturn(ID);
		model.add(customer);
		Assert.assertEquals(customer, model.get(0));
		model.set(0, newCustomer);
		Assert.assertEquals(newCustomer, model.get(0));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void setNotFound() {
		final List<CustomerAO> model = new SimpleMapDataModel<>();
	    model.set(0, Mockito.mock(CustomerAO.class)); 
	}
	
	@Test
	public final void addIndex() {
		@SuppressWarnings("unchecked")
		final List<CustomerAO> rows = Mockito.mock(List.class);
		@SuppressWarnings("unchecked")
		final Map<UUID,Object> map = Mockito.mock(Map.class);
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = prepareMocksWithID(rows, map, model);
		
		model.add(4711, customer);
		Mockito.verify(rows).add(4711, customer);
		Mockito.verify(map).put(UUID.nameUUIDFromBytes(ID.getBytes()), customer);
		
		
	}
	
	@Test
	public final void removeIndex() {
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		model.add(customer);
		Assert.assertEquals(1, model.size());
		Assert.assertEquals(customer, model.remove(0));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void removeInkonsistent(){
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		final Collection<CustomerAO> customers = new ArrayList<>(); 
		customers.add(customer);
		ReflectionTestUtils.setField(model, "rows", customers);
		model.remove(0);
	}
	
	@Test
	public final void indexOf() {
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final List<?> customers = Mockito.mock(List.class);
		ReflectionTestUtils.setField(model, "rows", customers);
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		Mockito.when(customers.indexOf(customer)).thenReturn(4711);
		Assert.assertEquals(INDEX, model.indexOf(customer));
		Mockito.verify(customers).indexOf(customer);
	}
	
	@Test
	public final void lastIndexOf() {
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final List<?> customers = Mockito.mock(List.class);
		ReflectionTestUtils.setField(model, "rows", customers);
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		Mockito.when(customers.lastIndexOf(customer)).thenReturn(INDEX);
		Assert.assertEquals(INDEX, model.lastIndexOf(customer));
		Mockito.verify(customers).lastIndexOf(customer);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void listIterator() {
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final List<?> customers = Mockito.mock(List.class);
		ReflectionTestUtils.setField(model, "rows", customers);
		@SuppressWarnings("rawtypes")
		final ListIterator listIterator = Mockito.mock(ListIterator.class);
		Mockito.when(customers.listIterator()).thenReturn(listIterator);
		Assert.assertEquals(listIterator, model.listIterator());
		Mockito.verify(customers).listIterator();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void listIteratorIndex() {
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		final List<?> customers = Mockito.mock(List.class);
		ReflectionTestUtils.setField(model, "rows", customers);
		@SuppressWarnings("rawtypes")
		final ListIterator listIterator = Mockito.mock(ListIterator.class);
		Mockito.when(customers.listIterator(INDEX)).thenReturn(listIterator);
		Assert.assertEquals(listIterator, model.listIterator(INDEX));
		Mockito.verify(customers).listIterator(INDEX);
	}
	
	
	
	
	@Test
	public final void subList() {
		final List<CustomerAO> model = new SimpleMapDataModel<>();
		@SuppressWarnings("rawtypes")
		final List customers = Mockito.mock(List.class);
		ReflectionTestUtils.setField(model, "rows", customers);
		
		Mockito.when(customers.subList(INDEX, 2*INDEX)).thenReturn(customers);
		Assert.assertEquals(customers, model.subList(INDEX, 2*INDEX));
		Mockito.verify(customers).subList(INDEX, 2*INDEX);
	}
	
	@Test
	public final void isRowAvailable()  {
		final List<CustomerAO> customers = new ArrayList<>();
		customers.add(Mockito.mock(CustomerAO.class));
		final DataModel<CustomerAO> model = new SimpleMapDataModel<>(customers);
		
		Assert.assertFalse(model.isRowAvailable());
		
		ReflectionTestUtils.setField(model, "index", 0);
		Assert.assertTrue(model.isRowAvailable());
		
		ReflectionTestUtils.setField(model, "index", 1);
		Assert.assertFalse(model.isRowAvailable());
	}
	
	@Test
	public final void rowCount() {
		final List<CustomerAO> customers = new ArrayList<>();
		customers.add(Mockito.mock(CustomerAO.class));
		final DataModel<CustomerAO> model = new SimpleMapDataModel<>(customers);
		Assert.assertEquals(1, model.getRowCount());
	}
	
	@Test
	public final void getIndex() {
		final DataModel<CustomerAO> model = new SimpleMapDataModel<>();
		ReflectionTestUtils.setField(model, "index", INDEX);
		Assert.assertEquals(INDEX, model.getRowIndex());
	}
	
	@Test
	public final void getRowData2() {
		
		final List<CustomerAO> customers = new ArrayList<>();
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		customers.add(customer);
		final DataModel<CustomerAO> model = new SimpleMapDataModel<>(customers);
		ReflectionTestUtils.setField(model, "index", 0);
		Assert.assertEquals(customer, model.getRowData());
	}
	
	@Test
	public final void setRowIndex() {
		final DataModel<CustomerAO> model = new SimpleMapDataModel<>();
		model.setRowIndex(INDEX);
		Assert.assertEquals(INDEX, model.getRowIndex());
	}
	
	
	@Test
	public final void getWrappedData() {
		final List<CustomerAO> customers = new ArrayList<>();
		final CustomerAO customer = Mockito.mock(CustomerAO.class);
		customers.add(customer);
	
		final DataModel<CustomerAO> model = new SimpleMapDataModel<>(customers);
		Assert.assertEquals(customers, model.getWrappedData());
	}
	
}
