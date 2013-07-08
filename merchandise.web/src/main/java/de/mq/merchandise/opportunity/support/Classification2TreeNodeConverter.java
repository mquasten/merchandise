package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class Classification2TreeNodeConverter  implements Converter<Collection<Classification>,TreeNode[]>{

	static  final TreeNode ROOT = new DefaultTreeNode(new ActivityClassificationImpl("headline", null), null);
	@Override
	public final TreeNode[] convert(final Collection<Classification> classifications) {
		System.out.println("read:" + classifications.size());
		final List<TreeNode> results = new ArrayList<>();
		for(final Classification classification : classifications){
			TreeNode parent = ROOT;
			if( classification.parent() != null){
				parent=new DefaultTreeNode(classification.parent(), ROOT);
			}
			results.add(new DefaultTreeNode(classification, parent));
		}
		
		return results.toArray(new TreeNode[results.size()]);
	
	}

	

	

	
}
