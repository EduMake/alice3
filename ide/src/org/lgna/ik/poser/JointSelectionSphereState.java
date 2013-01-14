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
package org.lgna.ik.poser;

import org.lgna.croquet.CustomItemState;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.State;
import org.lgna.croquet.components.ItemDropDown;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;

/**
 * @author Matt May
 */
public class JointSelectionSphereState extends CustomItemState<JointSelectionSphere> {

	private JointSelectionSphere value;

	public JointSelectionSphereState( JointSelectionSphere initialValue ) {
		super( null, java.util.UUID.fromString( "26646dcd-2003-43af-9939-c3eb61fdf560" ), new ItemCodec<JointSelectionSphere>() {

			public Class<JointSelectionSphere> getValueClass() {
				return JointSelectionSphere.class;
			}

			public JointSelectionSphere decodeValue( BinaryDecoder binaryDecoder ) {
				throw new RuntimeException( "todo" );
			}

			public void encodeValue( BinaryEncoder binaryEncoder, JointSelectionSphere value ) {
				throw new RuntimeException( "todo" );
			}

			public void appendRepresentation( StringBuilder sb, JointSelectionSphere value ) {
				sb.append( value );
			}
		} );
	}

	@Override
	protected void updateSwingModel( JointSelectionSphere nextValue ) {
		this.setValue( nextValue );
	}

	@Override
	protected JointSelectionSphere getActualValue() {
		return this.value;
	}

	public ItemDropDown<JointSelectionSphere, JointSelectionSphereState> createItemDropDown() {
		return new ItemDropDown<JointSelectionSphere, JointSelectionSphereState>( this ) {

			@Override
			protected void handleChanged( State<JointSelectionSphere> state, JointSelectionSphere prevValue, JointSelectionSphere nextValue, boolean isAdjusting ) {
				//				this.mainComponent.setText( nextValue != null ? nextValue.toString() : "null" );
				this.revalidateAndRepaint();
			}
		};
	}

}
