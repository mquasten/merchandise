package de.mq.merchandise.util.support;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import de.mq.mapping.util.proxy.support.BasicNullObjectResolverImpl;
import de.mq.merchandise.util.EntityUtil;

@Component
public class DomainObjectNullResolverImpl extends BasicNullObjectResolverImpl {
	
	
	private final String PACKAGE_SCAN = "de.mq.merchandise";

	private final MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(new PathMatchingResourcePatternResolver());
	
	private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	@PostConstruct()
	final void init() {
		final Map<Class<?>,Integer> levels = new HashMap<>();
		for(final Class<?> clazz: entities()){
			addHierarchy(levels, clazz);	
		}
	}


	private void addHierarchy(final Map<Class<?>, Integer> levels, final Class<?> clazz) {
		final Object entity = EntityUtil.create(clazz);
		for(final Entry<Class<?>,Integer> entry : hierarchy(clazz)){
			if(! levels.containsKey(entry.getKey())){
				put(entry.getKey(), entity);
				levels.put(entry.getKey(), entry.getValue());
				continue;
			}
			
			if( levels.get(entry.getKey() ) > entry.getValue() ) {
				put(entry.getKey(), entity);
				levels.put(entry.getKey(), entry.getValue());
				continue;
			}
		}
	}
	
	
	final Collection<Class<?>> entities()  {
	    final Collection<Class<?>> results = new HashSet<>();
	    final Resource[] resources = resources();
	    for (Resource resource : resources) {
	       
	        final Class<?> clazz = entityClass(metadataReaderFactory, resource);
	       
	       
	        if( ! clazz.isAnnotationPresent(Entity.class)&&! clazz.isAnnotationPresent(Embeddable.class) ){
	        	continue;
	        }
	      
	        if( Modifier.isAbstract( clazz.getModifiers() )){
	        	continue;
	        }
	      results.add(clazz);
	    
	    }
		return results;

	}
	
	
	final Collection<Entry<Class<?>, Integer>>hierarchy(Class<?> parentClass) {
		final Collection<Entry<Class<?>, Integer>> childs = new HashSet<>();
		childs.add(createEntry(parentClass,0));
		childs.addAll(childs(parentClass,0));
		
		return childs;
		
	}
	
	
	private   Collection<Entry<Class<?>, Integer>> childs(final Class<?> parentClass, final int level) {
		final Collection<Entry<Class<?>, Integer>> childs = new HashSet<>();
		for(Class<?> clazz : parentClass.getInterfaces()) {
			if( !clazz.getName().startsWith(PACKAGE_SCAN)){
				continue;
			}
			childs.add(createEntry(clazz, level));
			childs.addAll(childs(clazz, level+1));
		}
		
		Class<?> clazz =  parentClass.getSuperclass();
		int i=1;
		while(clazz!=null){
			if( !clazz.getName().startsWith(PACKAGE_SCAN)){
				break;
			} 
			childs.add(createEntry(clazz, level+i));
			childs.addAll(childs(clazz, level+i+1));
			clazz=clazz.getSuperclass();
			i++;
		}
		return childs;
	}


	private SimpleEntry<Class<?>, Integer> createEntry(final  Class<?> clazz, final int level) {
		if( clazz == null) {
			throw new IllegalArgumentException("Class should be mandatory");
		}
		return new AbstractMap.SimpleEntry<Class<?>, Integer>(clazz, level) {
			
			
			
			private static final long serialVersionUID = 1L;
			
			

			@Override
			public boolean equals(Object o) {
				return ((Entry<?,?>)o).getKey().equals(getKey());
			}

			@Override
			public int hashCode() {
			
				return getKey().hashCode();
			}
			
			
		};
	}

	private Class<?> entityClass(final MetadataReaderFactory metadataReaderFactory, Resource resource)  {
		try {
			return Class.forName(metadataReaderFactory.getMetadataReader(resource).getClassMetadata().getClassName());
		} catch (final IOException   ex ) {
			throw new BeanDefinitionStoreException("Unable to read meta data for Entity:"  ,  ex);
		} catch(final ClassNotFoundException ex) {
			throw new BeanDefinitionStoreException("Unable to load EntityClass", ex);
		}
	}

	private Resource[] resources()  {
		try {
			
			return resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
			                           ClassUtils.convertClassNameToResourcePath(PACKAGE_SCAN) + "/" + "**/*.class");
		} catch (IOException e) {
			throw new BeanDefinitionStoreException("Unable to read Entities", e);
		}
	}
	  

}
