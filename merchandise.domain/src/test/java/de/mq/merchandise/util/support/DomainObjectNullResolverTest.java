package de.mq.merchandise.util.support;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;



import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.CollectionUtils;


public class DomainObjectNullResolverTest {
	
	
	
	
	
	private static final IOException IO_EXCEPTION = new IOException("Don't worry only for test");
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
			"de.mq.merchandise.contact.support.CoordinatesImpl"}));
	
	
	@Test
	public final void  entities ()  {
		DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl();
			final Collection<Class<?>> results = domainObjectNullResolver.entities();
			Assert.assertEquals(CLASS_NAMES.size(), results.size());
			
			
			for(final Class<?> clazz : results){
				Assert.assertTrue("Entity is missing: " +clazz.getName() , CLASS_NAMES.contains(clazz.getName()));
				
			}
	
		}
	
	
	@Test(expected=BeanDefinitionStoreException.class)
	public final void entiesResolverException() throws IOException {
		DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl();
		PathMatchingResourcePatternResolver resourcePatternResolver = Mockito.mock(PathMatchingResourcePatternResolver.class);
		ReflectionTestUtils.setField(domainObjectNullResolver, "resourcePatternResolver", resourcePatternResolver);
		Mockito.when(resourcePatternResolver.getResources(Mockito.anyString())).thenThrow(IO_EXCEPTION);
		domainObjectNullResolver.entities();
	}
		
	
	@Test(expected=BeanDefinitionStoreException.class)
	public final void entiesMetaDataFactoryException() throws IOException {
		DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl();
		final MetadataReaderFactory metadataReaderFactory = Mockito.mock(MetadataReaderFactory.class);
		ReflectionTestUtils.setField(domainObjectNullResolver,"metadataReaderFactory", metadataReaderFactory);
		Mockito.when(metadataReaderFactory.getMetadataReader(Mockito.any(Resource.class))).thenThrow(IO_EXCEPTION);
		
		domainObjectNullResolver.entities();
	}
	
	@Test(expected=BeanDefinitionStoreException.class)
	public final void entiesWrongClassException() throws IOException {
		DomainObjectNullResolverImpl domainObjectNullResolver = new DomainObjectNullResolverImpl();
		final MetadataReaderFactory metadataReaderFactory = Mockito.mock(MetadataReaderFactory.class);
		ReflectionTestUtils.setField(domainObjectNullResolver,"metadataReaderFactory", metadataReaderFactory);
		MetadataReader reader = Mockito.mock(MetadataReader.class);
		Mockito.when(metadataReaderFactory.getMetadataReader(Mockito.any(Resource.class))).thenReturn(reader);
		final ClassMetadata classMetadata = Mockito.mock(ClassMetadata.class);
		Mockito.when(reader.getClassMetadata()).thenReturn(classMetadata);
		Mockito.when(classMetadata.getClassName()).thenReturn("don'tLetMeGet");
		domainObjectNullResolver.entities();
	}


}
