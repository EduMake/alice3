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
public abstract class Cascade<B> extends CompletionModel {
	private final Class< B > componentType;
	private final CascadeRoot< B > root;

	private final CascadePopupPrepModel<B> popupPrepModel;
	public Cascade( Group group, java.util.UUID id, Class< B > componentType, CascadeBlank< B >[] blanks ) {
		super( group, id );
		this.componentType = componentType;
		this.root = new CascadeRoot< B >( this );
		assert blanks != null;
		for( int i=0; i<blanks.length; i++ ) {
			assert blanks[ i ] != null : this;
			root.addBlank( blanks[ i ] );
		}
		this.popupPrepModel = new CascadePopupPrepModel<B>( this );
	}
	public Cascade( Group group, java.util.UUID id, Class< B > componentType, CascadeBlank< B > blank ) {
		this( group, id, componentType, new CascadeBlank[] { blank } );
	}
	public CascadePopupPrepModel<B> getPopupPrepModel() {
		return this.popupPrepModel;
	}
	@Override
	protected void localize() {
	}
	@Override
	public org.lgna.croquet.history.Step< ? > fire( org.lgna.croquet.triggers.Trigger trigger ) {
		return null;
	}
	@Override
	public boolean isAlreadyInState( org.lgna.croquet.edits.Edit< ? > edit ) {
		return false;
	}
	public CascadeRoot< B > getRoot() {
		return this.root;
	}
	public Class< B > getComponentType() {
		return this.componentType;
	}
	protected void prologue() {
	}
	protected void epilogue() {
	}
	protected abstract org.lgna.croquet.edits.Edit< ? extends Cascade< B > > createEdit( org.lgna.croquet.history.CascadeCompletionStep< B > step, B[] values );
	private CascadeMenuItemPrepModel menuPrepModel;
	public synchronized CascadeMenuItemPrepModel getMenuItemPrepModel() {
		if( this.menuPrepModel != null ) {
			//pass
		} else {
			this.menuPrepModel = new CascadeMenuItemPrepModel( this );
		}
		return this.menuPrepModel;
	}
	
	public void handleCompletion( org.lgna.croquet.history.CascadeCompletionStep< B > step, org.lgna.croquet.PopupPrepModel.PerformObserver performObserver, B[] values ) {
		try {
			org.lgna.croquet.edits.Edit< ? extends Cascade< B > > edit = this.createEdit( step, values );
			step.commitAndInvokeDo( edit );
		} finally {
			this.getPopupPrepModel().handleFinally( performObserver );
		}
	}
	public void handleCancel( org.lgna.croquet.PopupPrepModel.PerformObserver performObserver, org.lgna.croquet.history.CascadeCompletionStep< B > completionStep, org.lgna.croquet.triggers.Trigger trigger, CancelException ce ) {
		try {
			if( completionStep != null ) {
				completionStep.cancel();
			} else {
				org.lgna.croquet.history.TransactionManager.addCancelCompletionStep( this, trigger );
			}
		} finally {
			this.getPopupPrepModel().handleFinally( performObserver );
		}
	}
	
	
}