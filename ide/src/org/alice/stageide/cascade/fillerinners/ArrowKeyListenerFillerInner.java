package org.alice.stageide.cascade.fillerinners;

public class ArrowKeyListenerFillerInner extends org.alice.ide.cascade.fillerinners.ExpressionFillerInner {
	public ArrowKeyListenerFillerInner() {
		super( org.lgna.story.event.ArrowKeyPressListener.class );
	}
	@Override
	public void appendItems( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.project.annotations.ValueDetails< ? > details, boolean isTop, org.lgna.project.ast.Expression prevExpression ) {
		rv.add( org.alice.stageide.croquet.models.cascade.adapters.ArrowKeyAdapterFillIn.getInstance() );
	}
}
