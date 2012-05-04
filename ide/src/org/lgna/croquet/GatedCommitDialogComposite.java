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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class GatedCommitDialogComposite< MC extends Composite< ? >, CC extends GatedCommitDialogComposite.ControlsComposite > extends DialogComposite< org.lgna.croquet.components.BorderPanel > {
	private static final org.lgna.croquet.history.Step.Key< Boolean > IS_COMPLETED_KEY = org.lgna.croquet.history.Step.Key.createInstance( "GatedCommitDialogComposite.IS_COMPLETED_KEY" );

	private static final String NULL_EXPLANATION = "good to go";
	private static final String NULL_STEP_EXPLANATION = "null step";

	protected static abstract class ControlsComposite extends Composite< org.lgna.croquet.components.GridBagPanel > {
		public static final class InternalCompleteOperationResolver extends IndirectResolver< InternalCompleteOperation, ControlsComposite > {
			private InternalCompleteOperationResolver( ControlsComposite indirect ) {
				super( indirect );
			}
			public InternalCompleteOperationResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
				super( binaryDecoder );
			}
			@Override
			protected InternalCompleteOperation getDirect( ControlsComposite indirect ) {
				return indirect.completeOperation;
			}
		}
		public static final class InternalCancelOperationResolver extends IndirectResolver< InternalCancelOperation, ControlsComposite > {
			private InternalCancelOperationResolver( ControlsComposite indirect ) {
				super( indirect );
			}
			public InternalCancelOperationResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
				super( binaryDecoder );
			}
			@Override
			protected InternalCancelOperation getDirect( ControlsComposite indirect ) {
				return indirect.cancelOperation;
			}
		}
		protected static abstract class InternalDialogOperation extends ActionOperation {
			private final ControlsComposite controlsComposite;
			public InternalDialogOperation( java.util.UUID id, ControlsComposite controlsComposite ) {
				super( DIALOG_IMPLEMENTATION_GROUP, id );
				this.controlsComposite = controlsComposite;
			}
			public ControlsComposite getControlsComposite() {
				return this.controlsComposite;
			}
			@Override
			protected final void localize() {
				//note: do not invoke super
				//super.localize();
			}
		}
		
		private static abstract class InternalFinishOperation extends InternalDialogOperation {
			private final boolean isCompletion;
			public InternalFinishOperation( java.util.UUID id, ControlsComposite controlsComposite, boolean isCompletion ) {
				super( id, controlsComposite );
				this.isCompletion = isCompletion;
			}
			@Override
			protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
				org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
				org.lgna.croquet.history.CompletionStep<?> dialogStep = step.getFirstAncestorStepOfEquivalentModel( this.getControlsComposite().getGatedCommitDialogComposite().getOperation(), org.lgna.croquet.history.CompletionStep.class );
				org.lgna.croquet.components.Dialog dialog = dialogStep.getEphemeralDataFor( org.lgna.croquet.dialog.DialogUtilities.DIALOG_KEY );
				dialogStep.putEphemeralDataFor( IS_COMPLETED_KEY, this.isCompletion );
				dialog.setVisible( false );
				step.finish();
			}
			
		}
		
		private static final class InternalCompleteOperation extends InternalFinishOperation {
			private InternalCompleteOperation( ControlsComposite controlsComposite ) {
				super( java.util.UUID.fromString( "8618f47b-8a2b-45e1-ad03-0ff76e2b7e35" ), controlsComposite, true );
			}
			@Override
			protected InternalCompleteOperationResolver createResolver() {
				return new InternalCompleteOperationResolver( this.getControlsComposite() );
			}
		}
		private static final class InternalCancelOperation extends InternalFinishOperation {
			private InternalCancelOperation( ControlsComposite controlsComposite ) {
				super( java.util.UUID.fromString( "c467630e-39ee-49c9-ad07-d20c7a29db68" ), controlsComposite, false );
			}
			@Override
			protected InternalCancelOperationResolver createResolver() {
				return new InternalCancelOperationResolver( this.getControlsComposite() );
			}
		}

		private static class ExplanationLabel extends org.lgna.croquet.components.JComponent< javax.swing.JLabel > {
			@Override
			protected javax.swing.JLabel createAwtComponent() {
				javax.swing.JLabel rv = new javax.swing.JLabel( NULL_EXPLANATION ) {
					@Override
					protected void paintComponent( java.awt.Graphics g ) {
						if( this.getText() == NULL_EXPLANATION ) {
							//pass
						} else {
							super.paintComponent( g );
						}
					}
				};

				float f0 = 0.0f;
				float fA = 0.333f;
				float fB = 0.667f;
				float f1 = 1.0f;

				final int SCALE = 20;
				final int OFFSET = 0;

				f0 *= SCALE;
				fA *= SCALE;
				fB *= SCALE;
				f1 *= SCALE;

				final java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
				path.moveTo( fA, f0 );
				path.lineTo( fB, f0 );
				path.lineTo( f1, fA );
				path.lineTo( f1, fB );
				path.lineTo( fB, f1 );
				path.lineTo( fA, f1 );
				path.lineTo( f0, fB );
				path.lineTo( f0, fA );
				path.closePath();
				rv.setIcon( new javax.swing.Icon() {
					public int getIconWidth() {
						return SCALE + OFFSET + OFFSET;
					}
					public int getIconHeight() {
						return SCALE + OFFSET + OFFSET;
					}
					public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
						java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
						java.awt.geom.AffineTransform m = g2.getTransform();
						java.awt.Font font = g2.getFont();
						java.awt.Paint paint = g2.getPaint();
						try {
							g2.translate( OFFSET + x, OFFSET + y );
							g2.fill( path );
							g2.setPaint( java.awt.Color.WHITE );
							g2.draw( path );
							g2.setPaint( java.awt.Color.WHITE );
							g2.setFont( new java.awt.Font( null, java.awt.Font.BOLD, 12 ) );
							g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
							final int FUDGE_X = 1;
							edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g2, "X", FUDGE_X, 0, SCALE, SCALE );
						} finally {
							g2.setTransform( m );
							g2.setPaint( paint );
							g2.setFont( font );
						}
					}
				} );
				return rv;
			}		
		}

		private final GatedCommitDialogComposite gatedCommitDialogComposite;
		private final ExplanationLabel explanationLabel = new ExplanationLabel();
		private final InternalCompleteOperation completeOperation = new InternalCompleteOperation( this );
		private final InternalCancelOperation cancelOperation = new InternalCancelOperation( this );
		private final org.lgna.croquet.components.Button completeButton;
		public ControlsComposite( java.util.UUID id, GatedCommitDialogComposite gatedCommitDialogComposite ) {
			super( id );
			this.gatedCommitDialogComposite = gatedCommitDialogComposite;
			this.completeButton = this.getCompleteOperation().createButton();
		}
		public GatedCommitDialogComposite getGatedCommitDialogComposite() {
			return this.gatedCommitDialogComposite;
		}
		public org.lgna.croquet.components.Button getCompleteButton() {
			return this.completeButton;
		}
		protected abstract void addComponentsToControlLine( org.lgna.croquet.components.LineAxisPanel controlLine, org.lgna.croquet.components.Button leadingOkCancelButton, org.lgna.croquet.components.Button trailingOkCancelButton );
		@Override
		protected org.lgna.croquet.components.GridBagPanel createView() {
			org.lgna.croquet.components.Button okButton = this.getCompleteButton();
			org.lgna.croquet.components.Button cancelButton = this.getCancelOperation().createButton();
			
			org.lgna.croquet.components.Button leadingOkCancelButton;
			org.lgna.croquet.components.Button trailingOkCancelButton;
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
				leadingOkCancelButton = okButton;
				trailingOkCancelButton = cancelButton;
			} else {
				leadingOkCancelButton = cancelButton;
				trailingOkCancelButton = okButton;
			}
			org.lgna.croquet.components.LineAxisPanel controlLine = new org.lgna.croquet.components.LineAxisPanel();
			this.addComponentsToControlLine( controlLine, leadingOkCancelButton, trailingOkCancelButton );
			controlLine.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,4,4,4 ) );
			
			org.lgna.croquet.components.GridBagPanel rv = new org.lgna.croquet.components.GridBagPanel( this );
			java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
			gbc.anchor = java.awt.GridBagConstraints.NORTH;
			gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
			gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			rv.addComponent( this.explanationLabel, gbc );
			rv.addComponent( new org.lgna.croquet.components.HorizontalSeparator(), gbc );
			rv.addComponent( controlLine, gbc );
			controlLine.setBackgroundColor( null );
			rv.setBackgroundColor( null );
			return rv;
		}
		public Operation getCompleteOperation() {
			return this.completeOperation;
		}
		public Operation getCancelOperation() {
			return this.cancelOperation;
		}
	}
	private final org.lgna.croquet.history.event.Listener listener = new org.lgna.croquet.history.event.Listener() {
		public void changing( org.lgna.croquet.history.event.Event<?> e ) {
		}
		public void changed( org.lgna.croquet.history.event.Event<?> e ) {
			GatedCommitDialogComposite.this.handleFiredEvent( e );
		}
	};

	private final MC mainComposite;
	public GatedCommitDialogComposite( java.util.UUID id, Group operationGroup, MC mainComposite ) {
		super( id, operationGroup );
		this.mainComposite = mainComposite;
	}
	public MC getMainComposite() {
		return this.mainComposite;
	}
	@Override
	protected void localize() {
		super.localize();
		this.getControlsComposite().completeOperation.setName( this.findLocalizedText( "commit", GatedCommitDialogComposite.class ) );
		this.getControlsComposite().cancelOperation.setName( this.findLocalizedText( "cancel", GatedCommitDialogComposite.class ) );
	}
	protected abstract CC getControlsComposite();
	@Override
	protected org.lgna.croquet.components.BorderPanel createView() {
		org.lgna.croquet.components.BorderPanel rv = new org.lgna.croquet.components.BorderPanel();
		rv.addComponent( this.mainComposite.getView(), org.lgna.croquet.components.BorderPanel.Constraint.CENTER );
		rv.addComponent( this.getControlsComposite().getView(), org.lgna.croquet.components.BorderPanel.Constraint.PAGE_END );
		rv.setBackgroundColor( this.mainComposite.getView().getBackgroundColor() );
		return rv;
	}

	protected abstract String getExplanation( org.lgna.croquet.history.CompletionStep<?> step );
	protected void updateExplanation( org.lgna.croquet.history.CompletionStep<?> step ) {
		String explanation;
		if( step != null ) {
			explanation = this.getExplanation( step );
			if( explanation != null ) {
				//pass
			} else {
				explanation = NULL_EXPLANATION;
			}
		} else {
			explanation = NULL_STEP_EXPLANATION;
		}
		this.getControlsComposite().explanationLabel.getAwtComponent().setText( explanation );
		boolean isEnabled = explanation == NULL_EXPLANATION || explanation == NULL_STEP_EXPLANATION;
		this.getControlsComposite().getCompleteOperation().setEnabled( isEnabled );
	}

	public void handleFiredEvent( org.lgna.croquet.history.event.Event<?> event ) {
		org.lgna.croquet.history.CompletionStep<?> s = null;
		if( event != null ) {
			org.lgna.croquet.history.Node< ? > node = event.getNode();
			if( node != null ) {
				s = node.getFirstStepOfModelAssignableTo( GatedCommitDialogOperation.class, org.lgna.croquet.history.CompletionStep.class );
			}
		}
		this.updateExplanation( s );
	}
	@Override
	protected void handlePreShowDialog( org.lgna.croquet.history.Node<?> node ) {
		//todo
		org.lgna.croquet.history.CompletionStep<?> step = (org.lgna.croquet.history.CompletionStep<?>)node;
		org.lgna.croquet.components.Dialog dialog = step.getEphemeralDataFor( org.lgna.croquet.dialog.DialogUtilities.DIALOG_KEY );
		assert dialog != null : this + " " + node;
		ControlsComposite controlsComposite = this.getControlsComposite();
		assert controlsComposite != null : this;
		dialog.setDefaultButton( controlsComposite.getCompleteButton() );
		node.addListener( this.listener );
		this.updateExplanation( step );
		super.handlePreShowDialog( node );
	}
	@Override
	protected void handlePostHideDialog( org.lgna.croquet.history.Node<?> node ) {
		node.removeListener( this.listener );
		super.handlePostHideDialog( node );
	}
}
