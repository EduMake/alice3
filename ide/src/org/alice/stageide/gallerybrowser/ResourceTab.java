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

package org.alice.stageide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class ResourceTab extends GalleryTab {
	public static final javax.swing.Icon CREATE_PERSON_LARGE_ICON = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon(GalleryBrowser.class.getResource("images/create_person.png") );
	public static final javax.swing.Icon CREATE_PERSON_SMALL_ICON = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon(GalleryBrowser.class.getResource("images/create_person_24.png") );
	
	static class HorizontalScrollBarPaintOmittingWhenAppropriateJScrollPane extends javax.swing.JScrollPane {
		private static boolean isPaintRequiredFor( javax.swing.JScrollBar jScrollBar ) {
			//edu.cmu.cs.dennisc.java.util.logging.Logger.info( jScrollBar.getMinimum(), jScrollBar.getValue(), jScrollBar.getMaximum(), jScrollBar.getVisibleAmount() );
			return jScrollBar.getMinimum() != jScrollBar.getValue() || jScrollBar.getMaximum() != jScrollBar.getVisibleAmount();
		}
		@Override
		public javax.swing.JScrollBar createHorizontalScrollBar() {
			return new javax.swing.JScrollBar( javax.swing.JScrollBar.HORIZONTAL ) {
				@Override
				public void paint( java.awt.Graphics g ) {
					javax.swing.JScrollBar verticalScrollBar = HorizontalScrollBarPaintOmittingWhenAppropriateJScrollPane.this.getVerticalScrollBar();
					if( isPaintRequiredFor( this ) || isPaintRequiredFor( verticalScrollBar ) ) {
						super.paint( g );
					} else {
						java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
						java.awt.Shape clip = g.getClip();
						g2.setPaint( HorizontalScrollBarPaintOmittingWhenAppropriateJScrollPane.this.getBackground() );
						g2.fill( clip );
					}
				}
			};
		}
	}

	private static class SingletonHolder {
		private static ResourceTab instance = new ResourceTab();
	}
	public static ResourceTab getInstance() {
		return SingletonHolder.instance;
	}
	private ResourceTab() {
		super( java.util.UUID.fromString( "811380db-5339-4a2e-84e3-695b502188af" ) );
	}
	@Override
	public void customizeTitleComponent( org.lgna.croquet.BooleanState booleanState, org.lgna.croquet.components.BooleanStateButton< ? > button ) {
		super.customizeTitleComponent( booleanState, button );
		booleanState.setIconForBothTrueAndFalse( org.alice.ide.icons.Icons.EMPTY_HEIGHT_ICON_SMALL );
	}
	@Override
	protected org.lgna.croquet.components.View< ?, ? > createView() {
		class ResourceView extends org.lgna.croquet.components.BorderPanel {
			public ResourceView() {
				org.lgna.croquet.components.BorderPanel topPanel = new org.lgna.croquet.components.BorderPanel();
				topPanel.addComponent( new org.lgna.croquet.components.TreePathViewController( org.alice.ide.croquet.models.gallerybrowser.GalleryResourceTreeSelectionState.getInstance() ), Constraint.LINE_START );
				org.lgna.croquet.components.TextField filterTextField = FilterStringState.getInstance().createTextField();
				filterTextField.setMinimumPreferredWidth( 320 );
				filterTextField.setMaximumSizeClampedToPreferredSize( true );
				filterTextField.getAwtComponent().setTextForBlankCondition( "search entire gallery" );
				filterTextField.scaleFont( 1.5f );

				topPanel.addComponent( filterTextField, Constraint.LINE_END );

				org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( new GalleryDirectoryViewController() ) {
					@Override
					protected javax.swing.JScrollPane createAwtComponent() {
						return new HorizontalScrollBarPaintOmittingWhenAppropriateJScrollPane();
					}
				};
				scrollPane.setHorizontalScrollbarPolicy( org.lgna.croquet.components.ScrollPane.HorizontalScrollbarPolicy.ALWAYS );
				scrollPane.setBorder( null );
		        scrollPane.setBothScrollBarIncrements( 12, 24 );

				this.addComponent( topPanel, Constraint.PAGE_START );
				this.addComponent( scrollPane, Constraint.CENTER );

//				org.alice.stageide.croquet.models.gallerybrowser.CreateFieldFromPersonResourceOperation createTypeFromPersonResourceOperation = org.alice.stageide.croquet.models.gallerybrowser.CreateFieldFromPersonResourceOperation.getInstance();
//				org.lgna.croquet.components.Button createPersonButton = createTypeFromPersonResourceOperation.createButton();
//				createPersonButton.setHorizontalTextPosition( org.lgna.croquet.components.HorizontalTextPosition.CENTER );
//				createPersonButton.setVerticalTextPosition( org.lgna.croquet.components.VerticalTextPosition.BOTTOM );
//
//				createTypeFromPersonResourceOperation.setSmallIcon( CREATE_PERSON_LARGE_ICON );
//				
//				this.addComponent( createPersonButton, Constraint.LINE_START );
				
				org.lgna.croquet.components.PageAxisPanel lineEndPanel = new org.lgna.croquet.components.PageAxisPanel();
				lineEndPanel.addComponent( org.alice.stageide.croquet.models.declaration.BillboardFieldDeclarationOperation.getInstance().createButton() );
				lineEndPanel.addComponent( org.alice.stageide.croquet.models.declaration.AxesFieldDeclarationOperation.getInstance().createButton() );
				lineEndPanel.addComponent( org.alice.stageide.croquet.models.declaration.ConeFieldDeclarationOperation.getInstance().createButton() );
				lineEndPanel.addComponent( org.alice.stageide.croquet.models.declaration.SphereFieldDeclarationOperation.getInstance().createButton() );
				this.addComponent( lineEndPanel, Constraint.LINE_END );
				//todo
				this.setBackgroundColor( GalleryBrowser.BACKGROUND_COLOR );
				scrollPane.setBackgroundColor( GalleryBrowser.BACKGROUND_COLOR );
			}
		}
		return new ResourceView();
	}
	@Override
	public boolean contains( org.lgna.croquet.Model model ) {
		//todo
		return false;
	}
}
