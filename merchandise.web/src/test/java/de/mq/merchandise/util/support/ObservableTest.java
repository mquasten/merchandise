package de.mq.merchandise.util.support;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.util.event.Observable;
import de.mq.util.event.Observer;
import de.mq.util.event.support.ObservableImpl;



public class ObservableTest {
	

	
	private static final String EVENT1 = "myEvent";
	private static final String EVENT2 = "otherEvent";
	@SuppressWarnings("unchecked")
	final Observer<String> observer1 = Mockito.mock(Observer.class);
	@SuppressWarnings("unchecked")
	final Observer<String> observer2 = Mockito.mock(Observer.class);

	@Test
	public final void notifyObservers() {
		final Observable<String> subject = new ObservableImpl<>();
		
		
		subject.register(observer1, EVENT1);
		subject.register(observer2, EVENT1);
		
		subject.notifyObservers(EVENT1);
		
		Mockito.verify(observer1, Mockito.times(1)).process(EVENT1);
		Mockito.verify(observer2, Mockito.times(1)).process(EVENT1);
	}
	
	@Test
	public final void notifyObserversNothingRegistered() {
		final Observable<String> subject = new ObservableImpl<>();
		
		@SuppressWarnings("unchecked")
		Map<String, Collection<Observer<String>>> map = Mockito.mock(Map.class);
		ReflectionTestUtils.setField(subject, "observers", map);
		Mockito.when(map.containsKey(EVENT1)).thenReturn(false);
		subject.notifyObservers(EVENT1);
		Mockito.verify(map, Mockito.times(1)).containsKey(EVENT1);
		Mockito.verifyNoMoreInteractions(map);;
	}
	@Test
	public final void removeEvent() {
		final Observable<String> subject = new ObservableImpl<>();
		
		subject.register(observer1, EVENT1);
		subject.register(observer2, EVENT1);
		
		subject.register(observer1, EVENT2);
		subject.register(observer2, EVENT2);
		
		subject.remove(observer1, EVENT1);
		subject.notifyObservers(EVENT1);
		Mockito.verify(observer2, Mockito.times(1)).process(EVENT1);
		Mockito.verify(observer1, Mockito.times(0)).process(EVENT1);
		
		subject.notifyObservers( EVENT2);
		Mockito.verify(observer2, Mockito.times(1)).process(EVENT2);
		Mockito.verify(observer1, Mockito.times(1)).process(EVENT2);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void removeEventNotRegisterd() {
		final Observable<String> subject = new ObservableImpl<>();
		subject.remove(Mockito.mock(Observer.class), EVENT1);
	}
	
	@Test
	public final void remove() {
		final Observable<String> subject = new ObservableImpl<>();
		subject.register(observer1, EVENT1);
		subject.register(observer2, EVENT1);
		subject.register(observer1, EVENT2);
		subject.register(observer2, EVENT2);
		subject.remove(observer2);
		
		subject.notifyObservers(EVENT1);
		subject.notifyObservers(EVENT2);
		
		Mockito.verify(observer1, Mockito.times(1)).process(EVENT1);
		Mockito.verify(observer1, Mockito.times(1)).process(EVENT2);
		
		Mockito.verify(observer2, Mockito.times(0)).process(EVENT1);
		Mockito.verify(observer2, Mockito.times(0)).process(EVENT2);
	}

}
