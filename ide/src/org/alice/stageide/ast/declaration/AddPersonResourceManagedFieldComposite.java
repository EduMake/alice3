/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.stageide.ast.declaration;

/**
 * @author Dennis Cosgrove
 */
public class AddPersonResourceManagedFieldComposite extends org.alice.ide.ast.declaration.AddManagedFieldComposite {
	private static class SingletonHolder {
		private static AddPersonResourceManagedFieldComposite instance = new AddPersonResourceManagedFieldComposite();
	}

	public static AddPersonResourceManagedFieldComposite getInstance() {
		return SingletonHolder.instance;
	}

	private static org.lgna.project.ast.AbstractType<?, ?, ?> getDeclaringTypeFromInitializer( org.lgna.project.ast.Expression expression ) {
		if( expression instanceof org.lgna.project.ast.InstanceCreation ) {
			org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)expression;
			return instanceCreation.constructor.getValue().getDeclaringType();
		} else {
			return null;
		}
	}

	private org.lgna.project.ast.InstanceCreation initialInstanceCreation;

	private AddPersonResourceManagedFieldComposite() {
		super( java.util.UUID.fromString( "a3484b6d-bee1-40b6-9162-a8066bd9b015" ), new FieldDetailsBuilder()
				.valueComponentType( ApplicabilityStatus.DISPLAYED, null )
				.valueIsArrayType( ApplicabilityStatus.APPLICABLE_BUT_NOT_DISPLAYED, false )
				.initializer( ApplicabilityStatus.EDITABLE, null )
				.build() );
	}

	public void setPersonResource( org.lgna.story.resources.sims2.PersonResource personResource ) {
		if( personResource != null ) {
			try {
				org.lgna.project.ast.InstanceCreation argumentExpression = org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSims2PersonRecourseInstanceCreation( personResource );

				org.lgna.project.ast.NamedUserType type = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFromPersonResource( personResource );

				this.initialInstanceCreation = org.lgna.project.ast.AstUtilities.createInstanceCreation(
						type.getDeclaredConstructors().get( 0 ),
						argumentExpression
						);
			} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
				this.initialInstanceCreation = null;
			}
		} else {
			this.initialInstanceCreation = null;
		}
	}

	@Override
	protected org.lgna.project.ast.Expression getInitializerInitialValue() {
		return this.initialInstanceCreation;
	}

	@Override
	protected org.lgna.project.ast.AbstractType<?, ?, ?> getValueComponentTypeInitialValue() {
		return getDeclaringTypeFromInitializer( this.getInitializer() );
	}

	private class InitializerContext implements org.alice.ide.cascade.ExpressionCascadeContext {
		public org.lgna.project.ast.Expression getPreviousExpression() {
			//todo: investigate
			return getInitializer();
		}

		public org.alice.ide.ast.draganddrop.BlockStatementIndexPair getBlockStatementIndexPair() {
			return null;
		}
	}

	private class PersonResourceInitializerCustomizer implements ItemStateCustomizer<org.lgna.project.ast.Expression> {
		private org.alice.ide.cascade.ExpressionCascadeContext pushedContext;

		public org.lgna.croquet.CascadeFillIn getFillInFor( org.lgna.project.ast.Expression value ) {
			return null;
		}

		public void prologue( org.lgna.croquet.triggers.Trigger trigger ) {
			this.pushedContext = new InitializerContext();
			org.alice.ide.IDE.getActiveInstance().getExpressionCascadeManager().pushContext( this.pushedContext );
		}

		public void epilogue() {
			org.alice.ide.IDE.getActiveInstance().getExpressionCascadeManager().popAndCheckContext( this.pushedContext );
			this.pushedContext = null;
		}

		public void appendBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode ) {
			rv.add( org.alice.stageide.personresource.PersonResourceComposite.getInstance().getPreviousExpressionValueConverter().getFillIn() );
		}
	}

	@Override
	protected java.awt.Dimension calculateWindowSize( org.lgna.croquet.components.AbstractWindow<?> window ) {
		return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMaximumHeight( super.calculateWindowSize( window ), 400 );
	}

	@Override
	protected org.lgna.croquet.AbstractComposite.ItemStateCustomizer<org.lgna.project.ast.Expression> createInitializerCustomizer() {
		return new PersonResourceInitializerCustomizer();
	}

	@Override
	protected void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		super.handlePostHideDialog( completionStep );
		this.initialInstanceCreation = null;
	}
}
