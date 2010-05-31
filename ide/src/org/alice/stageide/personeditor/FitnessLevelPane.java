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
package org.alice.stageide.personeditor;


/**
 * @author Dennis Cosgrove
 */
//todo: note, not really inconsequential
class FitnessLevelActionOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	private edu.cmu.cs.dennisc.croquet.BoundedRangeIntegerState fitnessState;
	private int value;
	public FitnessLevelActionOperation( edu.cmu.cs.dennisc.croquet.BoundedRangeIntegerState fitnessState, int value, String name ) {
		super( java.util.UUID.fromString( "979d9be8-c24c-4921-93d4-23747bdf079d" ) );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: FitnessLevelActionOperation" );
		this.fitnessState = fitnessState;
		this.value = value;
		this.setName( name );
	}
	@Override
	protected void performInternal( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		this.fitnessState.setValue( this.value );
	}
}

/**
 * @author Dennis Cosgrove
 */
class FitnessLevelPane extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private edu.cmu.cs.dennisc.croquet.BoundedRangeIntegerState fitnessState = new edu.cmu.cs.dennisc.croquet.BoundedRangeIntegerState( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "8e172c61-c2b6-43e4-9777-e9d8fd2b0d65" ), 0, 50, 100 );
	public FitnessLevelPane() {
		this.addComponent( new FitnessLevelActionOperation( this.fitnessState, this.fitnessState.getMinimum(), "SOFT" ).createButton(), Constraint.WEST );
		this.addComponent( this.fitnessState.createSlider(), Constraint.CENTER );
		this.addComponent( new FitnessLevelActionOperation( this.fitnessState, this.fitnessState.getMaximum(), "CUT" ).createButton(), Constraint.EAST );
		this.fitnessState.addValueObserver( new edu.cmu.cs.dennisc.croquet.BoundedRangeIntegerState.ValueObserver() {
			public void changed(int nextValue) {
				PersonViewer.getSingleton().setFitnessLevel( nextValue*0.01 );
			}
		} );
	}
	public void setFitnessLevel( Double fitnessLevel ) {
		this.fitnessState.setValue( (int)((fitnessLevel+0.005)*100) );
	}
}
