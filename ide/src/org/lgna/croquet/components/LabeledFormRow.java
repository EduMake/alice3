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
public class LabeledFormRow implements FormRow {
	private final Label label;
	private final org.lgna.croquet.PlainStringValue labelStringValue;
	private final org.lgna.croquet.components.JComponent<?> trailingComponent;
	private final org.lgna.croquet.components.VerticalAlignment labelVerticalAlignment;
	private final boolean isFillHorizontal;

	private LabeledFormRow( org.lgna.croquet.components.Label label, org.lgna.croquet.PlainStringValue labelStringValue, org.lgna.croquet.components.JComponent<?> trailingComponent, org.lgna.croquet.components.VerticalAlignment labelVerticalAlignment, boolean isFillHorizontal ) {
		this.label = label;
		this.labelStringValue = labelStringValue;
		this.trailingComponent = trailingComponent;
		this.labelVerticalAlignment = labelVerticalAlignment;
		if( this.label != null ) {
			this.label.setVerticalAlignment( this.labelVerticalAlignment );
		}
		this.isFillHorizontal = isFillHorizontal;
	}

	public LabeledFormRow( org.lgna.croquet.PlainStringValue labelStringValue, org.lgna.croquet.components.JComponent<?> component, org.lgna.croquet.components.VerticalAlignment labelVerticalAlignment, boolean isFillHorizontal ) {
		this( null, labelStringValue, component, labelVerticalAlignment, isFillHorizontal );
	}

	public LabeledFormRow( org.lgna.croquet.PlainStringValue labelStringValue, org.lgna.croquet.components.JComponent<?> component, org.lgna.croquet.components.VerticalAlignment labelVerticalAlignment ) {
		this( labelStringValue, component, labelVerticalAlignment, true );
	}

	public LabeledFormRow( org.lgna.croquet.PlainStringValue labelStringValue, org.lgna.croquet.components.JComponent<?> component, boolean isFillHorizontal ) {
		this( labelStringValue, component, org.lgna.croquet.components.VerticalAlignment.CENTER, isFillHorizontal );
	}

	public LabeledFormRow( org.lgna.croquet.PlainStringValue labelStringValue, org.lgna.croquet.components.JComponent<?> component ) {
		this( labelStringValue, component, org.lgna.croquet.components.VerticalAlignment.CENTER );
	}

	public static LabeledFormRow createFromLabel( org.lgna.croquet.components.Label label, org.lgna.croquet.components.JComponent<?> component ) {
		return new LabeledFormRow( label, null, component, org.lgna.croquet.components.VerticalAlignment.CENTER, true );
	}

	protected org.lgna.croquet.components.JComponent<?> createLabel() {
		if( this.labelStringValue != null ) {
			org.lgna.croquet.components.AbstractLabel textField = this.labelStringValue.createLabel();
			textField.setHorizontalAlignment( org.lgna.croquet.components.HorizontalAlignment.TRAILING );
			if( this.labelVerticalAlignment == org.lgna.croquet.components.VerticalAlignment.CENTER ) {
				return textField;
			} else {
				//				org.lgna.croquet.components.BorderPanel.Constraint constraint;
				//				if( this.labelVerticalAlignment == org.lgna.croquet.components.VerticalAlignment.TOP ) {
				//					constraint = org.lgna.croquet.components.BorderPanel.Constraint.PAGE_START;
				//				} else {
				//					constraint = org.lgna.croquet.components.BorderPanel.Constraint.PAGE_END;
				//				}
				//				org.lgna.croquet.components.BorderPanel rv = new org.lgna.croquet.components.BorderPanel();
				//				rv.addComponent( textField, constraint );
				//				textField.makeStandOut();
				//				//				textField.setMaximumSizeClampedToPreferredSize( true );
				//				return rv;
				return textField;
			}
		} else {
			return new org.lgna.croquet.components.Label();
		}
	}

	public void addComponents( org.lgna.croquet.components.FormPanel formPanel ) {
		JComponent<?> leadingComponent;
		if( this.label != null ) {
			leadingComponent = this.label;
		} else {
			leadingComponent = this.createLabel();
		}
		StringBuilder sbTrailing = new StringBuilder();
		if( this.isFillHorizontal ) {
			sbTrailing.append( "growx, " );
		}
		sbTrailing.append( "wrap" );

		StringBuilder sbLeading = new StringBuilder();
		if( this.labelVerticalAlignment == org.lgna.croquet.components.VerticalAlignment.CENTER ) {
			//pass
		} else {
			sbLeading.append( "aligny " );
			sbLeading.append( this.labelVerticalAlignment.toString().toLowerCase() );
		}

		formPanel.addComponent( leadingComponent, sbLeading.toString() );
		formPanel.addComponent( this.trailingComponent, sbTrailing.toString() );
	}
}
