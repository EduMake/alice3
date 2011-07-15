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
package org.alice.stageide.choosers;

class TimeSlider extends javax.swing.JSlider {
	private javax.swing.event.ChangeListener changeAdapter = new javax.swing.event.ChangeListener() {
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			TimeSlider.this.updateTime();
		}
	};
	private double duration;
	private double time;
	public TimeSlider() {
		this.setMaximum( 1000 );
		this.setDuration( Double.NaN );
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		this.addChangeListener( this.changeAdapter );
	}
	@Override
	public void removeNotify() {
		this.removeChangeListener( this.changeAdapter );
		super.removeNotify();
	}
	private void updateValue() {
		if( Double.isNaN( this.time ) || Double.isNaN( this.duration ) ) {
			this.setValue( this.getMaximum() );
		} else {
			this.setValue( (int)((this.time/this.duration)*this.getMaximum()) );
		}
	}
	private void updateTime() {
		if( Double.isNaN( this.duration ) ) {
			//pass
		} else {
			this.time = this.getValue()*this.duration/this.getMaximum();
		}
	}
	
	public double getDuration() {
		return this.duration;
	}
	public void setDuration( double duration ) {
		this.duration = duration;
		this.setEnabled( Double.isNaN( this.duration )==false );
		this.updateValue();
	}
	private static java.text.NumberFormat format = new java.text.DecimalFormat( "0.00" );
	public double getTime() {
		if( Double.isNaN( this.time ) ) {
			return this.time;
		} else {
			return Double.parseDouble( format.format( this.time ) );
		}
	}
	public void setTime( double time ) {
		this.time = time;
		this.updateValue();
	}
}

/**
 * @author Dennis Cosgrove
 */
public class AudioSourceChooser extends org.alice.ide.choosers.AbstractRowsPaneChooser< edu.cmu.cs.dennisc.alice.ast.InstanceCreation > {
	class BogusNode extends edu.cmu.cs.dennisc.alice.ast.AbstractNode {
		private edu.cmu.cs.dennisc.alice.ast.ExpressionProperty bogusProperty = new edu.cmu.cs.dennisc.alice.ast.ExpressionProperty( this ) {
			@Override
			public edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getExpressionType() {
				return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.virtualmachine.resources.AudioResource.class );
			}
		};
		@Override
		public void firePropertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			super.firePropertyChanged( e );
			AudioSourceChooser.this.startTimeSlider.setTime( 0.0 );
			AudioSourceChooser.this.stopTimeSlider.setTime( Double.NaN );
			
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: update OK button?" );
//			edu.cmu.cs.dennisc.croquet.InputPanel< ? > inputPanel = AudioSourceChooser.this.getInputPanel();
//			if( inputPanel != null ) {
//				inputPanel.updateOKButton();
//			}
		}
	}
	
	private BogusNode bogusNode = new BogusNode();
	private org.lgna.croquet.components.Component< ? > dropDown;
	
	private org.alice.stageide.controls.VolumeLevelControl volumeLevelControl = new org.alice.stageide.controls.VolumeLevelControl();
	private TimeSlider startTimeSlider = new TimeSlider();
	private TimeSlider stopTimeSlider = new TimeSlider();
	
	class TestOperation extends org.alice.ide.operations.InconsequentialActionOperation {
		public TestOperation() {
			super( java.util.UUID.fromString( "6d1c60d4-bf5d-43ff-9b65-797eebf21a90" ) );
			this.setName( "test" );
		}
		@Override
		protected void performInternal( org.lgna.croquet.history.ActionOperationStep step ) {
			org.lookingglassandalice.storytelling.AudioSource audioSource = getAudioSource();
			edu.cmu.cs.dennisc.media.MediaFactory mediaFactory = edu.cmu.cs.dennisc.media.jmf.MediaFactory.getSingleton();
			edu.cmu.cs.dennisc.media.Player player = mediaFactory.createPlayer( audioSource.getAudioResource(), audioSource.getVolume(), audioSource.getStartTime(), audioSource.getStopTime() );
			org.lgna.croquet.triggers.Trigger trigger = step.getTrigger();
			player.test( trigger.getViewController().getAwtComponent() );
		}
	};
	private TestOperation testOperation = new TestOperation();

	private static edu.cmu.cs.dennisc.alice.ast.Expression getArgumentExpressionAt( edu.cmu.cs.dennisc.alice.ast.InstanceCreation instanceCreation, int index ) {
		assert instanceCreation.arguments.size() >= index;
		edu.cmu.cs.dennisc.alice.ast.Argument arg = instanceCreation.arguments.get( index );
		assert arg != null;
		return arg.expression.getValue();
	}
	public AudioSourceChooser() {
		edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression = null;
		org.alice.virtualmachine.resources.AudioResource audioResource = null;
		double volumeLevel = 1.0;
		double startTime = 0.0;
		double stopTime = Double.NaN;

		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = this.getPreviousExpression();
		if( previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.InstanceCreation ) {
			edu.cmu.cs.dennisc.alice.ast.InstanceCreation instanceCreation = (edu.cmu.cs.dennisc.alice.ast.InstanceCreation)previousExpression;
			int n = instanceCreation.arguments.size();
			if( n > 0 ) {
				edu.cmu.cs.dennisc.alice.ast.Expression expression0 = getArgumentExpressionAt( instanceCreation, 0 );
				if( expression0 instanceof edu.cmu.cs.dennisc.alice.ast.ResourceExpression ) {
					resourceExpression = (edu.cmu.cs.dennisc.alice.ast.ResourceExpression)expression0;
					org.alice.virtualmachine.Resource resource = resourceExpression.resource.getValue();
					if( resource instanceof org.alice.virtualmachine.resources.AudioResource ) {
						audioResource = (org.alice.virtualmachine.resources.AudioResource)resource;
					}
					if( n > 1 ) {
						if( n > 2 ) {
							if( n > 3 ) {
								edu.cmu.cs.dennisc.alice.ast.Expression expression3 = getArgumentExpressionAt( instanceCreation, 3 );
								if( expression3 instanceof edu.cmu.cs.dennisc.alice.ast.DoubleLiteral ) {
									edu.cmu.cs.dennisc.alice.ast.DoubleLiteral stopTimeLiteral = (edu.cmu.cs.dennisc.alice.ast.DoubleLiteral)expression3;
									stopTime = stopTimeLiteral.value.getValue();
								}
							}
							edu.cmu.cs.dennisc.alice.ast.Expression expression2 = getArgumentExpressionAt( instanceCreation, 2 );
							if( expression2 instanceof edu.cmu.cs.dennisc.alice.ast.DoubleLiteral ) {
								edu.cmu.cs.dennisc.alice.ast.DoubleLiteral startTimeLiteral = (edu.cmu.cs.dennisc.alice.ast.DoubleLiteral)expression2;
								startTime = startTimeLiteral.value.getValue();
							}
						}
						edu.cmu.cs.dennisc.alice.ast.Expression expression1 = getArgumentExpressionAt( instanceCreation, 1 );
						if( expression1 instanceof edu.cmu.cs.dennisc.alice.ast.DoubleLiteral ) {
							edu.cmu.cs.dennisc.alice.ast.DoubleLiteral volumeLevelLiteral = (edu.cmu.cs.dennisc.alice.ast.DoubleLiteral)expression1;
							volumeLevel = volumeLevelLiteral.value.getValue();
						}
					}
				}
			}
		}
		this.bogusNode.bogusProperty.setValue( resourceExpression );
		this.volumeLevelControl.setVolumeLevel( volumeLevel );
		this.startTimeSlider.setTime( startTime );
		this.stopTimeSlider.setTime( stopTime );

		org.alice.ide.common.Factory factory = org.alice.ide.IDE.getActiveInstance().getCodeFactory();
		this.dropDown = factory.createExpressionPropertyPane( bogusNode.bogusProperty, null );
		
		if( audioResource != null ) {
			double duration = audioResource.getDuration();
			if( Double.isNaN( duration ) ) {
				//pass
			} else {
				this.startTimeSlider.setDuration( duration );
				this.stopTimeSlider.setDuration( duration );
			}
		}

//		this.volumeLevelControl.addChangeListener( new javax.swing.event.ChangeListener() {
//			public void stateChanged( javax.swing.event.ChangeEvent e ) {
////				AudioSourceChooser.this.getInputPanel().updateOKButton();
//			}
//		} );
		this.startTimeSlider.addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				if( startTimeSlider.getValue() > stopTimeSlider.getValue() ) {
					stopTimeSlider.setValue( startTimeSlider.getValue() );
				}
			}
		} );
		this.stopTimeSlider.addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				if( startTimeSlider.getValue() > stopTimeSlider.getValue() ) {
					startTimeSlider.setValue( stopTimeSlider.getValue() );
				}
			}
		} );
	}
	@Override
	public String getExplanationIfOkButtonShouldBeDisabled() {
		edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression = (edu.cmu.cs.dennisc.alice.ast.ResourceExpression)bogusNode.bogusProperty.getValue();
		if( resourceExpression != null ) {
			if( resourceExpression.resource.getValue() instanceof org.alice.virtualmachine.resources.AudioResource ) {
				return null;
			} else {
				return "resource is not audio";
			}
		} else {
			return "resource is not set";
		}
	}
	@Override
	public String[] getLabelTexts() {
		return new String[] { 
				"resource:", 
				"volume:", 
				"", 
				"start marker:", 
				"stop marker:" };
	}
	@Override
	public org.lgna.croquet.components.Component< ? >[] getComponents() {
		return new org.lgna.croquet.components.Component< ? >[] { 
				this.dropDown, 
				new org.lgna.croquet.components.LineAxisPanel( 
						new org.lgna.croquet.components.SwingAdapter( this.volumeLevelControl ), 
						org.lgna.croquet.components.BoxUtilities.createHorizontalGlue() 
				), 
				org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 16 ), 
				new org.lgna.croquet.components.SwingAdapter( this.startTimeSlider ), 
				new org.lgna.croquet.components.SwingAdapter( this.stopTimeSlider ) };
	}
	@Override
	public java.util.List< org.lgna.croquet.components.Component< ? >[] > updateRows( java.util.List< org.lgna.croquet.components.Component< ? >[] > rv ) {
		super.updateRows( rv );
		rv.add( org.lgna.croquet.components.SpringUtilities.createRow( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 32 ), null ) );
		rv.add( org.lgna.croquet.components.SpringUtilities.createRow( testOperation.createButton(), null ) );
		return rv;
	}

	@Override
	public edu.cmu.cs.dennisc.alice.ast.InstanceCreation getValue() {
		org.lookingglassandalice.storytelling.AudioSource audioSource = this.getAudioSource();
		if( audioSource != null ) {
			org.alice.virtualmachine.resources.AudioResource audioResource = audioSource.getAudioResource();

			org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
			edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
			if( project != null ) {
				project.addResource( audioResource );
			}

			edu.cmu.cs.dennisc.alice.ast.Expression arg0Expression;
			if( audioResource != null ) {
				arg0Expression = new edu.cmu.cs.dennisc.alice.ast.ResourceExpression( org.alice.virtualmachine.resources.AudioResource.class, audioResource );				
			} else {
				arg0Expression = new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
			}

			double volume = audioSource.getVolume();
			double startTime = audioSource.getStartTime();
			double stopTime = audioSource.getStopTime();

			// apologies for the negative logic
			boolean isNotDefaultVolume = org.lookingglassandalice.storytelling.AudioSource.isWithinReasonableEpsilonOfDefaultVolume( volume ) == false;
			boolean isNotDefaultStartTime = org.lookingglassandalice.storytelling.AudioSource.isWithinReasonableEpsilonOfDefaultStartTime( startTime ) == false;
			boolean isNotDefaultStopTime = org.lookingglassandalice.storytelling.AudioSource.isDefaultStopTime_aka_NaN( stopTime ) == false;

			if( isNotDefaultVolume || isNotDefaultStartTime || isNotDefaultStopTime ) {
				edu.cmu.cs.dennisc.alice.ast.DoubleLiteral volumeLiteral = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( volume );
				if( isNotDefaultStartTime || isNotDefaultStopTime ) {
					edu.cmu.cs.dennisc.alice.ast.DoubleLiteral startTimeLiteral = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( startTime );
					if( isNotDefaultStopTime ) {
						edu.cmu.cs.dennisc.alice.ast.DoubleLiteral stopTimeLiteral = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( stopTime );

						edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( 
								org.lookingglassandalice.storytelling.AudioSource.class, 
								org.alice.virtualmachine.resources.AudioResource.class,
								Number.class, 
								Number.class, 
								Number.class );
						return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, arg0Expression, volumeLiteral, startTimeLiteral, stopTimeLiteral );
					} else {
						edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( 
								org.lookingglassandalice.storytelling.AudioSource.class, 
								org.alice.virtualmachine.resources.AudioResource.class,
								Number.class, 
								Number.class );
						return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, arg0Expression, volumeLiteral, startTimeLiteral );
					}
				} else {
					edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( 
							org.lookingglassandalice.storytelling.AudioSource.class, 
							org.alice.virtualmachine.resources.AudioResource.class,
							Number.class );
					return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, arg0Expression, volumeLiteral );
				}
			} else {
				edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( 
						org.lookingglassandalice.storytelling.AudioSource.class, 
						org.alice.virtualmachine.resources.AudioResource.class );
				return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, arg0Expression );
			}
		} else {
			return null;
		}
	}
	
	private org.lookingglassandalice.storytelling.AudioSource getAudioSource() {
		org.alice.virtualmachine.resources.AudioResource audioResource;
		double volume = this.volumeLevelControl.getVolumeLevel();
		double start;
		double stop;
		double duration;
		edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression = (edu.cmu.cs.dennisc.alice.ast.ResourceExpression)bogusNode.bogusProperty.getValue();
		if( resourceExpression != null ) {
			org.alice.virtualmachine.Resource resource = resourceExpression.resource.getValue();
			if( resource instanceof org.alice.virtualmachine.resources.AudioResource ) {
				audioResource = (org.alice.virtualmachine.resources.AudioResource)resource;
				duration = audioResource.getDuration();
			} else {
				audioResource = null;
				duration = Double.NaN;
			}
		} else {
			audioResource = null;
			duration = Double.NaN;
		}
		if( Double.isNaN( duration ) ) {
			//todo
			start = 0.0;
			stop = Double.NaN;
		} else {
			if( this.startTimeSlider.getValue() == this.startTimeSlider.getMinimum() ) {
				start = 0.0;
			} else {
				start = this.startTimeSlider.getTime();
			}
			if( this.stopTimeSlider.getValue() == this.stopTimeSlider.getMaximum() ) {
				stop = Double.NaN;
			} else {
				stop = this.stopTimeSlider.getTime();
			}
		}
		return new org.lookingglassandalice.storytelling.AudioSource( audioResource, volume, start, stop );
	}
}
