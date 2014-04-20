package de.mq.merchandise.util.support;

import java.io.IOException;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.CollectionUtils;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.LegalPerson;
import de.mq.merchandise.customer.Nativity;
import de.mq.merchandise.customer.NaturalPerson;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.State;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.customer.support.Digest;
import de.mq.merchandise.customer.support.NativityBuilderFactoryImpl;
import de.mq.merchandise.customer.support.StateBuilderImpl;
import de.mq.merchandise.util.EntityUtil;


public class DomainObjectNullResolverTest {
	
	
	
	private static final IOException IO_EXCEPTION = new IOException("Don't worry only for test");
	
	private  final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	

	private  final MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
	@SuppressWarnings("unchecked")
	final Set<String> CLASS_NAMES =  new HashSet<>(CollectionUtils.arrayToList(new String[] {
	
			"de.mq.merchandise.contact.support.GeoLocationImpl",
			"de.mq.merchandise.opportunity.support.RuleInstanceImpl",
			"de.mq.merchandise.opportunity.support.CommercialRelationImpl",
			"de.mq.merchandise.reference.support.ReferenceImpl",
			"de.mq.merchandise.contact.support.PhoneContactImpl",
			"de.mq.merchandise.opportunity.support.OpportunityFullTextSearchIndexImpl",
			"de.mq.merchandise.opportunity.support.OpportunityGeoLocationIndexImpl",
			"de.mq.merchandise.customer.support.LegalPersonImpl",
			"de.mq.merchandise.contact.support.EMailContactImpl",
			"de.mq.merchandise.customer.support.UserRelationImpl",
			"de.mq.merchandise.opportunity.support.ConditionImpl",
			"de.mq.merchandise.rule.support.RuleImpl",
			"de.mq.merchandise.customer.support.NaturalPersonImpl",
			"de.mq.merchandise.customer.support.CustomerImpl",
			"de.mq.merchandise.contact.support.PostBoxImpl",
			"de.mq.merchandise.opportunity.support.EntityContextImpl",
			"de.mq.merchandise.opportunity.support.CommercialSubjectImpl",
			"de.mq.merchandise.opportunity.support.ProductClassificationImpl",
			"de.mq.merchandise.opportunity.support.ActivityClassificationImpl",
			"de.mq.merchandise.contact.support.InstantMessengerContactImpl",
			"de.mq.merchandise.opportunity.support.OpportunityImpl",
			"de.mq.merchandise.contact.support.AddressImpl",
			
			"de.mq.merchandise.customer.support.DigestImpl",
			"de.mq.merchandise.customer.support.TradeRegisterImpl",
			"de.mq.merchandise.customer.support.StateImpl",
			"de.mq.merchandise.customer.support.NativityImpl",
			"de.mq.merchandise.contact.support.CoordinatesImpl",
			
	}));
	
	
	@Test
	public final void  entities ()  {
		DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl(metadataReaderFactory, resourcePatternResolver);
			final Collection<Class<?>> results = domainObjectNullResolver.entities();
			Assert.assertEquals(CLASS_NAMES.size(), results.size());
			
			
			for(final Class<?> clazz : results){
				Assert.assertTrue("Entity is missing: " +clazz.getName() , CLASS_NAMES.contains(clazz.getName()));
				
			}
	
		}
	
	
	@Test(expected=BeanDefinitionStoreException.class)
	public final void entiesResolverException() throws IOException {
		
		final PathMatchingResourcePatternResolver resourcePatternResolver = Mockito.mock(PathMatchingResourcePatternResolver.class);
		//ReflectionTestUtils.setField(domainObjectNullResolver, "resourcePatternResolver", resourcePatternResolver);
		Mockito.when(resourcePatternResolver.getResources(Mockito.anyString())).thenThrow(IO_EXCEPTION);
		
		DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl(metadataReaderFactory, resourcePatternResolver);
		domainObjectNullResolver.entities();
	}
		
	
	@Test(expected=BeanDefinitionStoreException.class)
	public final void entiesMetaDataFactoryException() throws IOException {
		
		final MetadataReaderFactory metadataReaderFactory = Mockito.mock(MetadataReaderFactory.class);
		//ReflectionTestUtils.setField(domainObjectNullResolver,"metadataReaderFactory", metadataReaderFactory);
		Mockito.when(metadataReaderFactory.getMetadataReader(Mockito.any(Resource.class))).thenThrow(IO_EXCEPTION);
		
		DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl(metadataReaderFactory, resourcePatternResolver);
		domainObjectNullResolver.entities();
	}
	
	@Test(expected=BeanDefinitionStoreException.class)
	public final void entiesWrongClassException() throws IOException {
		
		final MetadataReaderFactory metadataReaderFactory = Mockito.mock(MetadataReaderFactory.class);
	//	ReflectionTestUtils.setField(domainObjectNullResolver,"metadataReaderFactory", metadataReaderFactory);
		MetadataReader reader = Mockito.mock(MetadataReader.class);
		Mockito.when(metadataReaderFactory.getMetadataReader(Mockito.any(Resource.class))).thenReturn(reader);
		final ClassMetadata classMetadata = Mockito.mock(ClassMetadata.class);
		Mockito.when(reader.getClassMetadata()).thenReturn(classMetadata);
		Mockito.when(classMetadata.getClassName()).thenReturn("don'tLetMeGet");
		DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl(metadataReaderFactory, resourcePatternResolver);
		domainObjectNullResolver.entities();
	}
	
	@Test
	public final void hierarchy()  {
		final DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl(metadataReaderFactory, resourcePatternResolver);
		final Collection<Entry<Class<?>, Integer>> results = domainObjectNullResolver.hierarchy(legalPersonClass());
		
	
		Assert.assertEquals(5, results.size());
		final Set<Integer> hash = new HashSet<>();
		
		for(final Entry<Class<?>, Integer> interfaceEntry : results ){
			
			Assert.assertFalse(hash.contains(interfaceEntry.getKey().hashCode()));
			hash.add(interfaceEntry.getKey().hashCode());
			if (LegalPerson.class.equals( interfaceEntry.getKey())) {
				Assert.assertEquals(0,(int) interfaceEntry.getValue());
				continue;
			}
			if (Person.class.equals( interfaceEntry.getKey())) {
				Assert.assertEquals(1,(int) interfaceEntry.getValue());
				continue;
			}
			if (BasicEntity.class.equals( interfaceEntry.getKey())) {
				Assert.assertEquals(2,(int) interfaceEntry.getValue());
				continue;
			}
			
			if (abstractPersonClass().equals( interfaceEntry.getKey())) {
				Assert.assertEquals(1,(int) interfaceEntry.getValue());
				continue;
			}
			if (legalPersonClass().equals( interfaceEntry.getKey())) {
				Assert.assertEquals(0,(int) interfaceEntry.getValue());
				continue;
			}
			Assert.fail("Wrong Type: "+ interfaceEntry.getKey() );
		}
		Assert.assertEquals(results.size(), hash.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void entryNullGuard()  {
		final DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl(metadataReaderFactory, resourcePatternResolver);
		domainObjectNullResolver.hierarchy(null);
	}


	private Class<?> abstractPersonClass() {
		try {
			return Class.forName("de.mq.merchandise.customer.support.AbstractPerson");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException();
		}
	}
	
	private Class<?> legalPersonClass() {
		try {
			return Class.forName("de.mq.merchandise.customer.support.LegalPersonImpl");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException();
		}
	}
	
	@Test
	public final void init() throws IOException {
	
		
		
		
		final ResourcePatternResolver resourcePatternResolver = Mockito.mock(ResourcePatternResolver.class);
		final Resource[] resources = new Resource[] {new ClassPathResource(legalPersonClass().getSimpleName()+ ".class",  legalPersonClass()), new ClassPathResource(MockImpl.class.getSimpleName()+ ".class",  getClass()) };
		
	
		Mockito.when(resourcePatternResolver.getResources(Mockito.anyString())).thenReturn(resources);
		//ReflectionTestUtils.setField(domainObjectNullResolver, "resourcePatternResolver", resourcePatternResolver);
		
		final DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl(metadataReaderFactory, resourcePatternResolver);
		addTestEntityAnnotation(domainObjectNullResolver);
		domainObjectNullResolver.init();
		
		
		@SuppressWarnings("unchecked")
		final Map<Class<?>, Object> results = (Map<Class<?>, Object>) ReflectionTestUtils.getField(domainObjectNullResolver, "nullObjects");
		Assert.assertEquals(6, results.size());
		Assert.assertTrue(results.containsKey(legalPersonClass()));
		Assert.assertTrue(results.containsKey(MockImpl.class));
		final Object entity= results.get(legalPersonClass());
		final Object mock= results.get(MockImpl.class);
		
		Assert.assertEquals(entity, results.get(abstractPersonClass()));
		Assert.assertEquals(entity, results.get(LegalPerson.class));
		Assert.assertEquals(entity, results.get(Person.class));
		
		
		Assert.assertEquals(mock, results.get(BasicEntity.class));
		
		
		
		
	}


	@SuppressWarnings("unchecked")
	private void addTestEntityAnnotation(final DomainObjectNullResolverImpl domainObjectNullResolver) {
		((Collection<Class<?>>) ReflectionTestUtils.getField(domainObjectNullResolver, "entityAnnotations")).add(TestEntity.class);
	}
	

	@Test
	public final void inject() throws ClassNotFoundException {
		final DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl(metadataReaderFactory, resourcePatternResolver);
		@SuppressWarnings("unchecked")
		final NaturalPerson  person = EntityUtil.create((Class<? extends NaturalPerson>) Class.forName("de.mq.merchandise.customer.support.NaturalPersonImpl"));
		ReflectionTestUtils.setField(person, "digest", null);
		ReflectionTestUtils.setField(person, "state", null);
		Assert.assertNull(person.state());
		Assert.assertNull(person.digest());
		Assert.assertNull(person.nativity());
		
		
		final Customer customer = EntityUtil.create(CustomerImpl.class);
		ReflectionTestUtils.setField(customer, "state", null);
		
		
		final Map<Class<?>, Object> dependencies = new HashMap<>();
		
		dependencies.put(Nativity.class, new NativityBuilderFactoryImpl().nativityBuilder().withBirthDate(new GregorianCalendar(1968, 4, 28).getTime()).withBirthPlace("Melborne").build() );
		dependencies.put(State.class, new StateBuilderImpl().forState(true).build());
		dependencies.put(Digest.class, EntityUtil.create(Class.forName("de.mq.merchandise.customer.support.DigestImpl")));
		dependencies.put(Person.class, person);
		dependencies.put(Customer.class, customer);
		
		domainObjectNullResolver.inject(person, dependencies);
		domainObjectNullResolver.inject(customer, dependencies);
		
		Assert.assertEquals(person, customer.person());
		Assert.assertEquals(dependencies.get(State.class), customer.state());
		
		Assert.assertEquals(dependencies.get(State.class), person.state());
		Assert.assertEquals(dependencies.get(Nativity.class), person.nativity());
		Assert.assertEquals(dependencies.get(Digest.class), person.digest());
		
		
		
	}
	
	@Test
	public final void injectNotOverwriteExisting() {
		final DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl(metadataReaderFactory, resourcePatternResolver);
		final Customer customer = EntityUtil.create(CustomerImpl.class);
		
		final Map<Class<?>, Object> dependencies = new HashMap<>();
		dependencies.put(State.class, new StateBuilderImpl().forState(true).build());
		
		domainObjectNullResolver.inject(customer, dependencies);
		
		Assert.assertFalse(dependencies.get(State.class) == customer.state());
		
		
	}
	
	@Test
	public final void get() {
		final DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl(metadataReaderFactory, resourcePatternResolver);
		final Customer customer = EntityUtil.create(CustomerImpl.class);
		ReflectionTestUtils.setField(customer, "id", 19680528L);
		Person person = Mockito.mock(Person.class);
		ReflectionTestUtils.setField(customer, "person", person);
		
		assignCustomer(domainObjectNullResolver, customer);
		
		final Customer result = domainObjectNullResolver.forType(Customer.class);
		Assert.assertEquals(customer, result);
		Assert.assertFalse(customer==result);
		Assert.assertNotNull(result.person());
		Assert.assertFalse(person==result.person());
		
		Assert.assertTrue(customer.id() == result.id());
		
		
	}


	@SuppressWarnings("unchecked")
	private void assignCustomer(final DomainObjectNullResolverImpl domainObjectNullResolver, final Customer customer) {
		((Map<Class<?>, Object>) ReflectionTestUtils.getField(domainObjectNullResolver, "nullObjects")).put(Customer.class, customer);
	}


	@Test(expected=NoSuchBeanDefinitionException.class)
	public void beanExistsGuardTest() {
		final DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl(metadataReaderFactory, resourcePatternResolver);
		domainObjectNullResolver.forType(Customer.class);
	}
}
