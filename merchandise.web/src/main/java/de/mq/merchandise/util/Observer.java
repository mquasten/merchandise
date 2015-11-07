package de.mq.merchandise.util;

@FunctionalInterface
public interface Observer<EventType> {

	void process(final EventType event);

}