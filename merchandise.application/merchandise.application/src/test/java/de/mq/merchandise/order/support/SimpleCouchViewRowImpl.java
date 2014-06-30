package de.mq.merchandise.order.support;

import java.util.Collections;
import java.util.Map;



	class SimpleCouchViewRowImpl implements CouchViewResultRow {
	
		private Object key;
		
		private Object value;
		
		private String id;
		
		/* (non-Javadoc)
		 * @see de.mq.merchandise.order.support.CouchViewResultRow#id()
		 */
		@Override
		public final String id()  {
			return id;
		}
		
		/* (non-Javadoc)
		 * @see de.mq.merchandise.order.support.CouchViewResultRow#singleKey()
		 */
		@Override
		public final String singleKey() {
			
			if (key instanceof Map) {
				throw new IllegalArgumentException("Key is a Composed Key");
			}
			return (String) key;
			
		}
		
		/* (non-Javadoc)
		 * @see de.mq.merchandise.order.support.CouchViewResultRow#singleValue()
		 */
		@Override
		public final String singleValue() {
			if (key instanceof Map) {
				throw new IllegalArgumentException("Key is a Composed Key");
			}
			
			return (String) value;
		}
		
		/* (non-Javadoc)
		 * @see de.mq.merchandise.order.support.CouchViewResultRow#composedKey()
		 */
		@Override
		@SuppressWarnings("unchecked")
		public final Map<String,? extends Object> composedKey() {
			if (!(key instanceof Map<?,?>)) {
				throw new IllegalArgumentException("Key is not  a Composed Key");
			}
			return Collections.unmodifiableMap((Map<String, ? extends Object>) key);
		}
		
		/* (non-Javadoc)
		 * @see de.mq.merchandise.order.support.CouchViewResultRow#composedValue()
		 */
		@Override
		@SuppressWarnings("unchecked")
		public final Map<String,? extends Object> composedValue() {
			if (!(value instanceof Map<?,?>)) {
				throw new IllegalArgumentException("Key is not  a Composed Key");
			}
			return Collections.unmodifiableMap((Map<String, ? extends Object>) value);
		}
		
		
	}
