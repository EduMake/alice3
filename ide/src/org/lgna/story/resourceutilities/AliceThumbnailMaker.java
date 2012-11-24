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
package org.lgna.story.resourceutilities;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;

/**
 * @author Dave Culyba
 */
public class AliceThumbnailMaker extends AbstractThumbnailMaker {
	private static AliceThumbnailMaker instance;

	public static AliceThumbnailMaker getInstance() {
		if( instance == null ) {
			instance = new AliceThumbnailMaker();
		}
		else {
			instance.clear();
		}
		return instance;
	}

	private AliceThumbnailMaker()
	{
		super( AbstractThumbnailMaker.DEFAULT_THUMBNAIL_WIDTH, AbstractThumbnailMaker.DEFAULT_THUMBNAIL_HEIGHT, AbstractThumbnailMaker.DEFAULT_ANTI_ALIAS_FACTOR );
	}

	private AffineMatrix4x4 getThumbnailCameraOrientationForPerson( org.lgna.story.resources.sims2.PersonResource personResource ) {
		if( ( personResource.getLifeStage() == org.lgna.story.resources.sims2.LifeStage.ADULT ) ||
				( personResource.getLifeStage() == org.lgna.story.resources.sims2.LifeStage.ELDER ) ||
				( personResource.getLifeStage() == org.lgna.story.resources.sims2.LifeStage.TEEN ) ) {
			return getThumbnailCameraOrientation( new AxisAlignedBox( -.4, 0, -.4, .4, 1.6, .5 ) );
		}
		else {
			return getThumbnailCameraOrientation( new AxisAlignedBox( -.2, 0, -.2, .2, 1.1, .2 ) );
		}
	}

	public synchronized java.awt.image.BufferedImage createThumbnailFromPersonResource( org.lgna.story.resources.sims2.PersonResource resource ) throws Exception {

		System.out.println( "\n\n\nMAKING THUMBNAIL" );

		org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory factory = org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory.getInstance( resource );
		org.lgna.story.implementation.JointedModelImp.VisualData visualData = factory.createVisualData();
		visualData.setSGParent( this.getModelTransformable() );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getModelTransformable() );
		}
		java.awt.image.BufferedImage returnImage = takePicture( getThumbnailCameraOrientationForPerson( resource ) );
		visualData.setSGParent( null );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( null );
		}
		if( visualData instanceof org.lgna.story.implementation.sims2.NebulousVisualData<?> ) {
			( (org.lgna.story.implementation.sims2.NebulousVisualData<?>)visualData ).unload();
		}
		this.clear();
		System.out.println( "\n\n\nDONE MAKING THUMBNAIL" );

		return returnImage;
	}

	@Override
	protected AffineMatrix4x4 getThumbnailTransform( edu.cmu.cs.dennisc.scenegraph.Visual v, AxisAlignedBox bbox ) {
		return getThumbnailCameraOrientation( bbox );
	}
}
