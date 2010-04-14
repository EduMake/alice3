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

package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public class KScrollPane extends KComponent< javax.swing.JScrollPane > {
	private javax.swing.JScrollPane jScrollPane = new javax.swing.JScrollPane()	{
		@Override
		public void addNotify() {
			KScrollPane.this.adding();
			super.addNotify();
			KScrollPane.this.added();
		}
		@Override
		public void removeNotify() {
			KScrollPane.this.removing();
			super.removeNotify();
			KScrollPane.this.removed();
		}
	};
	public enum KVerticalScrollbarPolicy {
		NEVER( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER ),
		AS_NEEDED( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ),
		ALWAYS( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		private int internal;
		private KVerticalScrollbarPolicy( int internal ) {
			this.internal = internal;
		}
	}
	public enum KHorizontalScrollbarPolicy {
		NEVER( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ),
		AS_NEEDED( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ),
		ALWAYS( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
		private int internal;
		private KHorizontalScrollbarPolicy( int internal ) {
			this.internal = internal;
		}
	}
	public KScrollPane() {
	}
	public KScrollPane( KComponent viewportView ) {
		this.setViewportView( viewportView );
	}
	public KScrollPane( KComponent viewportView, KVerticalScrollbarPolicy verticalScrollbarPolicy, KHorizontalScrollbarPolicy horizontalScrollbarPolicy ) {
		this.setViewportView( viewportView );
		this.setVerticalScrollbarPolicy( verticalScrollbarPolicy );
		this.setHorizontalScrollbarPolicy( horizontalScrollbarPolicy );
	}
	public KScrollPane( KVerticalScrollbarPolicy verticalScrollbarPolicy, KHorizontalScrollbarPolicy horizontalScrollbarPolicy ) {
		this.setVerticalScrollbarPolicy( verticalScrollbarPolicy );
		this.setHorizontalScrollbarPolicy( horizontalScrollbarPolicy );
	}
	@Override
	protected javax.swing.JScrollPane getJComponent() {
		return this.jScrollPane;
	}
	
	public void setViewportView( KComponent view ) {
		assert view != null;
		this.jScrollPane.setViewportView( view.getJComponent() );
	}
	public void setVerticalScrollbarPolicy( KVerticalScrollbarPolicy verticalScrollbarPolicy ) {
		assert verticalScrollbarPolicy != null;
		this.jScrollPane.setVerticalScrollBarPolicy( verticalScrollbarPolicy.internal );
	}
	public void setHorizontalScrollbarPolicy( KHorizontalScrollbarPolicy horizontalScrollbarPolicy ) {
		assert horizontalScrollbarPolicy != null;
		this.jScrollPane.setHorizontalScrollBarPolicy( horizontalScrollbarPolicy.internal );
	}
}
