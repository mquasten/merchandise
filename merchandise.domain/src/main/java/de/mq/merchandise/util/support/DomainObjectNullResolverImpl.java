package de.mq.merchandise.util.support;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import de.mq.mapping.util.proxy.NullObjectResolver;
import de.mq.merchandise.util.EntityUtil;

@Component
@Profile({"CGLib-Proxy", "Dynamic-Proxy"})
public class DomainObjectNullResolverImpl  implements NullObjectResolver {
	
	
	private final String PACKAGE_SCAN = "de.mq.merchandise";

	
	private  final ResourcePatternResolver resourcePatternResolver; 
	
	

	private  final MetadataReaderFactory metadataReaderFactory;
	
	
	
	
	private final Map<Class<?>, Object> nullObjects = new HashMap<>();
	
	@Autowired
	DomainObjectNullResolverImpl(final MetadataReaderFactory metadataReaderFactory, final ResourcePatternResolver resourcePatternResolver) {
		
		this.resourcePatternResolver = resourcePatternResolver;
		this.metadataReaderFactory = metadataReaderFactory;
	}
	
	@SuppressWarnings("unchecked")
	private final Set<Class<Annotation>> entityAnnotations = new HashSet<>(CollectionUtils.arrayToList(new Class<?>[]{Entity.class, Embeddable.class} )); 
	
	@PostConstruct()
	final void init() {
		final Map<Class<?>,Integer> levels = new HashMap<>();
		for(final Class<?> clazz: entities()){
			addHierarchy(levels, clazz);	
		}
		
	}
	
	
	
	
	private void objectExistsGuard(final Class<?> clazz) {
		if( nullObjects.containsKey(clazz)){
			return;
		}
		throw new NoSuchBeanDefinitionException(clazz);
	}


	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T forType(Class<? extends T> clazz) {
		objectExistsGuard(clazz);
		return   EntityUtil.copy((T)nullObjects.get(clazz));
	}



	void inject(final Object entity, final Map<Class<?>,Object> entities) {
		ReflectionUtils.doWithFields(entity.getClass(), new FieldCallback() {

			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				field.setAccessible(true);
				
				//System.out.println(field.getType() + ":" + entities.get(field.getType()));
				if( field.get(entity)!=null){
					return;
				}
				field.set(entity, entities.get(field.getType()));
			}
			
			
		}, new FieldFilter() {
			
			@Override
			public boolean matches(final Field field) {
				
				field.setAccessible(true);
				
				return entities.containsKey(field.getType());
			}
		});
	}


	private void addHierarchy(final Map<Class<?>, Integer> levels, final Class<?> clazz) {
		final Object entity = EntityUtil.create(clazz);
		for(final Entry<Class<?>,Integer> entry : hierarchy(clazz)){
			if(! levels.containsKey(entry.getKey())){
				nullObjects.put(entry.getKey(), entity);	
				levels.put(entry.getKey(), entry.getValue());
				continue;
			}
			
			if( levels.get(entry.getKey() ) > entry.getValue() ) {
				nullObjects.put(entry.getKey(), entity);
				levels.put(entry.getKey(), entry.getValue());
				continue;
			}
		}
	}
	
	
	final Collection<Class<?>> entities()  {
	    final Collection<Class<?>> results = new LinkedHashSet <>();
	   
	    final Resource[] resources = resources();
	    for (Resource resource : resources) {
	       
	        final Class<?> clazz = entityClass(resource);
	       
	       
	        if( ! isAnnotationPresent(clazz) ){
	        	continue;
	        }
	      
	        if( Modifier.isAbstract( clazz.getModifiers() )){
	        	continue;
	        }
	      results.add(clazz);
	    
	    }
		return results;

	}
	
	
	private boolean isAnnotationPresent(Class<?> clazz) {
		for(final Class<Annotation> annotationClass : entityAnnotations){
			if( clazz.isAnnotationPresent((Class<? extends java.lang.annotation.Annotation>) annotationClass)) {
				return true;
			}
		}
		return false;
		
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

	private Class<?> entityClass(final Resource resource)  {
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
