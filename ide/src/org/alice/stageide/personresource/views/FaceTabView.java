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
package org.alice.stageide.personresource.views;

/**
 * @author Dennis Cosgrove
 */
public class FaceTabView extends org.lgna.croquet.components.MigPanel {
	public FaceTabView( org.alice.stageide.personresource.FaceTabComposite composite ) {
		super( composite, "insets 2, fillx", "[right][left, grow, shrink]", "" );
		java.awt.Color backgroundColor = org.alice.stageide.personresource.views.IngredientsView.BACKGROUND_COLOR;
		this.setBackgroundColor( backgroundColor );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );

		org.lgna.croquet.components.List<org.lgna.story.resources.sims2.BaseFace> faceList = new HorizontalWrapList<org.lgna.story.resources.sims2.BaseFace>( composite.getBaseFaceState(), -1 );
		faceList.setBackgroundColor( org.alice.stageide.personresource.views.IngredientsView.BACKGROUND_COLOR );
		this.addComponent( composite.getBaseFaceState().getSidekickLabel().createLabel(), "top" );
		org.lgna.croquet.components.ScrollPane faceScrollPane = new org.lgna.croquet.components.ScrollPane( faceList );
		faceScrollPane.setHorizontalScrollbarPolicy( org.lgna.croquet.components.ScrollPane.HorizontalScrollbarPolicy.NEVER );
		faceScrollPane.setBothScrollBarIncrements( 16, 16 );
		this.addComponent( faceScrollPane, "wrap, grow, shrink" );

		this.addComponent( composite.getBaseEyeColorState().getSidekickLabel().createLabel() );
		final boolean IS_LIST_DESIRED = false;
		if( IS_LIST_DESIRED ) {
			this.addComponent( new HorizontalWrapList( composite.getBaseEyeColorState(), 1 ), "wrap, shrink" );
		} else {
			final java.awt.Insets MARGIN = new java.awt.Insets( 0, -8, 0, -8 ); //todo
			org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.BaseEyeColor> eyeColorState = composite.getBaseEyeColorState();
			org.lgna.story.resources.sims2.BaseEyeColor[] baseEyeColors = org.lgna.story.resources.sims2.BaseEyeColor.values();
			String constraint = "split " + baseEyeColors.length;
			for( org.lgna.story.resources.sims2.BaseEyeColor baseEyeColor : baseEyeColors ) {
				java.awt.Color awtColor = baseEyeColor.getColor();
				org.lgna.croquet.BooleanState itemSelectedState = eyeColorState.getItemSelectedState( baseEyeColor );
				itemSelectedState.initializeIfNecessary();
				itemSelectedState.setTextForBothTrueAndFalse( "" );
				itemSelectedState.setIconForBothTrueAndFalse( new org.alice.ide.swing.icons.ColorIcon( awtColor ) );
				org.lgna.croquet.components.ToggleButton toggleButton = itemSelectedState.createToggleButton();
				toggleButton.tightenUpMargin( MARGIN );
				this.addComponent( toggleButton, constraint );
				constraint = "";
			}
		}
	}
}
