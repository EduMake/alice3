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

package test.ik;

import org.lgna.story.Biped;
import org.lgna.story.Camera;
import org.lgna.story.Color;
import org.lgna.story.Cone;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.Model;
import org.lgna.story.MoveDirection;
import org.lgna.story.Position;
import org.lgna.story.Program;
import org.lgna.story.Sphere;
import org.lgna.story.Turn;
import org.lgna.story.TurnDirection;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.math.Point3;

/**
 * @author Dennis Cosgrove
 */
class IkProgram extends Program {
	
	private final Camera camera = new Camera();
	private final Biped ogre = new Biped( org.lgna.story.resources.biped.Ogre.BROWN_OGRE );
	private final Model target = createDragProp();;
	private final IkScene scene = new IkScene( camera, ogre, target );
	private final edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter cameraNavigationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter();
	private final NiceDragAdapter modelManipulationDragAdapter = new NiceDragAdapter();

	private final org.lgna.croquet.State.ValueListener< Boolean > linearAngularEnabledListener = new org.lgna.croquet.State.ValueListener< Boolean >() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
	};
	private final org.lgna.croquet.State.ValueListener< org.lgna.story.resources.JointId > jointIdListener = new org.lgna.croquet.State.ValueListener< org.lgna.story.resources.JointId >() {
		public void changing( org.lgna.croquet.State< org.lgna.story.resources.JointId > state, org.lgna.story.resources.JointId prevValue, org.lgna.story.resources.JointId nextValue, boolean isAdjusting ) {
			IkProgram.this.handleChainChanging();
		}
		public void changed( org.lgna.croquet.State< org.lgna.story.resources.JointId > state, org.lgna.story.resources.JointId prevValue, org.lgna.story.resources.JointId nextValue, boolean isAdjusting ) {
			IkProgram.this.handleChainChanged();
		}
	};
	//SOLVER this is for printing out the chain
	private final org.lgna.croquet.State.ValueListener< org.lgna.ik.solver.Bone > boneListener = new org.lgna.croquet.State.ValueListener< org.lgna.ik.solver.Bone >() {
		public void changing( org.lgna.croquet.State< org.lgna.ik.solver.Bone > state, org.lgna.ik.solver.Bone prevValue, org.lgna.ik.solver.Bone nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.ik.solver.Bone > state, org.lgna.ik.solver.Bone prevValue, org.lgna.ik.solver.Bone nextValue, boolean isAdjusting ) {
			IkProgram.this.handleBoneChanged();
		}
	};
	private final edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener targetTransformListener = new edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener() {
		public void absoluteTransformationChanged(edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent absoluteTransformationEvent) {
			IkProgram.this.handleTargetTransformChanged();
		}
	};
//	private org.lgna.ik.solver.Chain chain;
//	private org.lgna.ik.solver.Solver solver;
	private org.lgna.ik.enforcer.JointedModelIkEnforcer ikEnforcer;
//	protected java.util.Map<org.lgna.ik.solver.Bone.Axis, Double> currentSpeeds;
	
	private org.lgna.story.implementation.SphereImp getTargetImp() {
		return ImplementationAccessor.getImplementation( this.target );
	}
	private Model createDragProp() {
		Sphere mainSphere = new Sphere();
		
		mainSphere.setRadius( 0.13 );
		mainSphere.setPaint( Color.RED );
		mainSphere.setOpacity( 0.5 );
		
		mainSphere.resizeHeight(.7);
		mainSphere.resizeWidth(.5);
		
		Cone cone = new Cone();
		cone.setBaseRadius(.07);
		cone.setLength(.2);
		cone.setPaint(Color.ORANGE);
//		cone.setOpacity(.5);
		cone.resizeWidth(.5);
		cone.resize(.8);
		cone.setVehicle(mainSphere);
		cone.move(MoveDirection.DOWN, .1);
		cone.move(MoveDirection.RIGHT, .05);
		cone.turn(TurnDirection.FORWARD, .25, Turn.asSeenBy(cone.getVehicle()));
		
		return mainSphere;
	}
	private org.lgna.story.implementation.JointedModelImp< ?,? > getSubjectImp() {
		return ImplementationAccessor.getImplementation( this.ogre );
	}
	private org.lgna.story.implementation.JointImp getAnchorImp() {
		org.lgna.story.resources.JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();
		return this.getSubjectImp().getJointImplementation( anchorId );
	}
	private org.lgna.story.implementation.JointImp getEndImp() {
		org.lgna.story.resources.JointId endId = test.ik.croquet.EndJointIdState.getInstance().getValue();
		return this.getSubjectImp().getJointImplementation( endId );
	}
	
	private void handleTargetTransformChanged() {
//		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.getTargetImp().getTransformation( this.getAnchorImp() );
//		edu.cmu.cs.dennisc.print.PrintUtilities.printlns( m );
		this.updateInfo();
	}
	//SOLVER this prints to the yellow area right under the chain display
	private void updateInfo() {
		org.lgna.ik.solver.Bone bone = test.ik.croquet.BonesState.getInstance().getSelectedItem();
		
		StringBuilder sb = new StringBuilder();
		if( bone != null ) {
			org.lgna.story.implementation.JointImp a = bone.getA();
//			org.lgna.story.implementation.JointImp b = bone.getB();
			sb.append( a.getJointId() );
			sb.append( ":\n" );
			edu.cmu.cs.dennisc.print.PrintUtilities.appendLines( sb, a.getLocalTransformation() );
//			sb.append( "\n" );
//			sb.append( b.getJointId() );
//			sb.append( ":\n" );
//			edu.cmu.cs.dennisc.print.PrintUtilities.appendLines( sb, b.getLocalTransformation() );
		}
		sb.append( "\n" );
		sb.append( "target:\n" );
		edu.cmu.cs.dennisc.print.PrintUtilities.appendLines( sb, this.getTargetImp().getLocalTransformation() );
		sb.append( "\n" );
		
		test.ik.croquet.InfoState.getInstance().setValue( sb.toString() );
	}
//	private org.lgna.ik.solver.Chain createChain() {
//		org.lgna.story.resources.JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();
//		org.lgna.story.resources.JointId endId = test.ik.croquet.EndJointIdState.getInstance().getValue();
//		return org.lgna.ik.solver.Chain.createInstance( this.getSubjectImp(), anchorId, endId );
//	}
	
	protected void handleChainChanging() {
		org.lgna.story.resources.JointId endId = test.ik.croquet.EndJointIdState.getInstance().getValue();
		org.lgna.story.resources.JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();
		
		if(endId != null && anchorId != null) {
			ikEnforcer.clearChainBetween(anchorId, endId);
		}
	}

	private void handleChainChanged() {
		org.lgna.story.resources.JointId endId = test.ik.croquet.EndJointIdState.getInstance().getValue();
		org.lgna.story.resources.JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();
		
		if(endId != null && anchorId != null) {
			ikEnforcer.setChainBetween(anchorId, endId);
			setDragAdornmentsVisible(true);
		} else {
			setDragAdornmentsVisible(false);
		}
		updateInfo();
	}
	
//	private void handleChainChanged_old() {
//		//this does not race with the thread. this creates a new one, it might use the old one one more time, which is fine. 
//		
//		if(chain != null) {
//			ikEnforcer.removeChain(chain);
//		}
//		chain = createChain();
//		
//		if(chain != null) {
//			setDragAdornmentsVisible(true);
//			JointImp lastJointImp = chain.getLastJointImp();
//			
//			edu.cmu.cs.dennisc.math.AffineMatrix4x4 ltrans = lastJointImp.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE);
//
//			edu.cmu.cs.dennisc.math.Point3 eePos = new edu.cmu.cs.dennisc.math.Point3(ltrans.translation);
//			eePos.add(edu.cmu.cs.dennisc.math.Point3.createMultiplication(ltrans.orientation.backward, -.2)); //can do something like this to drag fingertips. right now it results in jumping. 
//			chain.setEndEffectorPosition(eePos);
//			
//			//assuming that all are parented to scene...
//			this.getTargetImp().setLocalTransformation( new edu.cmu.cs.dennisc.math.AffineMatrix4x4(chain.getEndEffectorOrientation(), chain.getEndEffectorPosition()) );
//			ikEnforcer.addChain(chain);
//		} else {
//			setDragAdornmentsVisible(false);
//		}
//		
//		test.ik.croquet.BonesState.getInstance().setChain( chain );
//		this.updateInfo();
//	}
	
	protected void targetDragStarted() {
	}
	
	private void setDragAdornmentsVisible(boolean visible) {
		if(visible) {
			scene.anchor.setVehicle(scene);
			scene.ee.setVehicle(scene);
		} else {
			scene.anchor.setVehicle(null);
			scene.ee.setVehicle(null);
		}
	}
	private void initializeTest() {
		this.setActiveScene( this.scene );
		
		this.modelManipulationDragAdapter.setOnClickRunnable(new Runnable() {
			public void run() {
				targetDragStarted();
			}
		});
		
		this.modelManipulationDragAdapter.setOnscreenLookingGlass( ImplementationAccessor.getImplementation( this ).getOnscreenLookingGlass() );
		this.cameraNavigationDragAdapter.setOnscreenLookingGlass( ImplementationAccessor.getImplementation( this ).getOnscreenLookingGlass() );
		this.cameraNavigationDragAdapter.requestTarget( new edu.cmu.cs.dennisc.math.Point3( 0.0, 1.0, 0.0 ) );
		this.cameraNavigationDragAdapter.requestDistance( 8.0 );

		test.ik.croquet.AnchorJointIdState.getInstance().addValueListener( this.jointIdListener );
		test.ik.croquet.EndJointIdState.getInstance().addValueListener( this.jointIdListener );
		test.ik.croquet.BonesState.getInstance().addValueListener( this.boneListener );
		test.ik.croquet.IsLinearEnabledState.getInstance().addValueListener( this.linearAngularEnabledListener );
		test.ik.croquet.IsAngularEnabledState.getInstance().addValueListener( this.linearAngularEnabledListener );

		this.getTargetImp().setTransformation( this.getEndImp() );
		this.getTargetImp().getSgComposite().addAbsoluteTransformationListener( this.targetTransformListener );
		
//		solver = new org.lgna.ik.solver.Solver();
		ikEnforcer = new org.lgna.ik.enforcer.JointedModelIkEnforcer(getSubjectImp());
		
		//TODO use ikEnforcer's methods rather than dealing with chains.
		
		Thread calculateThread = new Thread() {
			@Override
			public void run() {
				while(!interrupted()) {
					//solver has the chain. can also have multiple chains. 
					//I can tell solver, for this chain this is the linear target, etc. 
					//it actually only needs the velocity, etc. then, I should say for this chain this is the desired velocity. ok. 

					java.util.Map<org.lgna.ik.solver.Bone.Axis, Double> desiredSpeedForAxis = new java.util.HashMap<org.lgna.ik.solver.Bone.Axis, Double>();
					
					//not bad concurrent programming practice
					boolean isLinearEnabled = test.ik.croquet.IsLinearEnabledState.getInstance().getValue();
					boolean isAngularEnabled = test.ik.croquet.IsAngularEnabledState.getInstance().getValue();
					
					//these could be multiple. in this app it is one pair.
					final JointId eeId = test.ik.croquet.EndJointIdState.getInstance().getValue();
					final JointId anchorId = test.ik.croquet.AnchorJointIdState.getInstance().getValue();
					
					double maxLinearSpeedForEe = 0.1;
					double maxAngularSpeedForEe = Math.PI / 10.0;
					
					double deltaTime = 0.1;
					
					if(ikEnforcer.hasActiveChain() && (isLinearEnabled || isAngularEnabled)) {
						//I could make chain setter not race with this
						//However, racing is fine, as long as the old chain is still valid. It is.  
						
						edu.cmu.cs.dennisc.math.AffineMatrix4x4 targetTransformation = getTargetImp().getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE);
						if(isLinearEnabled) {
							ikEnforcer.setEeDesiredPosition(eeId, targetTransformation.translation, maxLinearSpeedForEe);
						}
						
						if(isAngularEnabled) {
							ikEnforcer.setEeDesiredOrientation(eeId, targetTransformation.orientation, maxAngularSpeedForEe);
						}
						
						ikEnforcer.advanceTimeForFixedDuration(deltaTime);
												
						Point3 ep = ikEnforcer.getEndEffectorPosition(eeId);
						Point3 ap = ikEnforcer.getAnchorPosition(anchorId);
						scene.anchor.setPositionRelativeToVehicle(new Position(ap.x, ap.y, ap.z));
						scene.ee.setPositionRelativeToVehicle(new Position(ep.x, ep.y, ep.z));
						
						//TODO do this kind of thing as well
						//force bone reprint
						//this should be fine even if the chain is not valid anymore.
						javax.swing.SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								test.ik.croquet.BonesState.getInstance().setChain( ikEnforcer.getChainForPrinting(anchorId, eeId) );
							}
						});
					}
					
					try {
						sleep(10);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		};
		calculateThread.start();
		
		this.handleChainChanged();
	}

	private void handleBoneChanged() {
		this.updateInfo();
	}
	public static void main( String[] args ) {
		IkTestApplication app = new IkTestApplication();
		app.initialize( args );
		app.setPerspective( new test.ik.croquet.IkPerspective() );

		org.lgna.story.resources.JointId initialAnchor = org.lgna.story.resources.BipedResource.RIGHT_CLAVICLE; 
		org.lgna.story.resources.JointId initialEnd = org.lgna.story.resources.BipedResource.RIGHT_WRIST; 

		test.ik.croquet.AnchorJointIdState.getInstance().setValue( initialAnchor );
		test.ik.croquet.EndJointIdState.getInstance().setValue( initialEnd );
		
		test.ik.croquet.IsLinearEnabledState.getInstance().setValue(true);
		test.ik.croquet.IsAngularEnabledState.getInstance().setValue(false);
		
		IkProgram program = new IkProgram();

		test.ik.croquet.SceneComposite.getInstance().getView().initializeInAwtContainer( program );
		program.initializeTest();

		app.getFrame().setSize( 1200, 800 );
		app.getFrame().setVisible( true );
	}
}
