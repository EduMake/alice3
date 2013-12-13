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
package org.alice.ide.croquet.models.help.views;

/**
 * @author Dennis Cosgrove
 */
public class AbstractIssueView extends org.lgna.croquet.views.BorderPanel {
	protected static org.lgna.croquet.views.JComponent<?> createScrollPaneTextArea( org.lgna.croquet.StringState stringState ) {
		org.lgna.croquet.views.TextArea textArea = stringState.createTextArea();
		textArea.getAwtComponent().setLineWrap( true );
		textArea.getAwtComponent().setWrapStyleWord( true );
		org.lgna.croquet.views.ScrollPane rv = new org.lgna.croquet.views.ScrollPane( textArea );
		rv.setMinimumPreferredHeight( 128 );
		return rv;
	}

	public AbstractIssueView( org.alice.ide.croquet.models.help.AbstractIssueComposite<?> composite ) {
		super( composite );

		org.lgna.croquet.views.Button submitButton = composite.getSubmitBugOperation().createButton();
		submitButton.scaleFont( 1.6f );
		submitButton.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );

		org.lgna.croquet.views.FlowPanel submitPanel = new org.lgna.croquet.views.FlowPanel( org.lgna.croquet.views.FlowPanel.Alignment.CENTER );
		submitPanel.addComponent( submitButton );

		org.lgna.croquet.views.PageAxisPanel pageEndPanel = new org.lgna.croquet.views.PageAxisPanel( new org.lgna.croquet.views.HorizontalSeparator(), org.lgna.croquet.views.BoxUtilities.createVerticalSliver( 8 ), submitPanel );
		pageEndPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );

		this.addPageEndComponent( pageEndPanel );
	}

}
