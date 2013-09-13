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
package edu.cmu.cs.dennisc.video.vlcj;

import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * @author Dennis Cosgrove
 */
public class VlcjVideoPlayer implements edu.cmu.cs.dennisc.video.VideoPlayer {
	private final uk.co.caprica.vlcj.player.MediaPlayerEventListener mediaPlayerEventListener = new uk.co.caprica.vlcj.player.MediaPlayerEventListener() {
		public void mediaChanged( MediaPlayer mediaPlayer, uk.co.caprica.vlcj.binding.internal.libvlc_media_t media, String mrl ) {
			//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "mediaChanged", mediaPlayer, media, mrl );
			//uk.co.caprica.vlcj.player.MediaDetails mediaDetails = mediaPlayer.getMediaDetails();
			fireMediaChanged();
		}

		public void opening( MediaPlayer mediaPlayer ) {
			fireOpening();
		}

		public void buffering( MediaPlayer mediaPlayer, float newCache ) {
		}

		public void playing( MediaPlayer mediaPlayer ) {
			firePlaying();
		}

		public void paused( MediaPlayer mediaPlayer ) {
			firePaused();
		}

		public void stopped( MediaPlayer mediaPlayer ) {
			fireStopped();
		}

		public void forward( MediaPlayer mediaPlayer ) {
		}

		public void backward( MediaPlayer mediaPlayer ) {
		}

		public void finished( MediaPlayer mediaPlayer ) {
			fireFinished();
		}

		public void timeChanged( MediaPlayer mediaPlayer, long newTime ) {
			//todo
		}

		public void positionChanged( MediaPlayer mediaPlayer, float newPosition ) {
			firePositionChanged( newPosition );
		}

		public void seekableChanged( MediaPlayer mediaPlayer, int newSeekable ) {
		}

		public void pausableChanged( MediaPlayer mediaPlayer, int newSeekable ) {
		}

		public void titleChanged( MediaPlayer mediaPlayer, int newTitle ) {
		}

		public void snapshotTaken( MediaPlayer mediaPlayer, String filename ) {
		}

		public void lengthChanged( MediaPlayer mediaPlayer, long newLength ) {
			fireLengthChanged( newLength );
		}

		public void videoOutput( MediaPlayer mediaPlayer, int newCount ) {
			fireVideoOutput( newCount );
		}

		public void error( MediaPlayer mediaPlayer ) {
			fireError();
		}

		public void mediaMetaChanged( MediaPlayer mediaPlayer, int metaType ) {
		}

		public void mediaSubItemAdded( MediaPlayer mediaPlayer, uk.co.caprica.vlcj.binding.internal.libvlc_media_t subItem ) {
		}

		public void mediaDurationChanged( MediaPlayer mediaPlayer, long newDuration ) {
		}

		public void mediaParsedChanged( MediaPlayer mediaPlayer, int newStatus ) {
		}

		public void mediaFreed( MediaPlayer mediaPlayer ) {
		}

		public void mediaStateChanged( MediaPlayer mediaPlayer, int newState ) {
		}

		public void newMedia( MediaPlayer mediaPlayer ) {
			fireNewMedia();
		}

		public void subItemPlayed( MediaPlayer mediaPlayer, int subItemIndex ) {
		}

		public void subItemFinished( MediaPlayer mediaPlayer, int subItemIndex ) {
		}

		public void endOfSubItems( MediaPlayer mediaPlayer ) {
		}

	};
	private final java.util.List<edu.cmu.cs.dennisc.video.event.MediaListener> mediaListeners = new java.util.concurrent.CopyOnWriteArrayList<edu.cmu.cs.dennisc.video.event.MediaListener>();
	private final VlcjMediaPlayerComponent mediaPlayerComponent;

	private String mediaPath = null;

	public VlcjVideoPlayer() {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
			// The mac for now needs to use a software renderer
			this.mediaPlayerComponent = new LightweightMediaPlayerComponent();
		} else {
			this.mediaPlayerComponent = new HeavyweightMediaPlayerComponent();
		}

		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		if( mediaPlayer instanceof uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer ) {
			uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer embeddedMediaPlayer = (uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer)mediaPlayer;
			embeddedMediaPlayer.setEnableMouseInputHandling( false );
			embeddedMediaPlayer.setEnableKeyInputHandling( false );
		}
		mediaPlayer.addMediaPlayerEventListener( this.mediaPlayerEventListener );
	}

	private void fireNewMedia() {
		this.markPausedPositionAndTimeInMillisecondsInvalid();
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.newMedia( this );
		}
	}

	private void fireVideoOutput( int count ) {
		this.markPausedPositionAndTimeInMillisecondsInvalid();
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.videoOutput( this, count );
		}
	}

	private void firePositionChanged( float position ) {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.positionChanged( this, position );
		}
	}

	private void fireLengthChanged( long length ) {
		this.markPausedPositionAndTimeInMillisecondsInvalid();
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.lengthChanged( this, length );
		}
	}

	private void fireMediaChanged() {
		this.markPausedPositionAndTimeInMillisecondsInvalid();
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.mediaChanged( this );
		}
	}

	private void fireOpening() {
		this.markPausedPositionAndTimeInMillisecondsInvalid();
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.opening( this );
		}
	}

	private void firePlaying() {
		this.markPausedPositionAndTimeInMillisecondsInvalid();
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.playing( this );
		}
	}

	private void firePaused() {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.paused( this );
		}
	}

	private void fireStopped() {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.stopped( this );
		}
	}

	private void fireFinished() {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.finished( this );
		}
	}

	private void fireError() {
		for( edu.cmu.cs.dennisc.video.event.MediaListener mediaListener : mediaListeners ) {
			mediaListener.error( this );
		}
	}

	public void addMediaListener( edu.cmu.cs.dennisc.video.event.MediaListener listener ) {
		this.mediaListeners.add( listener );
	}

	public void removeMediaListener( edu.cmu.cs.dennisc.video.event.MediaListener listener ) {
		this.mediaListeners.add( listener );
	}

	public java.awt.Component getVideoSurface() {
		return this.mediaPlayerComponent.getVideoSurface();
	}

	public edu.cmu.cs.dennisc.java.awt.Painter getPainter() {
		return this.mediaPlayerComponent.getPainter();
	}

	public void setPainter( edu.cmu.cs.dennisc.java.awt.Painter painter ) {
		this.mediaPlayerComponent.setPainter( painter );
	}

	public void prepareMedia( java.net.URI uri ) {
		if( uri != null ) {
			String scheme = uri.getScheme();
			if( scheme.equalsIgnoreCase( "file" ) ) {
				java.io.File file = new java.io.File( uri );
				this.mediaPath = file.getAbsolutePath();
			} else {
				this.mediaPath = uri.toString();
			}
			MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
			mediaPlayer.prepareMedia( this.mediaPath );
		} else {
			System.err.println( "uri is null" );
		}
	}

	public boolean isPlayable() {
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		return mediaPlayer.isPlayable();
	}

	public boolean isPlaying() {
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		return mediaPlayer.isPlaying();
	}

	public void playResume() {
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		if( mediaPlayer.isPlayable() ) {
			mediaPlayer.play();
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "not playable (starting):", mediaPlayer );
			//			if( mediaPlayer.isSeekable() ) {
			//				mediaPlayer.setPosition( 0.0f );
			//			}
			mediaPlayer.start();
		}
	}

	public void pause() {
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		if( mediaPlayer.canPause() ) {
			mediaPlayer.setPause( true );
		} else {
			System.err.println( "cannot pause " + mediaPlayer );
		}
	}

	public void stop() {
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		mediaPlayer.stop();
	}

	private Float pausedSetPosition = null;
	private Long pausedSetTimeInMilliseconds = null;

	private float getPausedSetPosition() {
		if( this.pausedSetTimeInMilliseconds != null ) {
			long n = this.getLengthInMilliseconds();
			if( n != 0 ) {
				return this.pausedSetTimeInMilliseconds / (float)this.getLengthInMilliseconds();
			} else {
				return Float.NaN;
			}
		} else {
			return this.pausedSetPosition != null ? this.pausedSetPosition : Float.NaN;
		}
	}

	public Long getPausedSetTimeInMilliseconds() {
		if( this.pausedSetPosition != null ) {
			long n = this.getLengthInMilliseconds();
			if( n != 0 ) {
				return (long)( this.pausedSetPosition * this.getLengthInMilliseconds() );
			} else {
				return null;
			}
		} else {
			return this.pausedSetTimeInMilliseconds;
		}
	}

	private void markPausedPositionAndTimeInMillisecondsInvalid() {
		this.pausedSetPosition = null;
		this.pausedSetTimeInMilliseconds = null;
	}

	public long getTimeInMilliseconds() {
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		if( mediaPlayer.canPause() ) {
			if( mediaPlayer.isPlaying() ) {
				//pass
			} else {
				Long t = this.getPausedSetTimeInMilliseconds();
				if( t != null ) {
					return t;
				} else {
					//pass
				}
			}
		}
		return mediaPlayer.getTime();
	}

	public void setTimeInMilliseconds( long timeInMilliseconds ) {
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		if( mediaPlayer.canPause() ) {
			if( mediaPlayer.isPlaying() ) {
				// pass
			} else {
				this.pausedSetPosition = null;
				this.pausedSetTimeInMilliseconds = timeInMilliseconds;
			}
		}
		mediaPlayer.setTime( timeInMilliseconds );
	}

	public float getPosition() {
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		if( mediaPlayer.canPause() ) {
			if( mediaPlayer.isPlaying() ) {
				//pass
			} else {
				float p = this.getPausedSetPosition();
				if( Float.isNaN( p ) == false ) {
					return p;
				} else {
					//pass
				}
			}
		}
		return mediaPlayer.getPosition();
	}

	public void setPosition( float position ) {
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		if( mediaPlayer.isSeekable() ) {
			//pass
		} else {
			//System.err.println( "cannot setPosition " + position + " " + mediaPlayer + " starting." );
			mediaPlayer.start();
			mediaPlayer.pause();
		}
		if( mediaPlayer.canPause() ) {
			if( mediaPlayer.isPlaying() ) {
				// pass
			} else {
				this.pausedSetPosition = position;
				this.pausedSetTimeInMilliseconds = null;
			}
		}
		mediaPlayer.setPosition( position );

		final boolean IS_FIRE_POSITION_CHANGED_DESIRED = false; //todo?
		if( IS_FIRE_POSITION_CHANGED_DESIRED ) {
			// VLCJ does not fire this unless the video is playing. This seems wrong, especially for gathering thumbnails.
			this.firePositionChanged( position );
		}
	}

	public long getLengthInMilliseconds() {
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		return mediaPlayer.getLength();
	}

	public float getVolume() {
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		return mediaPlayer.getVolume() * 0.01f;
	}

	public void setVolume( float volume ) {
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		mediaPlayer.setVolume( Math.round( volume * 100 ) ); // 200?
	}

	public boolean isMuted() {
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		return mediaPlayer.isMute();
	}

	public void setMuted( boolean isMuted ) {
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		mediaPlayer.mute( isMuted );
	}

	private static final boolean IS_FFMPEG_SNAPSHOT_IMPLEMENTATION_DESIRED = edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux();

	private float getTimeInSeconds() {
		// TODO: 1.0 means the video is over... so we need something to get the last frame of the video if it's 1.0.
		//		float position = ( this.getPosition() >= 1.0f ) ? 0.88f : this.getPosition();
		MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
		return mediaPlayer.getTime() * 0.001f;
	}

	public boolean writeSnapshot( java.io.File file ) {
		if( IS_FFMPEG_SNAPSHOT_IMPLEMENTATION_DESIRED ) {
			try {
				float seconds = this.getTimeInSeconds();
				edu.wustl.cse.lookingglass.media.FFmpegImageExtractor.getFrameAt( this.mediaPath, seconds, file );
				return true;
			} catch( Exception e ) {
				return false;
			}
		} else {
			MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
			return mediaPlayer.saveSnapshot( file );
		}
	}

	public java.awt.Image getSnapshot() {
		if( IS_FFMPEG_SNAPSHOT_IMPLEMENTATION_DESIRED ) {
			float seconds = this.getTimeInSeconds();
			return edu.wustl.cse.lookingglass.media.FFmpegImageExtractor.getFrameAt( this.mediaPath, seconds );
		} else {
			MediaPlayer mediaPlayer = this.mediaPlayerComponent.getMediaPlayer();
			return mediaPlayer.getSnapshot();
		}
	}

	public void release() {
		this.mediaPlayerComponent.release();
	}

	public java.awt.Dimension getVideoSize() {
		return this.mediaPlayerComponent.getMediaPlayer().getVideoDimension();
	}
}
