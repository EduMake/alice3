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

package org.lgna.ik.solver;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lgna.ik.solver.Bone.Axis;

import Jama.Matrix;

import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author Dennis Cosgrove
 */
public class Solver {
	
	private class Constraint {
		private final Map<Bone, Map<Axis, Vector3>> contributions;
		private final edu.cmu.cs.dennisc.math.Vector3 desiredValue;

		public Constraint(Map<Bone, Map<Axis, Vector3>> map, Vector3 desiredValue) {
			this.contributions = map;
			this.desiredValue = desiredValue;
		}
	}
	
	public class JacobianAndInverse {
		private final Matrix jacobian;
		private final Matrix inverseJacobian;

		public JacobianAndInverse(Matrix jacobian, Matrix inverseJacobian) {
			this.jacobian = jacobian;
			this.inverseJacobian = inverseJacobian;
		}
		
		public Matrix getJacobian() {
			return jacobian;
		}
		
		public Matrix getInverseJacobian() {
			return inverseJacobian;
		}
	}
	
	private final java.util.List< Chain > chains = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private final java.util.List< Constraint > constraints = new java.util.ArrayList<Solver.Constraint>();
	//TODO this probably should also know about bone
//	private java.util.Map<org.lgna.ik.solver.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3[]> jacobianColumns;
	private Map<Bone, Map<Axis, Vector3[]>> jacobianColumns;
	private Vector3[] desiredVelocities;
	
	public void addChain( Chain chain ) {
		this.chains.add( chain );
	}
	public void removeChain( Chain chain ) {
		this.chains.remove( chain );
	}
	
	public double calculateErrorForTime(JacobianAndInverse jacobianAndInverse, double dt) {
		//will calculate I-(JJ^-1)

		Matrix jj11 = jacobianAndInverse.getJacobian().times(jacobianAndInverse.getInverseJacobian()).times(-1);
		
		int n = jj11.getRowDimension();
		
		for(int i = 0; i < n; ++i) {
			jj11.set(i, i, 1 + jj11.get(i, i));
		}

		double dtn = dt;
		
		for(int i = 1; i < n; ++i) {
			dtn *= dt;
		}
		
		double error = jj11.det() * dtn;
		
		return error;
	}
	
	public JacobianAndInverse prepareAndCalculateJacobianAndInverse() {
		prepareConstraints();
		if(constraints.size() == 0) {
			System.out.println("no constraints!");
			return null;
		}
		constructJacobianEntries();
		
		Matrix jacobian = createJacobian();
		
		Matrix inverseJacobian = invertJacobian(jacobian, 0.1);
		
		JacobianAndInverse jacobianAndInverse = new JacobianAndInverse(jacobian, inverseJacobian);

		return jacobianAndInverse;
	}
	
	/**
	 * This is called just by itself to solve everything. Use it as an example 
	 * when you are doing adaptive.
	 * @return The angle speeds
	 */
	public Map<Bone, Map<Axis, Double>> solve() {
		JacobianAndInverse jacobianAndInverse = prepareAndCalculateJacobianAndInverse();
		
		return calculateAngleSpeeds(jacobianAndInverse);
	}
	
	public Map<Bone, Map<Axis, Double>> calculateAngleSpeeds(JacobianAndInverse jacobianAndInverse) {
		return calculateAngleSpeeds(jacobianAndInverse.getInverseJacobian());
	}
	
	private Map<Bone, Map<Axis, Double>> calculateAngleSpeeds(Jama.Matrix ji) {
		Jama.Matrix pDot = createDesiredVelocitiesColumn(desiredVelocities);
		Jama.Matrix mThetadot = ji.times(pDot);
		
		Map<Bone, Map<Axis, Double>> boneSpeeds = new HashMap<Bone, Map<Axis,Double>>();
		
		//trusting that this will give me the same order of axes as in createJacobianMatrix(). No reason to doubt this.  
		
		int row = 0;
		for(Entry<Bone, Map<Axis, Vector3[]>> jce: jacobianColumns.entrySet()) {
			Bone bone = jce.getKey();
			Map<Axis, Vector3[]> columnsForBone = jce.getValue();
			
			HashMap<Bone.Axis, Double> axisSpeeds = new HashMap<Bone.Axis, Double>();
			boneSpeeds.put(bone, axisSpeeds);
			
			for(Entry<Axis, Vector3[]> e: columnsForBone.entrySet()) {
				Axis axis = e.getKey();
				
				axisSpeeds.put(axis, mThetadot.get(row, 0));
				
				++row;
			}
		}
		
		return boneSpeeds;
	}
	
	private void prepareConstraints() {
		constraints.clear();
		for(Chain chain: chains) {
			//calculate contributions on the last joint location
			Vector3 desiredLinearVelocity = desiredLinearVelocities.get(chain);
			Vector3 desiredAngularVelocity = desiredAngularVelocities.get(chain);
			
			if(desiredLinearVelocity != null || desiredAngularVelocity != null) {
				chain.updateStateFromJoints();
				
				if(desiredLinearVelocity != null) {
					chain.computeLinearVelocityContributions();
					//give this to a constraint set, together with the desired velocity (chain has it). 
					constraints.add(new Constraint(chain.getLinearVelocityContributions(), desiredLinearVelocity));
				}
				if(desiredAngularVelocity != null) {
					chain.computeAngularVelocityContributions();
					//give this to a constraint set, together with the desired velocity.
					constraints.add(new Constraint(chain.getAngularVelocityContributions(), desiredAngularVelocity));
				}
			}
		}
		desiredLinearVelocities.clear();
		desiredAngularVelocities.clear();
	}
	private void constructJacobianEntries() {
		//need to align axes in different constraints 
		//need to make it into a matrix
		//does making a map with lists make sense?
		//or all lists?
		//I think it's time to get orderly. all lists. 
		
		//pass one, create arrays
		jacobianColumns = new HashMap<Bone, Map<Axis, Vector3[]>>();
		desiredVelocities = new Vector3[constraints.size()];
		for(Constraint constraint: constraints) {
			for(Entry<Bone, Map<Axis, Vector3>> e: constraint.contributions.entrySet()) {
				Bone bone = e.getKey();
				Map<Axis, Vector3> contributionsForBone = e.getValue();
				
				Map<Axis, Vector3[]> columnForBone = new HashMap<Bone.Axis, Vector3[]>();
				jacobianColumns.put(bone, columnForBone);
				
				for(Entry<Axis, Vector3> eb: contributionsForBone.entrySet()) {
					Axis axis = eb.getKey();
					
					if(columnForBone.containsKey(axis)) {
						throw new RuntimeException("Axis already in column");
					}
					
					columnForBone.put(axis, new edu.cmu.cs.dennisc.math.Vector3[constraints.size()]);
				}
			}
		}
		
		//pass two, fill arrays
		for(Entry<Bone, Map<Axis, Vector3[]>> jce: jacobianColumns.entrySet()) {
			Bone bone = jce.getKey();
			Map<Axis, Vector3[]> columnsForBone = jce.getValue();
			for(Entry<Axis, Vector3[]> e: columnsForBone.entrySet()) {
				Axis axis = e.getKey();
				Vector3[] contributions = e.getValue();
				
				int row = 0;
				for(Constraint constraint: constraints) {
					Map<Axis, Vector3> contributionsForBone = constraint.contributions.get(bone);
					
					boolean found = false;
					if(contributionsForBone != null) {
						Vector3 contribution = contributionsForBone.get(axis);
						
						if(contribution != null) {
							found = true;
							contributions[row] = contribution;
						}
					}
					if(!found) {
						contributions[row] = Vector3.accessOrigin();
					}
					
					desiredVelocities[row] = constraint.desiredValue;
					++row;
				}
			}
		}
	}
	
	private Jama.Matrix createJacobian() {
		return createJacobianMatrix(this.jacobianColumns);
	}
	
	private Jama.Matrix invertJacobian(Jama.Matrix jacobian, double sThreshold) {
		Jama.Matrix ji = svdInvert(jacobian, sThreshold);
		// TODO also do any optimizations (don't move too fast, etc)
		
		return ji;
	}
	
	private Jama.Matrix createDesiredVelocitiesColumn(edu.cmu.cs.dennisc.math.Vector3[] desiredVelocities) {
		Jama.Matrix rv = new Jama.Matrix(desiredVelocities.length * 3, 1);

		int row = 0;
		for(edu.cmu.cs.dennisc.math.Vector3 desiredVelocity: desiredVelocities) {
			rv.set(row, 0, desiredVelocity.x);
			rv.set(row + 1, 0, desiredVelocity.y);
			rv.set(row + 2, 0, desiredVelocity.z);
			row += 3;
		}
		
		return rv;
	}
	private Jama.Matrix createJacobianMatrix(Map<Bone, Map<Axis, edu.cmu.cs.dennisc.math.Vector3[]>> jacobianColumns) {
		boolean found = false;
		int numConstraints = 0;
		int numColumns = 0;
		//calculate sizes
		
		for(Entry<Bone, Map<Axis, Vector3[]>> jce: jacobianColumns.entrySet()) {
			Map<Axis, Vector3[]> columnsForBone = jce.getValue();

			if(!found) {
				for(Entry<Axis, Vector3[]> e: columnsForBone.entrySet()) {
					numConstraints = e.getValue().length;
					found = true;
					break;
				}
			}
			
			numColumns += columnsForBone.size();
		}
		
		Jama.Matrix j = new Jama.Matrix(numConstraints * 3, numColumns);
		
		int column = 0;
		
		for(Entry<Bone, Map<Axis, Vector3[]>> jce: jacobianColumns.entrySet()) {
			Map<Axis, Vector3[]> columnsForBone = jce.getValue();
			
			for(Entry<Axis, Vector3[]> e: columnsForBone.entrySet()) {
				Vector3[] contributions = e.getValue();

				int row = 0;
				for(edu.cmu.cs.dennisc.math.Vector3 contribution: contributions) {
					j.set(row, column, contribution.x);
					j.set(row + 1, column, contribution.y);
					j.set(row + 2, column, contribution.z);
					
					row += 3;
				}
				
				++column;
			}
		}
		
		return j;
	}
	
	private Jama.Matrix svdInvert(Jama.Matrix mj, double threshold) {
		
		boolean transposed = false;
		int m = mj.getRowDimension();
		int n = mj.getColumnDimension();
		if(m < n) {
			transposed = true;
		} 

		if(transposed) {
			mj = mj.transpose();
		}
		
		Jama.SingularValueDecomposition svd = new Jama.SingularValueDecomposition(mj);
		
		Jama.Matrix u = svd.getU();
		
		Jama.Matrix s = svd.getS();
		
		Jama.Matrix v = svd.getV();
		
		//reduce s
		assert(s.getRowDimension() == s.getColumnDimension());
		for(int i = 0; i < s.getRowDimension(); ++i) {
			if(s.get(i, i) < threshold) {
//				System.out.printf("A value in S was lower than threshold %f < %f. Correcting.\n", s.get(i, i), threshold);
				s.set(i, i, 0.0);
				assert threshold > 0.0;
			} else {
				s.set(i, i, 1.0 / s.get(i, i));
			}
		}
		
		Jama.Matrix result = v.times(s).times(u.transpose());
		
		if(transposed) {
			result = result.transpose();
		}
		
		return result;
	}
	java.util.Map<Chain, edu.cmu.cs.dennisc.math.Vector3> desiredLinearVelocities = new java.util.HashMap<Chain, edu.cmu.cs.dennisc.math.Vector3>();
	java.util.Map<Chain, edu.cmu.cs.dennisc.math.Vector3> desiredAngularVelocities = new java.util.HashMap<Chain, edu.cmu.cs.dennisc.math.Vector3>();
	public void setDesiredEndEffectorLinearVelocity(Chain chain, edu.cmu.cs.dennisc.math.Vector3 linVelToUse) {
//		chain.setDesiredEndEffectorLinearVelocity(linVelToUse);
		desiredLinearVelocities.put(chain, linVelToUse);
	}
	public void setDesiredEndEffectorAngularVelocity(Chain chain, edu.cmu.cs.dennisc.math.Vector3 angVelToUse) {
//		chain.setDesiredEndEffectorAngularVelocity(angVelToUse);
		desiredAngularVelocities.put(chain, angVelToUse);
	}

}
