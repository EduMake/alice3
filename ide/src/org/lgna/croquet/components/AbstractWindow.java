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
//todo: better name
public abstract class AbstractWindow<W extends java.awt.Window> extends ScreenElement {
	private static java.util.Map< java.awt.Component, AbstractWindow<?> > map = edu.cmu.cs.dennisc.java.util.Collections.newWeakHashMap();
	/*package-private*/ static AbstractWindow<?> lookup( java.awt.Component component ) {
		if( component != null ) {
			return AbstractWindow.map.get( component );
		} else {
			return null;
		}
	}

	private Layer abovePopupLayer;
	private Layer belowPopupLayer;
	private final W window;
	public AbstractWindow( W window ) {
		this.window = window;
		this.getRootPane().setContentPane( this.contentPanel.getAwtComponent() );
		AbstractWindow.map.put( window, this );
	}

	@Override
	public final W getAwtComponent() {
		return this.window;
	}
	
	public Layer getAbovePopupLayer() {
		if( this.abovePopupLayer != null ) {
			this.abovePopupLayer = new Layer( this, javax.swing.JLayeredPane.POPUP_LAYER+1 );
		}
		return this.abovePopupLayer;
	}
	public Layer getBelowPopupLayer() {
		if( this.belowPopupLayer != null ) {
			this.belowPopupLayer = new Layer( this, javax.swing.JLayeredPane.POPUP_LAYER-1 );
		}
		return this.belowPopupLayer;
	}
	
	@Override
	public org.lgna.croquet.components.AbstractWindow< ? > getRoot() {
		return this;
	}
	protected abstract javax.swing.JRootPane getRootPane();

	private BorderPanel contentPanel = new BorderPanel(); 
	public BorderPanel getContentPanel() {
		return this.contentPanel;
	}
	
	public void addWindowListener( java.awt.event.WindowListener listener ) {
		this.window.addWindowListener( listener );
	}
	public void removeWindowListener( java.awt.event.WindowListener listener ) {
		this.window.removeWindowListener( listener );
	}
	public void addWindowStateListener( java.awt.event.WindowStateListener listener ) {
		this.window.addWindowStateListener( listener );
	}
	public void removeWindowStateListener( java.awt.event.WindowStateListener listener ) {
		this.window.removeWindowStateListener( listener );
	}

	public boolean isVisible() {
		return this.window.isVisible();
	}
	public void setVisible( boolean isVisible ) {
		this.window.setVisible( isVisible );
	}
	
	public int getX() {
		return this.window.getX();
	}
	public int getY() {
		return this.window.getY();
	}
	public java.awt.Point getLocation() {
		return this.window.getLocation();
	}
	public java.awt.Point getLocation( ScreenElement asSeenBy ) {
		if( asSeenBy.getAwtComponent().isVisible() && this.isVisible() ) {
			java.awt.Point ptAsSeenBy = asSeenBy.getAwtComponent().getLocationOnScreen();
			java.awt.Point ptThis = this.getAwtComponent().getLocationOnScreen();
			return new java.awt.Point( ptThis.x-ptAsSeenBy.x, ptThis.y-ptAsSeenBy.y );
		} else {
			return null;
		}
	}

	public void setLocation( java.awt.Point location ) {
		this.window.setLocation( location );
	}
	public void setLocation( int x, int y ) {
		this.window.setLocation( x, y );
	}

	public int getWidth() {
		return this.window.getWidth();
	}
	public int getHeight() {
		return this.window.getHeight();
	}
	public java.awt.Dimension getSize() {
		return this.window.getSize();
	}
	public void setSize( java.awt.Dimension size ) {
		this.window.setSize( size );
	}
	public void setSize( int width, int height ) {
		this.window.setSize( width, height );
	}
	
//	public java.awt.Rectangle getBounds() {
//		return this.window.getBounds();
//	}
//	public java.awt.Rectangle getLocalBounds() {
//		return new java.awt.Rectangle( 0, 0, this.getWidth(), this.getHeight() );
//	}
	public java.awt.Rectangle getBounds( ScreenElement asSeenBy ) {
		java.awt.Point pt = this.getLocation( asSeenBy );
		if( pt != null ) {
			return new java.awt.Rectangle( pt.x, pt.y, this.getWidth(), this.getHeight() );
		} else {
			return null;
		}
	}

	public java.awt.Shape getShape( ScreenElement asSeenBy, java.awt.Insets insets ) {
		return edu.cmu.cs.dennisc.java.awt.RectangleUtilities.inset( this.getBounds( asSeenBy ), insets );
	}
	public java.awt.Shape getVisibleShape( ScreenElement asSeenBy, java.awt.Insets insets ) {
		return this.getShape( asSeenBy, insets );
	}
	public ScrollPane getScrollPaneAncestor() {
		return null;
	}
	public boolean isInView() {
		return this.isVisible();
	}
	
	public TrackableShape getCloseButtonTrackableShape() {
		return new TrackableShape() {
			public java.awt.Shape getShape( ScreenElement asSeenBy, java.awt.Insets insets ) {
				java.awt.Rectangle bounds = AbstractWindow.this.getBounds( asSeenBy );
				if( bounds != null ) {
					bounds.height = bounds.height - AbstractWindow.this.getRootPane().getHeight();
					bounds.height -= 8;
					return edu.cmu.cs.dennisc.java.awt.RectangleUtilities.inset( bounds, insets );
				} else {
					return null;
				}
			}
			public java.awt.Shape getVisibleShape( ScreenElement asSeenBy, java.awt.Insets insets ) {
				return this.getShape( asSeenBy, insets );
			}
			public ScrollPane getScrollPaneAncestor() {
				return null;
			}
			public boolean isInView() {
				return AbstractWindow.this.isInView();
			}
			public void addComponentListener(java.awt.event.ComponentListener listener) {
				AbstractWindow.this.addComponentListener( listener );
			}
			public void removeComponentListener(java.awt.event.ComponentListener listener) {
				AbstractWindow.this.removeComponentListener( listener );
			}
			public void addHierarchyBoundsListener(java.awt.event.HierarchyBoundsListener listener) {
				AbstractWindow.this.addHierarchyBoundsListener( listener );
			}
			public void removeHierarchyBoundsListener(java.awt.event.HierarchyBoundsListener listener) {
				AbstractWindow.this.removeHierarchyBoundsListener( listener );
			}
		};
	}

	public void pack() {
		this.getAwtComponent().pack();
	}

	private static Button lookupButton( javax.swing.JButton jButton ) {
		Component< ? > component = Component.lookup( jButton );
		if( component instanceof Button ) {
			Button button = (Button)component;
			return button;
		} else {
			return null;
		}
	}
	
	public Button getDefaultButton() {
		return lookupButton( this.getRootPane().getDefaultButton() );
	}
	public void setDefaultButton( Button button ) {
		this.getRootPane().setDefaultButton( button.getAwtComponent() );
	}
	
	private java.util.Stack< javax.swing.JButton > defaultJButtonStack;
	public void pushDefaultButton( Button button ) {
		if( this.defaultJButtonStack != null ) {
			//pass
		} else {
			this.defaultJButtonStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
		}
		this.defaultJButtonStack.push( this.getRootPane().getDefaultButton() );
		this.setDefaultButton( button );
	}
	public Button popDefaultButton() {
		Button rv;
		if( this.defaultJButtonStack != null ) {
			if( this.defaultJButtonStack.isEmpty() ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.warning( this.defaultJButtonStack );
				rv = null;
			} else {
				javax.swing.JButton jButton = this.defaultJButtonStack.pop();
				this.getRootPane().setDefaultButton( jButton );
				rv = lookupButton( jButton );
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( this.defaultJButtonStack );
			rv = null;
		}
		return rv;
	}
	
	private org.lgna.croquet.MenuBarComposite menuBarComposite;
	private org.lgna.croquet.Composite<?> mainComposite;

	public org.lgna.croquet.Composite<?> getMainComposite() {
		return this.mainComposite;
	}
	public void setMainComposite( org.lgna.croquet.Composite<?> mainComposite ) {
		if( this.mainComposite != mainComposite ) {
			synchronized( this.getAwtComponent().getTreeLock() ) {
				this.getContentPanel().removeAllComponents();
				if( this.mainComposite != null ) {
					this.mainComposite.handlePostDeactivation();
				}
				this.mainComposite = mainComposite;
				if( this.mainComposite != null ) {
					this.mainComposite.handlePreActivation();
					this.getContentPanel().addComponent( this.mainComposite.getView(), org.lgna.croquet.components.BorderPanel.Constraint.CENTER );
				}
			}
			this.getContentPanel().revalidateAndRepaint();
		}
	}

	public org.lgna.croquet.MenuBarComposite getMenuBarComposite() {
		return this.menuBarComposite;
	}
	
	protected abstract void setJMenuBar( javax.swing.JMenuBar jMenuBar );
	
	public void setMenuBarComposite( org.lgna.croquet.MenuBarComposite menuBarComposite ) {
		if( this.menuBarComposite != menuBarComposite ) {
			synchronized( this.getAwtComponent().getTreeLock() ) {
				if( this.menuBarComposite != null ) {
					this.menuBarComposite.handlePostDeactivation();
				}
				this.menuBarComposite = menuBarComposite;
				javax.swing.JMenuBar jMenuBar;
				if( this.menuBarComposite != null ) {
					this.menuBarComposite.handlePreActivation();
					jMenuBar = menuBarComposite.getView().getAwtComponent();
				} else {
					jMenuBar = null;
				}
				this.setJMenuBar( jMenuBar );
			}

		}
//		try {
//		java.util.List< javax.swing.KeyStroke > keyStrokesToRemove = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
//		javax.swing.JComponent component = this.getAwtComponent().getRootPane();
//		//javax.swing.JComponent component = new javax.swing.JDesktopPane();
//		//javax.swing.JComponent component = this.getAwtComponent().getLayeredPane();
//		
//		//int condition = javax.swing.JComponent.WHEN_FOCUSED;
//		int condition = javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
//		//int condition = javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
//		javax.swing.InputMap inputMap = javax.swing.SwingUtilities.getUIInputMap( component, condition );
//		javax.swing.KeyStroke[] allKeys = inputMap.allKeys();
//		for( javax.swing.KeyStroke keyStroke : allKeys ) {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( keyStroke, inputMap.get( keyStroke ) );
//			if( keyStroke.getKeyCode() == java.awt.event.KeyEvent.VK_F6 ) {
//				keyStrokesToRemove.add( keyStroke );
//			}
//		}
//		for( javax.swing.KeyStroke keyStroke : keyStrokesToRemove ) {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "removing:", keyStroke );
//			inputMap.remove( keyStroke );
//		}
//
//		//javax.swing.SwingUtilities.replaceUIInputMap( component, type, inputMap );
//		inputMap = javax.swing.SwingUtilities.getUIInputMap( component, condition );
//		allKeys = inputMap.allKeys();
//		for( javax.swing.KeyStroke keyStroke : allKeys ) {
//			if( keyStroke.getKeyCode() == java.awt.event.KeyEvent.VK_F6 ) {
//				assert false;
//			}
//		}
//	} catch( Exception e ) {
//		e.printStackTrace();
//	}
	}
	
}
