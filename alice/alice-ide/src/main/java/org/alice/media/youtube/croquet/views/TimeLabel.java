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
package org.alice.media.youtube.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class TimeLabel extends org.lgna.croquet.views.Label {
	private static final java.text.SimpleDateFormat MINUTE_SECOND_FORMAT = new java.text.SimpleDateFormat( "mm:ss." );
	private static final java.text.NumberFormat CENTISECOND_FORMAT = new java.text.DecimalFormat( "00" );

	public TimeLabel() {
		this.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextFamily.MONOSPACED );
		this.setHorizontalAlignment( org.lgna.croquet.views.HorizontalAlignment.TRAILING );
	}

	public void setTimeInMilliseconds( long timeInMilliseconds ) {
		java.util.Date date = new java.util.Date( timeInMilliseconds );
		StringBuilder sb = new StringBuilder();
		sb.append( MINUTE_SECOND_FORMAT.format( date ) );
		sb.append( CENTISECOND_FORMAT.format( ( timeInMilliseconds % 1000 ) / 10 ) );
		this.setText( sb.toString() );
	}

	public void setTimeInSeconds( double timeInSeconds ) {
		this.setTimeInMilliseconds( (long)( timeInSeconds * 1000 ) );
	}
}
