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
package org.alice.ide.operations.file;

import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.Container;
import org.lgna.croquet.components.Dialog;
import org.lgna.croquet.history.CompletionStep;

/**
 * @author Dennis Cosgrove
 */
public class ExportVideoUploadToYouTubeOperation extends org.alice.ide.video.RecordVideoOperation {
	private static class SingletonHolder {
		private static ExportVideoUploadToYouTubeOperation instance = new ExportVideoUploadToYouTubeOperation();
	}

	public static ExportVideoUploadToYouTubeOperation getInstance() {
		return SingletonHolder.instance;
	}

	private final org.lgna.croquet.State.ValueListener<Boolean> isRecordingListener = new org.lgna.croquet.State.ValueListener<Boolean>() {
		public void changing( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			ExportVideoUploadToYouTubeOperation.this.setRecording( nextValue );
		}
	};

	private ExportVideoUploadToYouTubeOperation() {
		super( java.util.UUID.fromString( "fd6ec0d7-add3-4061-a895-f085f45c0667" ) );
		this.setName( "Export Video / Upload To YouTube\u2122..." );
	}

	@Override
	public org.alice.media.ExportToYoutubePanel createVideoExportPanel() {
		return new org.alice.media.ExportToYoutubePanel();
	}

	@Override
	protected Component<?> createControlsPanel( CompletionStep<?> step, Dialog dialog ) {
		org.alice.ide.video.IsRecordingState.getInstance().setValue( false );
		org.alice.ide.video.IsRecordingState.getInstance().addValueListener( this.isRecordingListener );
		return super.createControlsPanel( step, dialog );
	}

	@Override
	protected void handleFinally( CompletionStep<?> step, Dialog dialog, Container<?> contentPane ) {
		org.alice.ide.video.IsRecordingState.getInstance().removeValueListener( this.isRecordingListener );
		super.handleFinally( step, dialog, contentPane );
	}

	@Override
	protected void handleImage( java.awt.image.BufferedImage image, int i ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( image );
	}
	//	protected void performInternal( org.lgna.croquet.history.CompletionStep<?> step ) {
	//		final org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
	//		org.lgna.project.Project project = ide.getUpToDateProject();
	//		if( project != null ) {
	//			final int frameRate = 24;
	//			//this.rtProgram = new RecordableRuntimeProgram( sceneType, vm );
	//			org.alice.media.VideoCapturePane videoCapturePane = new org.alice.media.VideoCapturePane(project, frameRate){
	//				@Override
	//				protected void onClose()
	//				{
	//					javax.swing.SwingUtilities.getRoot( this ).setVisible( false );
	//				}
	//			};
	//
	//			javax.swing.JDialog dialog = edu.cmu.cs.dennisc.javax.swing.JDialogUtilities.createPackedJDialog( videoCapturePane, org.lgna.croquet.Application.getActiveInstance().getFrame().getAwtComponent(), "Export Video", true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
	//			dialog.setVisible( true );
	//		}
	//	}
}
//
//
//extends org.alice.ide.operations.InconsequentialActionOperation {
//	public ExportVideoUploadToYouTubeOperation() {
//	}
//	@Override
//	protected void performInternal( org.lgna.croquet.history.ActionOperationStep step ) {
//		final org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
//		org.lgna.project.Project project = ide.getUpToDateProject();
//		if( project != null ) {
//			org.alice.media.VideoCapturePane videoCapturePane = new org.alice.media.VideoCapturePane( new org.alice.stageide.program.VideoEncodingProgramContext( 24.0 ) ) {
//				@Override
//				protected void onClose()
//				{
//					javax.swing.SwingUtilities.getRoot( this ).setVisible( false );
//				}
//			};
//
//			javax.swing.JDialog dialog = edu.cmu.cs.dennisc.javax.swing.JDialogUtilities.createPackedJDialog( videoCapturePane, org.lgna.croquet.Application.getActiveInstance().getFrame().getAwtComponent(), "Export Video", true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
//			dialog.setVisible( true );
//		}
//	}
//}
