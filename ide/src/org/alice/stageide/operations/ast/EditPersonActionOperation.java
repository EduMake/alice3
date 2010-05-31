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
package org.alice.stageide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public class EditPersonActionOperation extends edu.cmu.cs.dennisc.croquet.InputDialogOperation {
	private edu.cmu.cs.dennisc.alice.ast.AbstractField field;
	public EditPersonActionOperation( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "ad113b0e-acb2-4e43-8196-eba6a4961dc8" ) );
		this.setName( "Edit..." );
		this.field = field;
	}
	
	private org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor getMoveAndTurnSceneEditor() {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( org.alice.ide.IDE.getSingleton().getSceneEditor(), org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor.class );
	}
	
	private org.alice.stageide.personeditor.PersonEditor personEditor;
	private org.alice.apis.stage.Gender prevGender;
	private org.alice.apis.stage.SkinTone prevSkinTone;
	private org.alice.apis.stage.EyeColor prevEyeColor;
	private org.alice.apis.stage.Outfit prevOutfit;
	private org.alice.apis.stage.Hair prevHair;
	private Double prevFitnessLevel;
	@Override
	protected edu.cmu.cs.dennisc.croquet.Component<?> prologue(edu.cmu.cs.dennisc.croquet.ModelContext context) {
		final org.alice.apis.stage.Person person = this.getMoveAndTurnSceneEditor().getInstanceInJavaForField( this.field, org.alice.apis.stage.Person.class );
		if( person != null ) {
			this.prevGender = person.getGender(); 
			this.prevSkinTone = person.getSkinTone(); 
			this.prevEyeColor = person.getEyeColor(); 
			this.prevOutfit = person.getOutfit(); 
			this.prevHair = person.getHair(); 
			this.prevFitnessLevel = person.getFitnessLevel();
			this.personEditor = new org.alice.stageide.personeditor.PersonEditor( person );
		} else {
			this.personEditor = null;
		}
		return this.personEditor; 
	}
	@Override
	protected void epilogue(edu.cmu.cs.dennisc.croquet.ModelContext context, boolean isOk) {
		final org.alice.apis.stage.Person person = this.personEditor.getPerson();
		if( isOk ) {
			final org.alice.apis.stage.Gender nextGender = person.getGender();
			final org.alice.apis.stage.SkinTone nextSkinTone = person.getSkinTone();
			final org.alice.apis.stage.EyeColor nextEyeColor = person.getEyeColor();
			final org.alice.apis.stage.Outfit nextOutfit = person.getOutfit();
			final org.alice.apis.stage.Hair nextHair = person.getHair();
			final Double nextFitnessLevel = person.getFitnessLevel();
			context.commitAndInvokeDo( new org.alice.ide.ToDoEdit( context ) {
				@Override
				public void doOrRedo( boolean isDo ) {
					EditPersonActionOperation.set( person, nextGender, nextSkinTone, nextEyeColor, nextOutfit, nextHair, nextFitnessLevel );
				}
				@Override
				public void undo() {
					EditPersonActionOperation.set( person, prevGender, prevSkinTone, prevEyeColor, prevOutfit, prevHair, prevFitnessLevel );
				}
				@Override
				protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
					rv.append( "edit: " );
					edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, field, locale );
					return rv;
				}
			} );
		} else {
			EditPersonActionOperation.set( person, prevGender, prevSkinTone, prevEyeColor, prevOutfit, prevHair, prevFitnessLevel );
			context.cancel();
		}
	}
	private static void set( org.alice.apis.stage.Person person, org.alice.apis.stage.Gender gender, org.alice.apis.stage.SkinTone skinTone, org.alice.apis.stage.EyeColor eyeColor, org.alice.apis.stage.Outfit outfit, org.alice.apis.stage.Hair hair, Double fitnessLevel ) {
		person.setGender( gender );
		person.setSkinTone( skinTone );
		person.setEyeColor( eyeColor );
		person.setOutfit( outfit );
		person.setHair( hair );
		person.setFitnessLevel( fitnessLevel );
	}
}
