package de.mq.merchandise.opportunity.support;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.primefaces.model.TreeNode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class TreeNode2ClassificationConverterImpl implements Converter<TreeNode[], Collection<Classification>>  {

	@Override
	public final  Collection<Classification> convert(final TreeNode[] nodes) {
		
		final Set<Classification> results = new HashSet<>();
		for(final TreeNode node : nodes){
			results.add((Classification) node.getData());
		}
		
		
		return results;
	}

	
	
	
	
}
