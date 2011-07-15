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
package org.alice.stageide.croquet.models.gallerybrowser;

import org.lgna.croquet.components.BorderPanel.Constraint;

/**
 * @author Dennis Cosgrove
 */
class CreateFieldFromBillboardPane extends org.alice.ide.declarationpanes.CreateLargelyPredeterminedFieldPane {
	private org.lookingglassandalice.storytelling.Billboard billboard;
	private org.alice.ide.croquet.ImageView imageView = new org.alice.ide.croquet.ImageView( 240 );
	public CreateFieldFromBillboardPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		super( declaringType, org.lookingglassandalice.storytelling.Billboard.class, null );
		this.imageView.setBorder( javax.swing.BorderFactory.createEmptyBorder(0,0,0,8) );
		this.addComponent( this.imageView, Constraint.LINE_END );
	}
	public org.lookingglassandalice.storytelling.Billboard getBillboard() {
		return this.billboard;
	}
	public void setBillboard( org.lookingglassandalice.storytelling.Billboard billboard ) {
		this.billboard = billboard;
		org.lookingglassandalice.storytelling.ImageSource frontImageSource = billboard.getFrontImageSource();
		if( frontImageSource != null ) {
			org.alice.virtualmachine.resources.ImageResource imageResource = frontImageSource.getImageResource();
			if( imageResource != null ) {
				java.awt.image.BufferedImage bufferedImage = edu.cmu.cs.dennisc.image.ImageFactory.getBufferedImage( imageResource );
				if( bufferedImage != null ) {
					this.imageView.setBufferedImage( bufferedImage );
				} else {
					//todo?
				}
			}
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class CreateBillboardOperation extends AbstractGalleryDeclareFieldOperation {
	private static class SingletonHolder {
		private static CreateBillboardOperation instance = new CreateBillboardOperation();
	}
	public static CreateBillboardOperation getInstance() {
		return SingletonHolder.instance;
	}
	private CreateBillboardOperation() {
		super( java.util.UUID.fromString( "6ec4f250-7bb7-4f73-830b-1e9b511b69d5" ) );
	}
	@Override
	protected org.alice.stageide.croquet.models.gallerybrowser.CreateFieldFromBillboardPane prologue( org.lgna.croquet.history.InputDialogOperationStep context ) {
		org.alice.ide.resource.prompter.ImageResourcePrompter imageResourcePrompter = org.alice.ide.resource.prompter.ImageResourcePrompter.getSingleton();
		CreateFieldFromBillboardPane rv = new CreateFieldFromBillboardPane( this.getOwnerType() );
		try {
			org.alice.virtualmachine.resources.ImageResource frontImageResource = imageResourcePrompter.promptUserForResource( this.getIDE().getFrame() );
			if( frontImageResource != null ) {
				edu.cmu.cs.dennisc.alice.Project project = this.getIDE().getProject();
				if( project != null ) {
					project.addResource( frontImageResource );
				}
				org.lookingglassandalice.storytelling.ImageSource frontImageSource = new org.lookingglassandalice.storytelling.ImageSource( frontImageResource );
				org.lookingglassandalice.storytelling.Billboard billboard = new org.lookingglassandalice.storytelling.Billboard();
				billboard.setFrontImageSource( frontImageSource );
				rv.setBillboard( billboard );
				//				String name = "billboard";
				//				edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type = this.getIDE().getTypeDeclaredInAliceFor(org.lookingglassandalice.storytelling.Billboard.class);
				//				edu.cmu.cs.dennisc.alice.ast.Expression initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation(type);
				//				edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice(name, type, initializer);
				//				field.finalVolatileOrNeither.setValue(edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL);
				//				field.access.setValue(edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE);
				//				return new edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object>( field, billboard );
			} else {
				return null;
			}
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
		return rv;
	}
	@Override
	protected edu.cmu.cs.dennisc.pattern.Tuple2< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, org.lookingglassandalice.storytelling.Billboard > createFieldAndInstance( org.lgna.croquet.history.InputDialogOperationStep context ) {
		CreateFieldFromBillboardPane createFieldFromBillboardPane = context.getMainPanel();
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = createFieldFromBillboardPane.getInputValue();
		if( field != null ) {
			//ide.getSceneEditor().handleFieldCreation( declaringType, field, person );
			return edu.cmu.cs.dennisc.pattern.Tuple2.createInstance( field, createFieldFromBillboardPane.getBillboard() );
		} else {
			return null;
		}
	}
}
