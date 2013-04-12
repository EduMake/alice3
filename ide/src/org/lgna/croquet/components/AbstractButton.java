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
public abstract class AbstractButton<J extends javax.swing.AbstractButton, M extends org.lgna.croquet.Model> extends ViewController<J, M> {
	private static final javax.swing.ButtonModel MODEL_FOR_NULL = new javax.swing.DefaultButtonModel();

	private final String uiDefaultsName;
	private boolean isIconClobbered;
	private javax.swing.Icon clobberIcon;

	public AbstractButton( M model, String uiDefaultsName ) {
		super( model );
		this.uiDefaultsName = uiDefaultsName;
	}

	public boolean isIconClobbered() {
		return this.isIconClobbered;
	}

	public void setIconClobbered( boolean isIconClobbered ) {
		this.isIconClobbered = isIconClobbered;
	}

	public javax.swing.Icon getClobberIcon() {
		return this.clobberIcon;
	}

	public void setClobberIcon( javax.swing.Icon clobberIcon ) {
		this.clobberIcon = clobberIcon;
		this.isIconClobbered = true;
	}

	public int getIconTextGap() {
		return this.getAwtComponent().getIconTextGap();
	}

	public void setIconTextGap( int iconTextGap ) {
		this.getAwtComponent().setIconTextGap( iconTextGap );
	}

	private static final java.awt.Insets ZERO_MARGIN = new java.awt.Insets( 0, 0, 0, 0 );

	public void tightenUpMargin( java.awt.Insets margin ) {
		javax.swing.AbstractButton jButton = this.getAwtComponent();
		if( "javax.swing.plaf.synth.SynthButtonUI".equals( jButton.getUI().getClass().getName() ) ) {
			if( this.uiDefaultsName != null ) {
				if( margin != null ) {
					//pass
				} else {
					int right;
					String text = jButton.getText();
					final int PAD = 4;
					if( ( text != null ) && ( text.length() > 0 ) ) {
						right = PAD + 4;
					} else {
						right = PAD;
					}
					margin = new java.awt.Insets( PAD, PAD, PAD, right );
				}
				javax.swing.UIDefaults uiDefaults = new javax.swing.UIDefaults();
				uiDefaults.put( this.uiDefaultsName + ".contentMargins", margin );
				this.getAwtComponent().putClientProperty( "Nimbus.Overrides", uiDefaults );
			} else {
				java.util.Enumeration<Object> enm = javax.swing.UIManager.getDefaults().keys();
				while( enm.hasMoreElements() ) {
					Object key = enm.nextElement();
					if( key != null ) {
						if( key.toString().endsWith( ".contentMargins" ) ) {
							edu.cmu.cs.dennisc.java.util.logging.Logger.errln( key, javax.swing.UIManager.get( key ) );
						}
					}
				}
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "uiDefaultsName is null:", this );
			}
		} else {
			this.setMargin( margin != null ? margin : ZERO_MARGIN );
		}
	}

	public void tightenUpMargin() {
		this.tightenUpMargin( null );
	}

	/* package-private */void setSwingButtonModel( javax.swing.ButtonModel model ) {
		if( model != null ) {
			//pass
		} else {
			model = MODEL_FOR_NULL;
		}
		if( model != this.getAwtComponent().getModel() ) {
			this.getAwtComponent().setModel( model );
		}
	}

	/* package-private */void setAction( javax.swing.Action action ) {
		if( action != this.getAwtComponent().getAction() ) {
			this.getAwtComponent().setAction( action );
		}
	}

	public void doClick() {
		this.getAwtComponent().doClick();
	}

	public void setHorizontalTextPosition( HorizontalTextPosition horizontalTextPosition ) {
		this.getAwtComponent().setHorizontalTextPosition( horizontalTextPosition.getInternal() );
	}

	public void setVerticalTextPosition( VerticalTextPosition verticalTextPosition ) {
		this.getAwtComponent().setVerticalTextPosition( verticalTextPosition.getInternal() );
	}

	public void setHorizontalAlignment( HorizontalAlignment horizontalAlignment ) {
		this.getAwtComponent().setHorizontalAlignment( horizontalAlignment.getInternal() );
	}

	public void setVerticalAlignment( VerticalAlignment verticalAlignment ) {
		this.getAwtComponent().setVerticalAlignment( verticalAlignment.getInternal() );
	}

	public void setMargin( java.awt.Insets margin ) {
		this.getAwtComponent().setMargin( margin );
	}

	public java.awt.Insets getMargin() {
		return this.getAwtComponent().getMargin();
	}
}
