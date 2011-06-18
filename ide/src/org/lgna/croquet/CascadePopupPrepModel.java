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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public final class CascadePopupPrepModel<T> extends PopupPrepModel {
	private final Cascade<T> cascade;
	/*package-private*/ CascadePopupPrepModel( Cascade< T > cascade ) {
		super( java.util.UUID.fromString( "56116a5f-a081-4ce8-9626-9c515c6c5887" ) );
		this.cascade = cascade;
	}
	@Override
	public Iterable< ? extends Model > getChildren() {
		return edu.cmu.cs.dennisc.java.util.Collections.newLinkedList( this.cascade.getRoot() );
	}

	@Override
	protected Class< ? extends org.lgna.croquet.Model > getClassUsedForLocalization() {
		return this.cascade.getClassUsedForLocalization();
	}
	
	public Cascade< T > getCompletionModel() {
		return this.cascade;
	}
	protected void handleFinally( PerformObserver performObserver ) {
		this.cascade.epilogue();
		performObserver.handleFinally();
	}
	
	@Override
	protected org.lgna.croquet.history.Step< ? > perform( org.lgna.croquet.triggers.Trigger trigger, org.lgna.croquet.PopupPrepModel.PerformObserver performObserver ) {
		final org.lgna.croquet.cascade.RtRoot< T > rtRoot = new org.lgna.croquet.cascade.RtRoot< T >( this.getCompletionModel().getRoot(), performObserver );
		org.lgna.croquet.history.Step< ? > rv;
		if( rtRoot.isGoodToGo() ) {
			rv = rtRoot.complete( new org.lgna.croquet.triggers.AutomaticCompletionTrigger( trigger ) );
			performObserver.handleFinally();
		} else {
			final org.lgna.croquet.history.CascadePopupPrepStep< T > prepStep = org.lgna.croquet.history.TransactionManager.addCascadePopupPrepStep( cascade.getPopupPrepModel(), trigger );			
			final org.lgna.croquet.components.PopupMenu popupMenu = new org.lgna.croquet.components.PopupMenu( cascade.getPopupPrepModel() );
			popupMenu.addComponentListener( new java.awt.event.ComponentListener() {
				public void componentShown( java.awt.event.ComponentEvent e ) {
				}
				public void componentMoved( java.awt.event.ComponentEvent e ) {
				}
				public void componentResized( java.awt.event.ComponentEvent e ) {
					org.lgna.croquet.history.TransactionManager.firePopupMenuResized( prepStep );
				}
				public void componentHidden( java.awt.event.ComponentEvent e ) {
				}
			} );
			popupMenu.addPopupMenuListener( rtRoot.getPopupMenuListener() );
			this.cascade.prologue();
			trigger.showPopupMenu( popupMenu );
			rv = prepStep;
		}
		return rv;
	}
}
