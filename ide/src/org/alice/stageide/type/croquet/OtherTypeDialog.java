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
package org.alice.stageide.type.croquet;

/**
 * @author Dennis Cosgrove
 */
public class OtherTypeDialog extends org.lgna.croquet.SingleValueCreatorInputDialogCoreComposite<org.lgna.croquet.components.Panel, org.lgna.project.ast.AbstractType> {
	private static class SingletonHolder {
		private static OtherTypeDialog instance = new OtherTypeDialog();
	}

	public static OtherTypeDialog getInstance() {
		return SingletonHolder.instance;
	}

	private final TypeTreeState typeTreeState = new TypeTreeState();
	private final ErrorStatus noSelectionError = this.createErrorStatus( this.createKey( "noSelectionError" ) );

	private OtherTypeDialog() {
		super( java.util.UUID.fromString( "58d24fb6-a6f5-4ad9-87b0-dfb5e9e4de41" ) );
	}

	public org.lgna.croquet.TreeSelectionState<TypeNode> getTreeState() {
		return this.typeTreeState;
	}

	@Override
	protected org.lgna.project.ast.AbstractType createValue() {
		TypeNode typeNode = this.typeTreeState.getValue();
		if( typeNode != null ) {
			return typeNode.getType();
		} else {
			return null;
		}
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		TypeNode typeNode = this.typeTreeState.getValue();
		if( typeNode != null ) {
			return IS_GOOD_TO_GO_STATUS;
		} else {
			return this.noSelectionError;
		}
	}

	private static TypeNode build( org.lgna.project.ast.AbstractType<?, ?, ?> type, java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, TypeNode> map ) {
		TypeNode typeNode = map.get( type );
		if( typeNode != null ) {
			//pass
		} else {
			typeNode = new TypeNode( type );
			map.put( type, typeNode );
			org.lgna.project.ast.AbstractType<?, ?, ?> superType = type.getSuperType();
			TypeNode superTypeNode = map.get( superType );
			if( superTypeNode != null ) {
				//pass
			} else {
				superTypeNode = build( superType, map );
			}
			superTypeNode.add( typeNode );
		}
		return typeNode;

	}

	@Override
	public void handlePreActivation() {
		org.lgna.project.Project project = org.alice.ide.ProjectStack.peekProject();
		Iterable<org.lgna.project.ast.NamedUserType> types = project.getNamedUserTypes();
		org.lgna.project.ast.JavaType rootType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SThing.class );
		org.lgna.project.ast.JavaType filterType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.STurnable.class );
		java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, TypeNode> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		TypeNode rootNode = new TypeNode( rootType );
		map.put( rootType, rootNode );
		for( org.lgna.project.ast.NamedUserType type : types ) {
			if( filterType.isAssignableFrom( type ) ) {
				build( type, map );
			}
		}
		this.typeTreeState.setRoot( rootNode );
		super.handlePreActivation();
	}

	@Override
	protected org.lgna.croquet.components.Panel createView() {
		return new org.alice.stageide.type.croquet.views.OtherTypeDialogPane( this );
	}

	public static void main( String[] args ) throws Exception {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
		}

		new org.lgna.croquet.simple.SimpleApplication();

		org.lgna.project.Project project = org.lgna.project.io.IoUtilities.readProject( args[ 0 ] );
		org.alice.ide.ProjectStack.pushProject( project );
		org.lgna.croquet.triggers.Trigger trigger = null;
		OtherTypeDialog.getInstance().getValueCreator().fire( trigger );
		System.exit( 0 );
	}

}
