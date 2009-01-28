/**
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package sceneeditor;

import java.awt.Point;

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.lookingglass.PickResult;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class ObjectTranslateDragManipulator extends DragManipulator implements CameraInformedManipulator {

	protected AbstractCamera camera = null;
	protected OnscreenLookingGlass onscreenLookingGlass = null;
	
	protected Point3 initialClickPoint = new Point3();
	protected Plane movementPlane = new edu.cmu.cs.dennisc.math.Plane( 0.0d, 1.0d, 0.0d, 0.0d );
	protected Point3 offsetToOrigin = null;
	
	public void setCamera( AbstractCamera camera ) {
		this.camera = camera;
	}

	public void setOnscreenLookingGlass( OnscreenLookingGlass onscreenLookingGlass ) {
		this.onscreenLookingGlass = onscreenLookingGlass;
	}
	
	@Override
	public void dataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if ( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) && this.manipulatedTransformable != null)
		{
			Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenLookingGlass, this.camera, currentInput.getMouseLocation().x, currentInput.getMouseLocation().y );
			if (pickRay != null)
			{
				Point3 pointInPlane = PlaneUtilities.getPointInPlane( this.movementPlane, pickRay );
				Point3 newPosition = Point3.createAddition( this.offsetToOrigin, pointInPlane );
				this.manipulatedTransformable.setTranslationOnly( newPosition, AsSeenBy.SCENE );
			}
		}
	}

	@Override
	public void endManipulator( InputState endInput, InputState previousInput  ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startManipulator( InputState startInput ) {
		this.manipulatedTransformable = startInput.getCurrentlySelectedObject();
		if (this.manipulatedTransformable != null)
		{
			Transformable t = startInput.getClickPickedTransformable();
			
			startInput.getClickPickResult().getPositionInSource(this.initialClickPoint);
			startInput.getClickPickResult().getSource().transformTo_AffectReturnValuePassedIn( this.initialClickPoint, startInput.getClickPickResult().getSource().getRoot() );
			this.movementPlane = new Plane(this.initialClickPoint, Vector3.createPositiveYAxis());
			
			Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenLookingGlass, this.camera, startInput.getMouseLocation().x, startInput.getMouseLocation().y );
			if (pickRay != null)
			{
				Point3 pointInPlane = PlaneUtilities.getPointInPlane( this.movementPlane, pickRay );
				this.offsetToOrigin = Point3.createSubtraction( this.manipulatedTransformable.getAbsoluteTransformation().translation, pointInPlane );
			}
			else 
			{
				this.manipulatedTransformable = null;
			}
		}
	}

	@Override
	public void timeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub
		
	}


}
