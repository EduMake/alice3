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

package org.alice.stageide.ast;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionCreator extends org.alice.ide.ast.ExpressionCreator {
	private org.lgna.project.ast.Expression createPositionExpression( org.lgna.story.Position position ) {
		if( position != null ) {
			Class< ? > cls = org.lgna.story.Position.class;
			org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance( cls, Number.class, Number.class, Number.class );
			return org.alice.ide.ast.AstUtilities.createInstanceCreation( 
					constructor, 
					this.createDoubleExpression( position.getRight() ),
					this.createDoubleExpression( position.getUp() ), 
					this.createDoubleExpression( position.getBackward() ) 
			);
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}
	private org.lgna.project.ast.Expression createOrientationExpression( org.lgna.story.Orientation orientation ) {
		if( orientation != null ) {
			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes = org.lgna.story.ImplementationAccessor.getOrthogonalMatrix3x3( orientation );
			edu.cmu.cs.dennisc.math.UnitQuaternion q = new edu.cmu.cs.dennisc.math.UnitQuaternion( axes );
			Class< ? > cls = org.lgna.story.Orientation.class;
			org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance( cls, Number.class, Number.class, Number.class, Number.class);
			return org.alice.ide.ast.AstUtilities.createInstanceCreation( constructor, 
					this.createDoubleExpression( q.x ), 
					this.createDoubleExpression( q.y ), 
					this.createDoubleExpression( q.z ), 
					this.createDoubleExpression( q.w ) 
			);
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}
	private org.lgna.project.ast.Expression createScaleExpression( org.lgna.story.Scale scale ) {
		if( scale != null ) {
			Class< ? > cls = org.lgna.story.Scale.class;
			org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance( cls, Number.class, Number.class, Number.class );
			return org.alice.ide.ast.AstUtilities.createInstanceCreation( 
					constructor, 
					this.createDoubleExpression( scale.getLeftToRight() ), 
					this.createDoubleExpression( scale.getBottomToTop() ), 
					this.createDoubleExpression( scale.getFrontToBack() ) 
			);
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}

	private org.lgna.project.ast.Expression createFontExpression( org.lgna.story.Font font ) {
		Class< ? > cls = org.lgna.story.Font.class;
		org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance( cls, org.lgna.story.font.Attribute[].class );
		return org.alice.ide.ast.AstUtilities.createInstanceCreation( constructor, 
				this.createExpression( font.getFamily() ), 
				this.createExpression( font.getWeight() ), 
				this.createExpression( font.getPosture() ) 
		);
	}
	
	private org.lgna.project.ast.Expression createColorExpression( org.lgna.story.Color color ) {
		org.lgna.project.ast.Expression rv = null;
		Class< ? > cls = org.lgna.story.Color.class;
		for( java.lang.reflect.Field fld : edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getPublicStaticFinalFields( cls, cls ) ) {
			if( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( fld, null ).equals( color ) ) {
				rv = this.createPublicStaticFieldAccess( fld );
				break;
			}
		}
		if( rv != null ) {
			//pass
		} else {
			org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance( cls, Number.class, Number.class, Number.class );
			rv = org.alice.ide.ast.AstUtilities.createInstanceCreation( constructor, this.createDoubleExpression( color.getRed() ), this.createDoubleExpression( color.getGreen() ), this.createDoubleExpression( color.getBlue() ) );
		}
		return rv;
	}
	private org.lgna.project.ast.Expression createImageSourceExpression( org.lgna.story.ImageSource imageSource ) {
		if( imageSource != null ) {
			org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance( org.lgna.story.ImageSource.class, org.alice.virtualmachine.resources.ImageResource.class );
			org.lgna.project.ast.Expression arg0Expression;
			org.alice.virtualmachine.resources.ImageResource imageResource = imageSource.getImageResource();
			if( imageResource != null ) {
				arg0Expression = new org.lgna.project.ast.ResourceExpression( org.alice.virtualmachine.resources.ImageResource.class, imageResource );
			} else {
				arg0Expression = new org.lgna.project.ast.NullLiteral();
			}
			return org.alice.ide.ast.AstUtilities.createInstanceCreation( constructor, arg0Expression );
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}
	
	private org.lgna.project.ast.Expression createImagePaintExpression( org.lgna.story.ImagePaint imagePaint ) {
		if( imagePaint != null ) {
			if( imagePaint instanceof Enum ) {
				Enum e = (Enum)imagePaint;
				java.lang.reflect.Field fld = edu.cmu.cs.dennisc.java.lang.EnumUtilities.getFld( (Enum)imagePaint );
				return createPublicStaticFieldAccess( fld );
			} else {
				//todo?
				//return new org.lgna.project.ast.NullLiteral();
				return this.createExpression( org.lgna.story.Color.RED );
			}
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}
	private org.lgna.project.ast.Expression createPaintExpression( org.lgna.story.Paint paint ) {
		if( paint != null ) {
			if( paint instanceof org.lgna.story.Color ) {
				org.lgna.story.Color color = (org.lgna.story.Color)paint;
				return createColorExpression( color );
			} else if( paint instanceof org.lgna.story.ImagePaint ) {
				org.lgna.story.ImagePaint imagePaint = (org.lgna.story.ImagePaint)paint;
				return this.createImagePaintExpression( imagePaint );
			} else if( paint instanceof org.lgna.story.ImageSource ) {
				org.lgna.story.ImageSource imageSource = (org.lgna.story.ImageSource)paint;
				return this.createImageSourceExpression( imageSource );
			} else {
				return null;
			}
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}	

	@Override
	protected org.lgna.project.ast.Expression createCustomExpression( Object value ) {
		if( value instanceof org.lgna.story.Position ) {
			return this.createPositionExpression( (org.lgna.story.Position)value );
		} else if( value instanceof org.lgna.story.Orientation ) {
			return this.createOrientationExpression( (org.lgna.story.Orientation)value );
		} else if( value instanceof org.lgna.story.Scale ) {
			return this.createScaleExpression( (org.lgna.story.Scale)value );
		} else if( value instanceof org.lgna.story.Paint ) {
			return this.createPaintExpression( (org.lgna.story.Paint)value );
		} else if( value instanceof org.lgna.story.Font ) {
			return this.createFontExpression( (org.lgna.story.Font)value );
		} else {
			throw new RuntimeException( value.toString() );
		}

	}
}
