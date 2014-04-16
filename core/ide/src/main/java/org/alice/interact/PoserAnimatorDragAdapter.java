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
package org.alice.interact;

import org.alice.interact.PickHint.PickType;
import org.alice.interact.condition.DragAndDropCondition;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.condition.MouseDragCondition;
import org.alice.interact.condition.MousePressCondition;
import org.alice.interact.condition.MouseWheelCondition;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.HandleStyle;
import org.alice.interact.handle.ManipulationHandleIndirection;
import org.alice.interact.manipulator.CameraOrbitAboutTargetDragManipulator;
import org.alice.interact.manipulator.ObjectRotateDragManipulator;
import org.alice.stageide.sceneeditor.interact.manipulators.CameraZoomMouseWheelManipulator;
import org.lgna.ik.poser.PoserSphereManipulatorListener;
import org.lgna.ik.poser.scene.AbstractPoserScene;
import org.lgna.ik.poser.scene.PoserPicturePlaneInteraction;
import org.lgna.story.SModel;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

/**
 * @author Matt May
 */
public class PoserAnimatorDragAdapter extends AbstractDragAdapter {

	private PoserPicturePlaneInteraction dragAdapter = null;
	private final AbstractPoserScene poserScene;
	private ManipulatorConditionSet selectObject;
	private static final CameraOrbitAboutTargetDragManipulator orbiter = new CameraOrbitAboutTargetDragManipulator();

	public PoserAnimatorDragAdapter( AbstractPoserScene poserScene ) {
		this.poserScene = poserScene;
		this.setUpControls();
	}

	private void setUpControls() {
		MouseDragCondition middleMouseAndAnything = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON2, new PickCondition( PickHint.getAnythingHint() ) );

		ManipulatorConditionSet cameraOrbit = new ManipulatorConditionSet( orbiter );
		//		cameraOrbit.addCondition(rightMouseAndNonInteractive);
		cameraOrbit.addCondition( middleMouseAndAnything );
		this.manipulators.add( cameraOrbit );

		ManipulationHandleIndirection rotateJointAboutZAxis = new ManipulationHandleIndirection( new org.alice.interact.handle.JointRotationRingHandle( MovementDirection.BACKWARD, Color4f.BLUE ) );
		rotateJointAboutZAxis.setManipulation( new ObjectRotateDragManipulator() );
		rotateJointAboutZAxis.addToSet( HandleSet.JOINT_ROTATION_INTERACTION );
		rotateJointAboutZAxis.addToGroups( HandleSet.HandleGroup.Z_AXIS, HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.JOINT );
		rotateJointAboutZAxis.setDragAdapterAndAddHandle( this );

		ManipulationHandleIndirection rotateJointAboutYAxis = new ManipulationHandleIndirection( new org.alice.interact.handle.JointRotationRingHandle( MovementDirection.UP, Color4f.GREEN ) );
		rotateJointAboutYAxis.setManipulation( new ObjectRotateDragManipulator() );
		rotateJointAboutYAxis.addToSet( HandleSet.JOINT_ROTATION_INTERACTION );
		rotateJointAboutYAxis.addToGroups( HandleSet.HandleGroup.Y_AXIS, HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.JOINT );
		rotateJointAboutYAxis.setDragAdapterAndAddHandle( this );

		ManipulationHandleIndirection rotateJointAboutXAxis = new ManipulationHandleIndirection( new org.alice.interact.handle.JointRotationRingHandle( MovementDirection.LEFT, Color4f.RED ) );
		rotateJointAboutXAxis.setManipulation( new ObjectRotateDragManipulator() );
		rotateJointAboutXAxis.addToSet( HandleSet.JOINT_ROTATION_INTERACTION );
		rotateJointAboutXAxis.addToGroups( HandleSet.HandleGroup.X_AXIS, HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.JOINT );
		rotateJointAboutXAxis.setDragAdapterAndAddHandle( this );

		addHandle( rotateJointAboutXAxis );
		addHandle( rotateJointAboutYAxis );
		addHandle( rotateJointAboutZAxis );

		ManipulatorConditionSet mouseWheelCameraZoom = new ManipulatorConditionSet( new CameraZoomMouseWheelManipulator() );
		MouseWheelCondition mouseWheelCondition = new MouseWheelCondition( new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ) );
		mouseWheelCameraZoom.addCondition( mouseWheelCondition );
		this.manipulators.add( mouseWheelCameraZoom );

		selectObject = new ManipulatorConditionSet( new ObjectRotateDragManipulator() );

		selectObject.setEnabled( false );
		selectObject.addCondition( new MousePressCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.PickType.SELECTABLE.pickHint() ) ) );
		selectObject.addCondition( new DragAndDropCondition() );

		InteractionGroup group = new InteractionGroup( HandleSet.ROTATION_INTERACTION, selectObject, PickType.JOINT );
		this.mapHandleStyleToInteractionGroup.put( HandleStyle.ROTATION, group );
		setInteractionState( HandleStyle.ROTATION );

		this.manipulators.add( selectObject );

		for( int i = 0; i < this.manipulators.size(); i++ ) {
			this.manipulators.get( i ).getManipulator().setDragAdapter( this );
		}
	}

	@Override
	public boolean shouldSnapToRotation() {
		return false;
	}

	@Override
	public boolean shouldSnapToGround() {
		return false;
	}

	@Override
	public boolean shouldSnapToGrid() {
		return false;
	}

	@Override
	public double getGridSpacing() {
		return 1.0;
	}

	@Override
	public edu.cmu.cs.dennisc.math.Angle getRotationSnapAngle() {
		return new edu.cmu.cs.dennisc.math.AngleInRadians( Math.PI / 16.0 );
	}

	@Override
	public void undoRedoEndManipulation( org.alice.interact.manipulator.AbstractManipulator manipulator, AffineMatrix4x4 originalTransformation ) {
	}

	public final void setTarget( SModel model ) {
		orbiter.setTarget( model );
	}

	@Override
	public void setOnscreenLookingGlass( OnscreenLookingGlass onscreenLookingGlass ) {
		super.setOnscreenLookingGlass( onscreenLookingGlass );
		initDragAdapter( onscreenLookingGlass );
	}

	private void initDragAdapter( OnscreenLookingGlass onscreenLookingGlass ) {
		dragAdapter = new PoserPicturePlaneInteraction( poserScene, onscreenLookingGlass );
		dragAdapter.startUp();
	}

	public void addSphereDragListener( PoserSphereManipulatorListener sphereDragListener ) {
		try {
			dragAdapter.addListener( sphereDragListener );
		} catch( NullPointerException e ) {
			Logger.severe( "Drag Adapter cannot be initialized until OnscreenLookingGlass is set" );
			Thread.dumpStack();
		}
	}

	@Override
	protected org.lgna.croquet.SingleSelectListState<HandleStyle> getHandleStyleState() {
		//todo
		return null;
	}

	@Override
	public void setHandleVisibility( boolean isVisible ) {
		super.setHandleVisibility( isVisible );
		selectObject.setEnabled( isVisible );
	}
}
