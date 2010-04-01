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
package org.alice.ide.operations.ast;

import edu.cmu.cs.dennisc.alice.ast.NodeListProperty;
import edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice;

/**
 * @author Dennis Cosgrove
 */
public class DeleteParameterOperation extends AbstractCodeParameterOperation {
	public DeleteParameterOperation( NodeListProperty< ParameterDeclaredInAlice > parametersProperty, edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter ) {
		super( parametersProperty, parameter );
		this.putValue( javax.swing.Action.NAME, "Delete" );
	}
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		final java.util.Map< edu.cmu.cs.dennisc.alice.ast.MethodInvocation, edu.cmu.cs.dennisc.alice.ast.Argument > map = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.MethodInvocation, edu.cmu.cs.dennisc.alice.ast.Argument >();
		final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( this.getCode(), edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class );
		final int index = method.parameters.indexOf( this.getParameter() );
		if( method != null && index >= 0 ) {
			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.ParameterAccess > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.ParameterAccess >( edu.cmu.cs.dennisc.alice.ast.ParameterAccess.class ) {
				@Override
				protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.ParameterAccess parameterAccess ) {
					return parameterAccess.parameter.getValue() == getParameter();
				}
			};
			method.crawl( crawler, false );
			java.util.List< edu.cmu.cs.dennisc.alice.ast.ParameterAccess > parameterAccesses = crawler.getList();
			final int N_ACCESSES = parameterAccesses.size();

			java.util.List< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > methodInvocations = this.getIDE().getMethodInvocations( method );
			final int N_INVOCATIONS = methodInvocations.size();
			if( N_ACCESSES > 0 ) {
				StringBuffer sb = new StringBuffer();
				sb.append( "<html><body>There " );
				if( N_ACCESSES == 1 ) {
					sb.append( "is 1 access" );
				} else {
					sb.append( "are " );
					sb.append( N_ACCESSES );
					sb.append( " accesses" );
				}
				sb.append( " to this parameter.<br>You must remove the " );
				if( N_ACCESSES == 1 ) {
					sb.append( "access" );
				} else {
					sb.append( "accesses" );
				}
				sb.append( " before you may delete the parameter.<br>Cancelling.</body></html>" );
				javax.swing.JOptionPane.showMessageDialog( this.getIDE(), sb.toString() );
				actionContext.cancel();
			} else {
				if( N_INVOCATIONS > 0 ) {
					String codeText;
					if( method.isProcedure() ) {
						codeText = "procedure";
					} else {
						codeText = "function";
					}
					StringBuffer sb = new StringBuffer();
					sb.append( "<html><body>There " );
					if( N_INVOCATIONS == 1 ) {
						sb.append( "is 1 invocation" );
					} else {
						sb.append( "are " );
						sb.append( N_INVOCATIONS );
						sb.append( " invocations" );
					}
					sb.append( " to this " );
					sb.append( codeText );
					sb.append( " in your program.<br>Deleting this parameter will also delete the arguments from those " );
					if( N_INVOCATIONS == 1 ) {
						sb.append( "invocation" );
					} else {
						sb.append( "invocations" );
					}
					sb.append( "<br>Would you like to continue with the deletion?</body></html>" );
					int result = javax.swing.JOptionPane.showConfirmDialog(this.getIDE(), sb.toString(), "Delete Parameter", javax.swing.JOptionPane.YES_NO_CANCEL_OPTION );
					if( result == javax.swing.JOptionPane.YES_OPTION ){
						//pass
					} else {
						actionContext.cancel();
					}
				}
			}
			if( actionContext.isCancelled() ) {
				//pass
			} else {
				actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
					@Override
					public void doOrRedo( boolean isDo ) {
						org.alice.ide.ast.NodeUtilities.removeParameter( map, method, getParameter(), index, getIDE().getMethodInvocations( method ) );
					}
					@Override
					public void undo() {
						org.alice.ide.ast.NodeUtilities.addParameter( map, method, getParameter(), index, getIDE().getMethodInvocations( method ) );
					}
					@Override
					protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
						rv.append( "delete:" );
						edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr(rv, getParameter(), locale);
						return rv;
					}
				} );
			}
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: DeleteParameterOperation" );
			actionContext.cancel();
		}
	}
}
