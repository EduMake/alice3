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

package org.alice.stageide.typecontext.components;


class SelectedTypeView extends org.lgna.croquet.components.BorderPanel {
	private final org.lgna.croquet.components.Label label = new org.lgna.croquet.components.Label( "selected type:" );
	private final org.lgna.croquet.components.Label typeLabel = new org.lgna.croquet.components.Label();
	private final org.lgna.croquet.components.Label snapshotLabel = new org.lgna.croquet.components.Label();
	private final org.lgna.croquet.State.ValueListener< org.lgna.project.ast.NamedUserType > typeListener = new org.lgna.croquet.State.ValueListener< org.lgna.project.ast.NamedUserType >() {
		public void changing( org.lgna.croquet.State< org.lgna.project.ast.NamedUserType > state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.project.ast.NamedUserType > state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
			SelectedTypeView.this.handleTypeStateChanged( nextValue );
		}
	};
	public SelectedTypeView() {
		//this.typeLabel.setHorizontalAlignment( org.lgna.croquet.components.HorizontalAlignment.CENTER );
		this.snapshotLabel.setHorizontalAlignment( org.lgna.croquet.components.HorizontalAlignment.CENTER );
		this.addComponent( new org.lgna.croquet.components.LineAxisPanel( this.label, this.typeLabel ), Constraint.PAGE_START );
		this.addComponent( this.snapshotLabel, Constraint.CENTER );
	}
	private void handleTypeStateChanged( org.lgna.project.ast.NamedUserType nextValue ) {
		this.typeLabel.setIcon( org.alice.ide.common.TypeIcon.getInstance( nextValue ) );
		
		java.awt.image.BufferedImage thumbnail = null;
		String snapshotText = null;
		javax.swing.Icon snapshotIcon = null;
		if( nextValue != null ) {
			org.lgna.project.ast.AbstractType< ?,?,? > snapshotType = org.alice.ide.typemanager.ConstructorArgumentUtilities.getContructor0Parameter0Type( nextValue );
			
			if( snapshotType != null ) {
				if( snapshotType instanceof org.lgna.project.ast.JavaType ) {
					org.lgna.project.ast.JavaType snapShotJavaType = (org.lgna.project.ast.JavaType)snapshotType;
					thumbnail = org.lgna.story.implementation.alice.AliceResourceUtilties.getThumbnail(snapShotJavaType.getClassReflectionProxy().getReification());
				}
				if( thumbnail != null ) {
					snapshotIcon = new javax.swing.ImageIcon(thumbnail);
				} else {
					snapshotIcon = org.alice.ide.croquet.models.gallerybrowser.TypeGalleryNode.getIcon( snapshotType );
				}
				//snapshotIcon = org.alice.stageide.gallerybrowser.ResourceManager.getLargeIconForType( snapshotType );
				//snapshotText = snapshotType.toString();
			} else {
				org.lgna.project.ast.JavaField field = org.alice.ide.typemanager.ConstructorArgumentUtilities.getArgumentField( nextValue.getDeclaredConstructors().get( 0 ) );
				if( field != null ) {
					thumbnail = org.lgna.story.implementation.alice.AliceResourceUtilties.getThumbnail(field.getValueType().getClassReflectionProxy().getReification());
					//snapshotText = field.toString();
					if( thumbnail != null ) {
						snapshotIcon = new javax.swing.ImageIcon(thumbnail);
					}
				}
			}
		}
		this.snapshotLabel.setText( snapshotText );
		this.snapshotLabel.setIcon( snapshotIcon );
		this.revalidateAndRepaint();
	}

	@Override
	protected void handleAddedTo( org.lgna.croquet.components.Component< ? > parent ) {
		super.handleAddedTo( parent );
		org.alice.ide.declarationseditor.TypeState.getInstance().addAndInvokeValueListener( this.typeListener );
	}
	@Override
	protected void handleRemovedFrom( org.lgna.croquet.components.Component< ? > parent ) {
		org.alice.ide.declarationseditor.TypeState.getInstance().removeValueListener( this.typeListener );
		super.handleRemovedFrom( parent );
	}
}

class ReturnToSceneTypeButton extends org.lgna.croquet.components.Button {
	private static javax.swing.Icon BACK_ICON = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( NonSceneTypeView.class.getResource( "images/24/back.png" ) );
	private static class ThumbnailIcon implements javax.swing.Icon {
		private static final int WIDTH = 64;
		private static final int HEIGHT = (WIDTH*3)/4;

		private boolean isDirty = true;
		private java.awt.image.BufferedImage image = null;
		public void markDirty() {
			this.image = null;
			this.isDirty = true;
		}
		public int getIconWidth() {
			return WIDTH;
		}
		public int getIconHeight() {
			return HEIGHT;
		}
		public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
			if( this.isDirty ) {
				//Thread.dumpStack();
				try {
					this.image = org.alice.stageide.sceneeditor.ThumbnailGenerator.createThumbnail( WIDTH, HEIGHT );
				} catch( Throwable t ) {
					this.image = null;
					t.printStackTrace();
				}
				if( this.image != null ) {
					this.image = edu.cmu.cs.dennisc.image.ImageUtilities.createAlphaMaskedImage( this.image, new edu.cmu.cs.dennisc.java.awt.Painter() {
						public void paint( java.awt.Graphics2D g2, int width, int height ) {
							final int N = 15;
							g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
							g2.setComposite( java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.SRC_OVER, 1.0f/N ) );
							g2.setColor( java.awt.Color.BLACK );
							for( int i=0; i<N; i++ ) {
								g2.fillRoundRect( N-i, N-i, width-(N-i)*2+1, height-(N-i)*2+1, N-i, N-i );
							}
						}
					} );
				}
				this.isDirty = false;
			}
			if( this.image != null ) {
				g.drawImage( this.image, 0, 0, null );
			} else {
				int w = this.getIconWidth();
				int h = this.getIconHeight()/2;
				g.setColor( java.awt.Color.BLUE );
				g.fillRect( x, y, w, h );
				g.setColor( java.awt.Color.GREEN );
				g.fillRect( x, y+h, w, h );
			}
		}
	};

	private final ThumbnailIcon thumbnailIcon = new ThumbnailIcon();
	private final org.lgna.croquet.components.Label thumbnailLabel = new org.lgna.croquet.components.Label( thumbnailIcon );
	private final org.lgna.croquet.components.Label typeIconLabel = new org.lgna.croquet.components.Label();
	public ReturnToSceneTypeButton( org.alice.stageide.typecontext.SelectSceneTypeOperation operation ) {
		super( operation );
		javax.swing.JButton jButton = this.getAwtComponent();
		jButton.setLayout( new javax.swing.BoxLayout( jButton, javax.swing.BoxLayout.LINE_AXIS ) );
		org.lgna.croquet.components.LineAxisPanel lineAxisPanel = new org.lgna.croquet.components.LineAxisPanel( 
				new org.lgna.croquet.components.Label( BACK_ICON ),
				new org.lgna.croquet.components.Label( "back to:" ),
				typeIconLabel,
				thumbnailLabel
		);
		
		this.internalAddComponent( new org.lgna.croquet.components.Label( BACK_ICON ) );
		this.internalAddComponent( new org.lgna.croquet.components.Label( "back to:" ) );
		this.internalAddComponent( this.typeIconLabel );
		this.internalAddComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 8 ) );
		this.internalAddComponent( this.thumbnailLabel );
//		
//		thumbnailLabel.setHorizontalAlignment( org.lgna.croquet.components.HorizontalAlignment.CENTER );
//		jButton.add( lineAxisPanel.getAwtComponent(), java.awt.BorderLayout.PAGE_END );
		//jButton.add( thumbnailLabel.getAwtComponent(), java.awt.BorderLayout.CENTER );
	}
	@Override
	protected void handleHierarchyChanged( java.awt.event.HierarchyEvent e ) {
		super.handleHierarchyChanged( e );
		this.thumbnailIcon.markDirty();
		
		//todo:
		org.lgna.project.ast.NamedUserType sceneType = org.alice.ide.IDE.getActiveInstance().getSceneType();
		this.typeIconLabel.setIcon( org.alice.ide.common.TypeIcon.getInstance( sceneType ) );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class NonSceneTypeView extends org.lgna.croquet.components.CornerSpringPanel {
	public NonSceneTypeView( org.alice.stageide.typecontext.NonSceneTypeComposite composite ) {
		super( composite );
//		org.lgna.croquet.components.Button button = org.alice.stageide.typecontext.SelectSceneTypeOperation.getInstance().createButton();
//		button.setVerticalTextPosition( org.lgna.croquet.components.VerticalTextPosition.BOTTOM );
//		button.setHorizontalTextPosition( org.lgna.croquet.components.HorizontalTextPosition.CENTER );
		this.setSouthWestComponent( new ReturnToSceneTypeButton( org.alice.stageide.typecontext.SelectSceneTypeOperation.getInstance() ) );
		this.setNorthWestComponent( new SelectedTypeView() );
		this.setNorthEastComponent( org.alice.stageide.croquet.models.run.RunOperation.getInstance().createButton() );
	}
	@Override
	protected javax.swing.JPanel createJPanel() {
		return new DefaultJPanel() {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return new java.awt.Dimension( 320, 240 );
			}
		};
	}
}
