package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;


import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

public class ClassificationTreeChangedObserveableControllerTest {
	
	final private ClassificationTreeChangedObserveableControllerImpl classificationTreeChangedObserveableController= new ClassificationTreeChangedObserveableControllerImpl();
	
	final private TreeNode root = new DefaultTreeNode();
	
	final private ActivityClassification kylie = Mockito.mock(ActivityClassification.class);
	
	final private ActivityClassification cantGetYourOutOfMyHead = Mockito.mock(ActivityClassification.class);
	
	final private ActivityClassification spinningAround = Mockito.mock(ActivityClassification.class);
	
	final private ActivityClassification gaga = Mockito.mock(ActivityClassification.class);
	
	final private ActivityClassification bornThisWay = Mockito.mock(ActivityClassification.class);
	
	
	public final Collection<Classification>  classifications () {
		final Collection<Classification> classifications = new HashSet<>();
		Mockito.when(kylie.id()).thenReturn("KYLIE");
		Mockito.when(kylie.description()).thenReturn("Kylie Minogue");
		
		Mockito.when(cantGetYourOutOfMyHead.id()).thenReturn("KYLIE-01");
		Mockito.when(cantGetYourOutOfMyHead.description()).thenReturn("Can't get you out of my head");
		Mockito.when(cantGetYourOutOfMyHead.parent()).thenReturn(kylie);
		
		Mockito.when(spinningAround.id()).thenReturn("KYLIE-02");
		Mockito.when(spinningAround.description()).thenReturn("Spinning around");
		Mockito.when(spinningAround.parent()).thenReturn(kylie);
		
		Mockito.when(gaga.id()).thenReturn("LADYGAGA");
		Mockito.when(gaga.description()).thenReturn("Lady Gaga");
		
		Mockito.when(bornThisWay.id()).thenReturn("LADYGAGA-01");
		Mockito.when(bornThisWay.description()).thenReturn("Born This Way");
		Mockito.when(bornThisWay.parent()).thenReturn(gaga);
		
		classifications.add(gaga);
		classifications.add(bornThisWay);
		
		classifications.add(spinningAround);
		classifications.add(kylie);
		classifications.add(cantGetYourOutOfMyHead);
		return classifications;
	}
	
	
	@Test
	public final void createTree() {
		
		classificationTreeChangedObserveableController.notifyClassificationsChanged(classifications(), root);
		
		Assert.assertEquals(2, root.getChildCount());
		Assert.assertEquals(kylie, root.getChildren().get(0).getData());
		Assert.assertEquals(root, root.getChildren().get(0).getParent());
		
		Assert.assertEquals(cantGetYourOutOfMyHead, root.getChildren().get(0).getChildren().get(0).getData());
		Assert.assertEquals(root.getChildren().get(0), root.getChildren().get(0).getChildren().get(0).getParent());
		Assert.assertEquals(2,  root.getChildren().get(0).getChildCount());
		Assert.assertEquals(spinningAround, root.getChildren().get(0).getChildren().get(1).getData());
		Assert.assertEquals(root.getChildren().get(0), root.getChildren().get(0).getChildren().get(1).getParent());
		
		
		Assert.assertEquals(gaga,  root.getChildren().get(1).getData());
		Assert.assertEquals(root,  root.getChildren().get(1).getParent());
		
		Assert.assertEquals(1,  root.getChildren().get(1).getChildCount());
		Assert.assertEquals(bornThisWay,  root.getChildren().get(1).getChildren().get(0).getData());
		Assert.assertEquals(root.getChildren().get(1), root.getChildren().get(1).getChildren().get(0).getParent());
	}
	
	
	@Test
	public final void select() {
		classificationTreeChangedObserveableController.notifyClassificationsChanged(classifications(), root);
		
		final Collection<Classification> selected = new ArrayList<>();
		
		selected.add(cantGetYourOutOfMyHead);
		
		for(final TreeNode node : root.getChildren()){
			Assert.assertFalse(node.isExpanded());
			Assert.assertFalse(node.isSelected());
			for(final TreeNode child : node.getChildren()){
				Assert.assertFalse(child.isExpanded());
				Assert.assertFalse(child.isSelected());
			}
		}
		
		classificationTreeChangedObserveableController.notifyClassificationChanged(selected, root);
		
		for(TreeNode node : root.getChildren()){
			Assert.assertFalse(node.isSelected());
			if( node.getData().equals(kylie)){
			    Assert.assertTrue(node.isExpanded());
			} else {
				Assert.assertFalse(node.isExpanded());
			}
			for(final TreeNode child : node.getChildren()){
				Assert.assertFalse(child.isExpanded());
				if( child.getData().equals(cantGetYourOutOfMyHead) ) {
					Assert.assertTrue(child.isSelected());
				} else {
					Assert.assertFalse(child.isSelected());
				}
			}
		}
	}

}
