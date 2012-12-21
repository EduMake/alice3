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

package org.lgna.croquet.components;

/**
 * @author Dennis Cosgrove
 */
public class ToolPaletteTabbedPane<E extends org.lgna.croquet.TabComposite<?>> extends AbstractTabbedPane<E> {
	private E card;

	public ToolPaletteTabbedPane( org.lgna.croquet.ListSelectionState<E> model ) {
		super( model );
	}

	@Override
	protected BooleanStateButton<? extends javax.swing.AbstractButton> createTitleButton( E item, org.lgna.croquet.BooleanState itemSelectedState, java.awt.event.ActionListener closeButtonActionListener ) {
		ToolPaletteTitle rv = new ToolPaletteTitle( itemSelectedState );
		rv.setHorizontalAlignment( HorizontalAlignment.LEADING );
		return rv;
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new java.awt.GridBagLayout();
	}

	@Override
	protected void removeAllDetails() {
		this.internalRemoveAllComponents();
	}

	@Override
	protected void handleValueChanged( E card ) {
		if( card != this.card ) {
			if( this.card != null ) {
				this.card.getRootComponent().setVisible( false );
				this.card.handlePostDeactivation();
			}
			this.card = card;
			if( this.card != null ) {
				this.card.handlePreActivation();
				this.card.getRootComponent().setVisible( true );
			}
			this.revalidateAndRepaint();
		}
	}

	@Override
	protected void addPrologue( int count ) {
	}

	@Override
	protected void addItem( E item, BooleanStateButton<?> button ) {
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0f;
		gbc.weighty = 0.0f;
		this.internalAddComponent( button, gbc );
		gbc.weighty = 1.0f;
		item.getRootComponent().setVisible( item == this.getModel().getValue() );
		this.internalAddComponent( item.getRootComponent(), gbc );
	}

	@Override
	protected void addEpilogue() {
	}
}
