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
package edu.cmu.cs.dennisc.renderer.gl.imp;

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import edu.cmu.cs.dennisc.renderer.gl.AdapterFactory;
import edu.cmu.cs.dennisc.renderer.gl.GlrRenderTarget;
import edu.cmu.cs.dennisc.renderer.gl.Graphics2D;
import edu.cmu.cs.dennisc.renderer.gl.RenderContext;
import edu.cmu.cs.dennisc.renderer.gl.imp.adapters.AbstractCameraAdapter;
import edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults;

/**
 * @author Dennis Cosgrove
 */
public class RenderTargetImp {
	public RenderTargetImp( edu.cmu.cs.dennisc.renderer.gl.GlrRenderTarget glrRT ) {
		this.glrRT = glrRT;
		this.reusableLookingGlassRenderEvent = new ReusableLookingGlassRenderEvent( this.getRenderTarget(), new Graphics2D( this.renderContext ) );
	}

	public edu.cmu.cs.dennisc.renderer.gl.GlrRenderTarget getRenderTarget() {
		return this.glrRT;
	}

	public edu.cmu.cs.dennisc.renderer.SynchronousPicker getSynchronousPicker() {
		return this.synchronousPicker;
	}

	public edu.cmu.cs.dennisc.renderer.SynchronousImageCapturer getSynchronousImageCapturer() {
		return this.synchronousImageCapturer;
	}

	public edu.cmu.cs.dennisc.renderer.AsynchronousPicker getAsynchronousPicker() {
		return this.asynchronousPicker;
	}

	public edu.cmu.cs.dennisc.renderer.AsynchronousImageCapturer getAsynchronousImageCapturer() {
		return this.asynchronousImageCapturer;
	}

	public void addRenderTargetListener( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener listener ) {
		this.renderTargetListeners.add( listener );
	}

	public void removeRenderTargetListener( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener listener ) {
		this.renderTargetListeners.remove( listener );
	}

	public java.util.List<edu.cmu.cs.dennisc.renderer.event.RenderTargetListener> getRenderTargetListeners() {
		return java.util.Collections.unmodifiableList( this.renderTargetListeners );
	}

	public void addSgCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		assert sgCamera != null : this;
		this.sgCameras.add( sgCamera );
		if( this.isListening() ) {
			//pass
		} else {
			javax.media.opengl.GLAutoDrawable glAutoDrawable = this.glrRT.getGLAutoDrawable();
			this.startListening( glAutoDrawable );
		}
	}

	public void removeSgCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		assert sgCamera != null;
		this.sgCameras.remove( sgCamera );
		if( this.isListening() ) {
			if( this.sgCameras.isEmpty() ) {
				this.stopListening( this.glrRT.getGLAutoDrawable() );
			}
		}
	}

	public void clearSgCameras() {
		if( this.sgCameras.size() > 0 ) {
			this.sgCameras.clear();
		}
		if( this.isListening() ) {
			this.stopListening( this.glrRT.getGLAutoDrawable() );
		}
	}

	public int getSgCameraCount() {
		return this.sgCameras.size();
	}

	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSgCameraAt( int index ) {
		return this.sgCameras.get( index );
	}

	public java.util.List<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> getSgCameras() {
		return java.util.Collections.unmodifiableList( this.sgCameras );
	}

	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getCameraAtPixel( int xPixel, int yPixel ) {
		java.util.ListIterator<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> iterator = this.sgCameras.listIterator( this.sgCameras.size() );
		while( iterator.hasPrevious() ) {
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = iterator.previous();
			synchronized( s_actualViewportBufferForReuse ) {
				this.glrRT.getActualViewport( s_actualViewportBufferForReuse, sgCamera );
				if( s_actualViewportBufferForReuse.contains( xPixel, yPixel ) ) {
					return sgCamera;
				}
			}
		}
		return null;
	}

	public void forgetAllCachedItems() {
		if( this.renderContext != null ) {
			this.renderContext.forgetAllCachedItems();
		}
	}

	public void clearUnusedTextures() {
		if( this.renderContext != null ) {
			this.renderContext.clearUnusedTextures();
		}
	}

	/*package-private*/void addDisplayTask( DisplayTask displayTask ) {
		synchronized( this.displayTasks ) {
			this.displayTasks.add( displayTask );
		}
	}

	/* package-private */void fireInitialized( edu.cmu.cs.dennisc.renderer.event.RenderTargetInitializeEvent e ) {
		for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener rtListener : this.renderTargetListeners ) {
			rtListener.initialized( e );
		}
	}

	/* package-private */void fireCleared( edu.cmu.cs.dennisc.renderer.event.RenderTargetRenderEvent e ) {
		for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener rtListener : this.renderTargetListeners ) {
			rtListener.cleared( e );
		}
	}

	/* package-private */void fireRendered( edu.cmu.cs.dennisc.renderer.event.RenderTargetRenderEvent e ) {
		for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener rtListener : this.renderTargetListeners ) {
			rtListener.rendered( e );
		}
	}

	/* package-private */void fireResized( edu.cmu.cs.dennisc.renderer.event.RenderTargetResizeEvent e ) {
		for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener rtListener : this.renderTargetListeners ) {
			rtListener.resized( e );
		}
	}

	/* package-private */void fireDisplayChanged( edu.cmu.cs.dennisc.renderer.event.RenderTargetDisplayChangeEvent e ) {
		for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener rtListener : this.renderTargetListeners ) {
			rtListener.displayChanged( e );
		}
	}

	/*package-private*/void performDisplayTasks( javax.media.opengl.GLAutoDrawable drawable, javax.media.opengl.GL2 gl ) {
		synchronized( this.displayTasks ) {
			if( this.displayTasks.size() > 0 ) {
				for( DisplayTask displayTask : this.displayTasks ) {
					displayTask.handleDisplay( this, drawable, gl );
				}
			}
		}
	}

	private static class ReusableLookingGlassRenderEvent extends edu.cmu.cs.dennisc.renderer.event.RenderTargetRenderEvent {
		public ReusableLookingGlassRenderEvent( GlrRenderTarget lookingGlass, Graphics2D g ) {
			super( lookingGlass, g );
		}

		@Override
		public boolean isReservedForReuse() {
			return true;
		}

		private void prologue() {
			( (Graphics2D)getGraphics2D() ).initialize( getTypedSource().getSurfaceSize() );
		}

		private void epilogue() {
			getGraphics2D().dispose();
		}
	}

	private boolean isListening;

	public boolean isListening() {
		return this.isListening;
	}

	public void startListening( javax.media.opengl.GLAutoDrawable drawable ) {
		if( this.isListening ) {
			if( drawable == this.drawable ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( drawable, this.drawable );
			}
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "request GLEventAdapter.startListening( drawable ) ignored; already listening." );
		} else {
			this.isListening = true;
			this.drawable = drawable;
			this.drawable.addGLEventListener( this.glEventListener );
		}
	}

	public void stopListening( javax.media.opengl.GLAutoDrawable drawable ) {
		if( drawable == this.drawable ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( drawable, this.drawable );
		}
		if( this.isListening ) {
			this.isListening = false;
			drawable.removeGLEventListener( this.glEventListener );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "request GLEventAdapter.stopListening( drawable ) ignored; already not listening." );
		}
		this.drawable = null;
	}

	//	private void paintOverlay() {
	//		edu.cmu.cs.dennisc.lookingglass.Overlay overlay = this.lookingGlass.getOverlay();
	//		if( overlay != null ) {
	//
	//			this.renderContext.gl.glMatrixMode( GL_PROJECTION );
	//			this.renderContext.gl.glPushMatrix();
	//			this.renderContext.gl.glLoadIdentity();
	//			this.renderContext.gl.glOrtho( 0, this.lookingGlass.getWidth() - 1, this.lookingGlass.getHeight() - 1, 0, -1, 1 );
	//			this.renderContext.gl.glMatrixMode( GL_MODELVIEW );
	//			this.renderContext.gl.glPushMatrix();
	//			this.renderContext.gl.glLoadIdentity();
	//
	//			this.renderContext.gl.glDisable( GL_DEPTH_TEST );
	//			this.renderContext.gl.glDisable( GL_LIGHTING );
	//			this.renderContext.gl.glDisable( GL_CULL_FACE );
	//			this.renderContext.setDiffuseColorTextureAdapter( null );
	//			this.renderContext.setBumpTextureAdapter( null );
	//
	//
	//			try {
	//				overlay.paint( this.lookingGlass );
	//				this.renderContext.gl.glFlush();
	//			} finally {
	//				this.renderContext.gl.glMatrixMode( GL_PROJECTION );
	//				this.renderContext.gl.glPopMatrix();
	//				this.renderContext.gl.glMatrixMode( GL_MODELVIEW );
	//				this.renderContext.gl.glPopMatrix();
	//			}
	//		}
	//	}

	private void performRender() {
		edu.cmu.cs.dennisc.renderer.RenderTarget rt = this.getRenderTarget();
		if( rt.isRenderingEnabled() ) {
			this.renderContext.actuallyForgetTexturesIfNecessary();
			this.renderContext.actuallyForgetDisplayListsIfNecessary();
			if( this.isDisplayIgnoredDueToPreviousException ) {
				//pass
			} else if( ( this.width == 0 ) || ( this.height == 0 ) ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this.width, this.height, rt.getSurfaceSize() );
			} else {
				try {
					//todo: separate clearing and rendering
					this.reusableLookingGlassRenderEvent.prologue();
					try {
						this.fireCleared( this.reusableLookingGlassRenderEvent );
					} finally {
						this.reusableLookingGlassRenderEvent.epilogue();
					}
					if( rt.getSgCameraCount() > 0 ) {
						this.renderContext.initialize();
						for( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera : this.sgCameras ) {
							AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapterI = AdapterFactory.getAdapterFor( sgCamera );
							cameraAdapterI.performClearAndRenderOffscreen( this.renderContext, this.width, this.height );
							this.reusableLookingGlassRenderEvent.prologue();
							try {
								cameraAdapterI.postRender( this.renderContext, this.width, this.height, rt, this.reusableLookingGlassRenderEvent.getGraphics2D() );
							} finally {
								this.reusableLookingGlassRenderEvent.epilogue();
							}
						}
						this.renderContext.renderLetterboxingIfNecessary( this.width, this.height );
					} else {
						this.renderContext.gl.glClearColor( 0, 0, 0, 1 );
						this.renderContext.gl.glClear( GL_COLOR_BUFFER_BIT );
					}
					this.reusableLookingGlassRenderEvent.prologue();
					try {
						this.fireRendered( this.reusableLookingGlassRenderEvent );
					} finally {
						this.reusableLookingGlassRenderEvent.epilogue();
					}
					if( ( this.rvColorBuffer != null ) || ( this.rvDepthBuffer != null ) ) {
						this.renderContext.captureBuffers( this.rvColorBuffer, this.rvDepthBuffer, this.atIsUpsideDown );
					}

				} catch( RuntimeException re ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "rendering will be disabled due to exception" );
					this.isDisplayIgnoredDueToPreviousException = true;
					re.printStackTrace();
					throw re;
				} catch( Error er ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "rendering will be disabled due to exception" );
					this.isDisplayIgnoredDueToPreviousException = true;
					er.printStackTrace();
					throw er;
				}
			}
		}
	}

	private java.awt.image.BufferedImage createBufferedImageForUseAsColorBuffer( int type ) {
		if( this.drawable != null ) {
			if( ( this.width != GlDrawableUtilities.getGlDrawableWidth( this.drawable ) ) || ( this.height != GlDrawableUtilities.getGlDrawableHeight( this.drawable ) ) ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "warning: createBufferedImageForUseAsColorBuffer size mismatch" );
				this.width = GlDrawableUtilities.getGlDrawableWidth( this.drawable );
				this.height = GlDrawableUtilities.getGlDrawableHeight( this.drawable );
			}
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "warning: drawable null" );
		}

		if( ( this.width > 0 ) && ( this.height > 0 ) ) {
			return new java.awt.image.BufferedImage( this.width, this.height, type );
		} else {
			return null;
		}
	}

	public java.awt.image.BufferedImage createBufferedImageForUseAsColorBuffer() {
		//		boolean isClearedToCreateImage;
		//		if( this.this.renderContext.gl != null ) {
		//			String extensions = this.this.renderContext.gl.glGetString( GL_EXTENSIONS );
		//			if( extensions != null ) {
		//				boolean isABGRExtensionSupported = extensions.contains( "GL_EXT_abgr" );
		//				if( isABGRExtensionSupported ) {
		//					//pass
		//				} else {
		//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "createBufferedImageForUseAsColorBuffer: capturing images from gl is expected to fail since since GL_EXT_abgr not found in: " );
		//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "\t" + extensions );
		//				}
		//				isClearedToCreateImage = isABGRExtensionSupported;
		//			} else {
		//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "createBufferedImageForUseAsColorBuffer: capturing images from gl is expected to fail since since gl.glGetString( GL_EXTENSIONS ) returns null." );
		//				isClearedToCreateImage = false;
		//			}
		//		} else {
		//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "createBufferedImageForUseAsColorBuffer: opengl is not initialized yet, so we will assume the GL_EXT_abgr extension is present." );
		//			isClearedToCreateImage = true;
		//		}
		//
		//
		//		//todo: investigate
		//		if( isClearedToCreateImage ) {
		//			//pass
		//		} else {
		//			isClearedToCreateImage = true;
		//		}
		//
		//		if( isClearedToCreateImage ) {
		//			//todo:
		//			//int type = java.awt.image.BufferedImage.TYPE_3BYTE_ABGR;
		//			int type = java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
		//			//int type = java.awt.image.BufferedImage.TYPE_INT_ARGB;
		//			return createBufferedImageForUseAsColorBuffer( type );
		//		} else {
		//			return null;
		//		}
		int type = java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
		return createBufferedImageForUseAsColorBuffer( type );
	}

	public java.awt.image.BufferedImage getColorBuffer( java.awt.image.BufferedImage rv, boolean[] atIsUpsideDown ) {
		return this.getColorBufferWithTransparencyBasedOnDepthBuffer( rv, null, atIsUpsideDown );
	}

	public java.awt.image.BufferedImage createBufferedImageForUseAsColorBufferWithTransparencyBasedOnDepthBuffer() {
		return createBufferedImageForUseAsColorBuffer( java.awt.image.BufferedImage.TYPE_4BYTE_ABGR );
	}

	public java.nio.FloatBuffer createFloatBufferForUseAsDepthBuffer() {
		return java.nio.FloatBuffer.allocate( this.width * this.height );
	}

	public java.nio.FloatBuffer getDepthBuffer( java.nio.FloatBuffer rv ) {
		this.rvDepthBuffer = rv;
		this.drawable.setAutoSwapBufferMode( false );
		try {
			this.drawable.display();
		} finally {
			this.rvDepthBuffer = null;
			this.drawable.setAutoSwapBufferMode( true );
		}
		return rv;
	}

	public java.awt.image.BufferedImage getColorBufferWithTransparencyBasedOnDepthBuffer( java.awt.image.BufferedImage rv, java.nio.FloatBuffer depthBuffer, boolean[] atIsUpsideDown ) {
		javax.media.opengl.GLContext glCurrentContext = javax.media.opengl.GLContext.getCurrent();
		if( ( glCurrentContext != null ) && ( glCurrentContext == this.drawable.getContext() ) ) {
			this.renderContext.captureBuffers( rv, depthBuffer, atIsUpsideDown );
		} else {
			if( this.rvColorBuffer != null ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this.rvColorBuffer );
			}
			this.rvColorBuffer = rv;
			this.rvDepthBuffer = depthBuffer;
			this.atIsUpsideDown = atIsUpsideDown;
			this.drawable.setAutoSwapBufferMode( false );
			try {
				this.drawable.display();
			} finally {
				this.rvColorBuffer = null;
				this.rvDepthBuffer = null;
				this.atIsUpsideDown = null;
				this.drawable.setAutoSwapBufferMode( true );
			}
		}
		return rv;
	}

	private void initialize( javax.media.opengl.GLAutoDrawable drawable ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "initialize", drawable );
		assert drawable == this.drawable;
		javax.media.opengl.GL2 gl = drawable.getGL().getGL2();
		ConformanceTestResults.SINGLETON.updateRenderInformationIfNecessary( gl );

		//edu.cmu.cs.dennisc.print.PrintUtilities.println( drawable.getChosenGLCapabilities() );

		final boolean USE_DEBUG_GL = false;
		if( USE_DEBUG_GL ) {
			if( gl instanceof javax.media.opengl.DebugGL2 ) {
				// pass
			} else {
				gl = new javax.media.opengl.DebugGL2( gl );
				edu.cmu.cs.dennisc.java.util.logging.Logger.info( "using debug gl: ", gl );
				drawable.setGL( gl );
			}
		}

		this.width = GlDrawableUtilities.getGlDrawableWidth( drawable );
		this.height = GlDrawableUtilities.getGlDrawableHeight( drawable );

		this.renderContext.setGL( gl );
		this.fireInitialized( new edu.cmu.cs.dennisc.renderer.event.RenderTargetInitializeEvent( this.getRenderTarget(), GlDrawableUtilities.getGlDrawableWidth( this.drawable ), GlDrawableUtilities.getGlDrawableHeight( this.drawable ) ) );
	}

	//todo: investigate not being invoked
	private void handleInit( javax.media.opengl.GLAutoDrawable drawable ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "init", drawable );
		initialize( drawable );
	}

	private void handleDisplay( javax.media.opengl.GLAutoDrawable drawable ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "display:", drawable );
		assert drawable == this.drawable;
		//this.lookingGlass.commitAnyPendingChanges();
		//todo?
		javax.media.opengl.GL2 gl = drawable.getGL().getGL2();
		if( this.renderContext.gl != null ) {
			//pass
		} else {
			initialize( drawable );
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "note: initialize necessary from display" );
		}
		if( ( this.width > 0 ) && ( this.height > 0 ) ) {
			//pass
		} else {
			int nextWidth = GlDrawableUtilities.getGlDrawableWidth( drawable );
			int nextHeight = GlDrawableUtilities.getGlDrawableHeight( drawable );
			if( ( this.width != nextWidth ) || ( this.height != nextHeight ) ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this.width, this.height, nextWidth, nextHeight );
				this.width = nextWidth;
				this.height = nextHeight;
			}
		}
		this.renderContext.setGL( gl );

		performRender();

		this.performDisplayTasks( drawable, gl );
	}

	private void handleReshape( javax.media.opengl.GLAutoDrawable drawable, int x, int y, int width, int height ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "reshape", drawable, x, y, width, height );
		assert drawable == this.drawable;
		this.width = width;
		this.height = height;
		this.fireResized( new edu.cmu.cs.dennisc.renderer.event.RenderTargetResizeEvent( this.getRenderTarget(), width, height ) );
	}

	//	public void displayChanged( javax.media.opengl.GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged ) {
	//		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "displayChanged", drawable, modeChanged, deviceChanged );
	//		assert drawable == this.drawable;
	//		this.rtImp.fireDisplayChanged( new edu.cmu.cs.dennisc.renderer.event.RenderTargetDisplayChangeEvent( this.rtImp.getRenderTarget(), modeChanged, deviceChanged ) );
	//	}

	private void handleDispose( javax.media.opengl.GLAutoDrawable drawable ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( drawable );
	}

	private final RenderContext renderContext = new RenderContext();

	private javax.media.opengl.GLAutoDrawable drawable;
	private int width;
	private int height;

	private java.awt.image.BufferedImage rvColorBuffer = null;
	private java.nio.FloatBuffer rvDepthBuffer = null;
	private boolean[] atIsUpsideDown = null;

	private boolean isDisplayIgnoredDueToPreviousException = false;
	private final ReusableLookingGlassRenderEvent reusableLookingGlassRenderEvent;

	private final edu.cmu.cs.dennisc.renderer.gl.GlrRenderTarget glrRT;

	private final SynchronousPicker synchronousPicker = new SynchronousPicker( this );
	private final SynchronousImageCapturer synchronousImageCapturer = new SynchronousImageCapturer( this );

	private final GlrAsynchronousPicker asynchronousPicker = new GlrAsynchronousPicker( this );
	private final GlrAsynchronousImageCapturer asynchronousImageCapturer = new GlrAsynchronousImageCapturer( this );

	private final java.util.List<edu.cmu.cs.dennisc.renderer.event.RenderTargetListener> renderTargetListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	private final java.util.List<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> sgCameras = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<DisplayTask> displayTasks = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

	//
	private static java.awt.Rectangle s_actualViewportBufferForReuse = new java.awt.Rectangle();
	private final javax.media.opengl.GLEventListener glEventListener = new javax.media.opengl.GLEventListener() {
		@Override
		public void init( javax.media.opengl.GLAutoDrawable drawable ) {
			handleInit( drawable );
		}

		@Override
		public void display( javax.media.opengl.GLAutoDrawable drawable ) {
			handleDisplay( drawable );
		}

		@Override
		public void reshape( javax.media.opengl.GLAutoDrawable drawable, int x, int y, int width, int height ) {
			handleReshape( drawable, x, y, width, height );
		}

		@Override
		public void dispose( javax.media.opengl.GLAutoDrawable drawable ) {
			handleDispose( drawable );
		}
	};
}
