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
package org.alice.ide.uricontent;

/**
 * @author Dennis Cosgrove
 */
public class FileProjectLoader extends UriProjectLoader {
	private final java.io.File file;

	public FileProjectLoader( java.io.File file ) {
		this.file = file;
	}

	@Override
	protected boolean isCacheAndCopyStyle() {
		return false;
	}

	@Override
	public java.net.URI getUri() {
		return this.file.toURI();
	}

	@Override
	protected org.lgna.project.Project load() {
		if( file.exists() ) {
			String lcFilename = file.getName().toLowerCase();
			if( lcFilename.endsWith( ".a2w" ) ) {
				new edu.cmu.cs.dennisc.javax.swing.option.MessageDialog.Builder( "Alice3 does not load Alice2 worlds" )
						.title( "Cannot read file" )
						.messageType( edu.cmu.cs.dennisc.javax.swing.option.MessageType.ERROR )
						.buildAndShow();
			} else if( lcFilename.endsWith( org.lgna.project.io.IoUtilities.TYPE_EXTENSION.toLowerCase() ) ) {
				new edu.cmu.cs.dennisc.javax.swing.option.MessageDialog.Builder( file.getAbsolutePath() + " appears to be a class file and not a project file.\n\nLook for files with an " + org.lgna.project.io.IoUtilities.PROJECT_EXTENSION + " extension." )
						.title( "Incorrect File Type" )
						.messageType( edu.cmu.cs.dennisc.javax.swing.option.MessageType.ERROR )
						.buildAndShow();
			} else {
				boolean isWorthyOfException = lcFilename.endsWith( org.lgna.project.io.IoUtilities.PROJECT_EXTENSION.toLowerCase() );
				java.util.zip.ZipFile zipFile;
				try {
					zipFile = new java.util.zip.ZipFile( file );
				} catch( java.io.IOException ioe ) {
					if( isWorthyOfException ) {
						throw new RuntimeException( file.getAbsolutePath(), ioe );
					} else {
						org.alice.ide.ProjectApplication.getActiveInstance().showUnableToOpenProjectMessageDialog( file, false );
						zipFile = null;
					}
				}
				if( zipFile != null ) {
					try {
						return org.lgna.project.io.IoUtilities.readProject( zipFile );
					} catch( org.lgna.project.VersionNotSupportedException vnse ) {
						org.alice.ide.ProjectApplication.getActiveInstance().handleVersionNotSupported( file, vnse );
					} catch( java.io.IOException ioe ) {
						if( isWorthyOfException ) {
							throw new RuntimeException( file.getAbsolutePath(), ioe );
						} else {
							org.alice.ide.ProjectApplication.getActiveInstance().showUnableToOpenProjectMessageDialog( file, true );
						}
					}
				} else {
					//actionContext.cancel();
				}
			}
		} else {
			org.alice.ide.ProjectApplication.getActiveInstance().showUnableToOpenFileDialog( file, "It does not exist." );
		}
		return null;
	}

	public static void main( String[] args ) throws Exception {
		java.io.File file = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "Alice3/MyProjects/a.a3p" );
		FileProjectLoader uriProjectPair = new FileProjectLoader( file );

		UriContentLoader.MutationPlan mutationPlan = MutationPlan.WILL_MUTATE;
		for( int i = 0; i < 32; i++ ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( i );
			final boolean IS_OBSERVER_DESIRED = true;
			if( IS_OBSERVER_DESIRED ) {
				uriProjectPair.getContentOnEventDispatchThread( mutationPlan, new GetContentObserver<org.lgna.project.Project>() {
					public void workStarted() {
						edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "workStarted" );
					}

					public void workEnded() {
						edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "workEnded" );
					}

					public void completed( org.lgna.project.Project project ) {
						edu.cmu.cs.dennisc.java.util.logging.Logger.outln( project );
					}

					public void failed( Throwable t ) {
						t.printStackTrace();
					}
				} );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( uriProjectPair.getContentWaitingIfNecessary( mutationPlan ) );
			}
			Thread.sleep( 100 );
		}
	}
}
