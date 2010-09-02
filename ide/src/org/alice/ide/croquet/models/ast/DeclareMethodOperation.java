/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.croquet.models.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class DeclareMethodOperation extends org.alice.ide.operations.InputDialogWithPreviewOperation<org.alice.ide.declarationpanes.CreateDeclarationPane< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice >> {
	private edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > declaringType;
	public DeclareMethodOperation( java.util.UUID individualId, edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > declaringType ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, individualId );
		this.declaringType = declaringType;
	}
	protected String getDeclarationName(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<org.alice.ide.declarationpanes.CreateDeclarationPane< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice >> context) {
		org.alice.ide.declarationpanes.CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> createMethodPane = context.getMainPanel();
		if( createMethodPane != null ) {
			return createMethodPane.getDeclarationName();
		} else {
			return null;
		}
	}
	protected abstract org.alice.ide.declarationpanes.CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> createCreateMethodPane( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > declaringType );
	@Override
	protected org.alice.ide.declarationpanes.CreateDeclarationPane< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > prologue(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< org.alice.ide.declarationpanes.CreateDeclarationPane< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > > context) {
		assert this.declaringType != null;
		return this.createCreateMethodPane( this.declaringType );
	}
	@Override
	protected void epilogue(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<org.alice.ide.declarationpanes.CreateDeclarationPane< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice >> context, boolean isOk) {
		if( isOk ) {
			org.alice.ide.declarationpanes.CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> createMethodPane = context.getMainPanel();
			final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = createMethodPane.getActualInputValue();
			if( method != null ) {
				final org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
				final edu.cmu.cs.dennisc.alice.ast.AbstractCode prevCode = ide.getFocusedCode();
//				context.commitAndInvokeDo( new org.alice.ide.ToDoEdit() {
//					@Override
//					protected final void doOrRedoInternal( boolean isDo ) {
//						declaringType.methods.add( method );
////						assert method.getDeclaringType() == method.body.getValue().getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.AbstractType.class );
//						ide.setFocusedCode( method );
//					}
//					@Override
//					protected final void undoInternal() {
//						int index = declaringType.methods.indexOf( method );
//						if( index != -1 ) {
//							declaringType.methods.remove( index );
//							ide.setFocusedCode( prevCode );
//						} else {
//							throw new javax.swing.undo.CannotUndoException();
//						}
//					}
//					@Override
//					protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
//						rv.append( "declare:" );
//						edu.cmu.cs.dennisc.alice.ast.NodeUtilities.safeAppendRepr(rv, method, locale);
//						return rv;
//					}
//				} );
				context.commitAndInvokeDo( new org.alice.ide.croquet.edits.ast.DeclareMethodEdit(declaringType, method));
			} else {
				context.cancel();
			}
		} else {
			context.cancel();
		}
	}
	public edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> getDeclaringType() {
		return this.declaringType;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getName() );
		sb.append( "[" );
		if( this.declaringType != null ) {
			sb.append( this.declaringType.getName() );
		}
		sb.append( "]" );
		return sb.toString();
	}
	
}
