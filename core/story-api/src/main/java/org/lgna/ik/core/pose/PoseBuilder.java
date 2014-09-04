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
package org.lgna.ik.core.pose;

import java.util.List;

import org.lgna.story.EmployeesOnly;
import org.lgna.story.Orientation;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.java.util.Lists;

/**
 * @author Matt May
 */
public abstract class PoseBuilder<M extends org.lgna.story.SJointedModel, P extends Pose<M>> {
	//TODO: mmay 
	//package-private with EmployeesOnly accessor 
	public void addJointKey( JointIdQuaternionPair jointKey ) {
		this.keys.add( jointKey );
	}

	protected void addJointKey( JointId jointId, Orientation orientation ) {
		edu.cmu.cs.dennisc.math.UnitQuaternion quaternion = EmployeesOnly.getOrthogonalMatrix3x3( orientation ).createUnitQuaternion();
		this.addJointKey( new JointIdQuaternionPair( jointId, quaternion ) );
	}

	public final PoseBuilder<M, P> arbitraryJoint( JointId jointId, org.lgna.story.Orientation orientation ) {
		this.addJointKey( jointId, orientation );
		return this;
	}

	protected abstract P build( JointIdQuaternionPair[] buffer );

	public final P build() {
		JointIdQuaternionPair[] buffer = new JointIdQuaternionPair[ this.keys.size() ];
		this.keys.toArray( buffer );
		return this.build( buffer );
	}

	private final List<JointIdQuaternionPair> keys = Lists.newLinkedList();
}
