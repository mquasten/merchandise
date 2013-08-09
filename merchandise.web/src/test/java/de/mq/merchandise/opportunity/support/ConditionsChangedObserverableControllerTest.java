package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import de.mq.merchandise.opportunity.support.Condition.ConditionType;

public class ConditionsChangedObserverableControllerTest {
	
	private static final String VALUE = "value";

	@Test
	public final void notifyConditionsChanged() {
		final TreeNode node = new DefaultTreeNode();
	    final TreeNode child = new DefaultTreeNode(Mockito.mock(CommercialRelation.class), node);
		
		final Collection<CommercialRelation> commercialRelations = new ArrayList<>();
		final ConditionTreeAO conditionTreeAO = Mockito.mock(ConditionTreeAO.class);
		
		final CommercialRelation commercialRelation = Mockito.mock(CommercialRelation.class);
		
		final Condition condition = Mockito.mock(Condition.class);
		Mockito.when(condition.conditionType()).thenReturn(ConditionType.PricePerUnit);
		final Map<ConditionType,Condition> conditionMap = new HashMap<>();
		conditionMap.put(ConditionType.PricePerUnit, condition);
		Mockito.when(commercialRelation.conditions()).thenReturn(conditionMap);
		
		final List<String> values = new ArrayList<>();
		values.add(VALUE);
		Mockito.when(condition.values()).thenReturn(values);
		
		commercialRelations.add(commercialRelation);
		
		final ConditionsChangedObserverableControllerImpl conditionsChangedObserverableController = new ConditionsChangedObserverableControllerImpl();
		
		Assert.assertTrue(node.getChildren().contains(child));
		conditionsChangedObserverableController.notifyConditionsChanged(commercialRelations, node, conditionTreeAO);
		Assert.assertFalse(node.getChildren().contains(child));
		
		Mockito.verify(conditionTreeAO).setSelected(null);
		
		Assert.assertEquals(1, node.getChildren().size());
		Assert.assertEquals(commercialRelation, node.getChildren().get(0).getData());
		Assert.assertEquals(node, node.getChildren().get(0).getParent());
		
		Assert.assertEquals(1, node.getChildren().get(0).getChildren().size());
		Assert.assertEquals(condition, node.getChildren().get(0).getChildren().get(0).getData());
		
		Assert.assertEquals(node.getChildren().get(0), node.getChildren().get(0).getChildren().get(0).getParent());
		
		Assert.assertEquals(1,node.getChildren().get(0).getChildren().get(0).getChildCount());
		
		Assert.assertEquals(VALUE, node.getChildren().get(0).getChildren().get(0).getChildren().get(0).getData());
		Assert.assertEquals(node.getChildren().get(0).getChildren().get(0),  node.getChildren().get(0).getChildren().get(0).getChildren().get(0).getParent());
		
		
	}

}
