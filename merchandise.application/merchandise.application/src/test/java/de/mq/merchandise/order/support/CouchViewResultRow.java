package de.mq.merchandise.order.support;

import java.util.Map;

interface CouchViewResultRow {

	String id();

	String singleKey();

	String singleValue();

	Map<String, ? extends Object> composedKey();

	Map<String, ? extends Object> composedValue();

}