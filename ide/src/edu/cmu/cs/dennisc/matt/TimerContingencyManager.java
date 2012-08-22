package edu.cmu.cs.dennisc.matt;

import java.util.ArrayList;

import org.lgna.story.SThing;
import org.lgna.story.SModel;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SScene;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.ComesIntoViewEvent;
import org.lgna.story.event.EndCollisionEvent;
import org.lgna.story.event.EnterProximityEvent;
import org.lgna.story.event.ExitProximityEvent;
import org.lgna.story.event.ViewExitListener;
import org.lgna.story.event.LeavesViewEvent;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.ProximityEnterListener;
import org.lgna.story.event.ProximityExitListener;
import org.lgna.story.event.StartCollisionEvent;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.WhileCollisionListener;
import org.lgna.story.event.WhileInViewListener;
import org.lgna.story.event.WhileOcclusionListener;
import org.lgna.story.event.WhileProximityListener;
import org.lgna.story.implementation.SceneImp;

public class TimerContingencyManager {

	private TimerEventHandler timer;
	private SScene scene;

	public TimerContingencyManager( TimerEventHandler timer ) {
		this.timer = timer;
	}

	public void register( WhileCollisionListener listener, ArrayList<SThing> groupOne, ArrayList<SThing> groupTwo, Double frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		scene.addCollisionStartListener( newStartCollisionAdapter( listener ), toArray( groupOne ), toArray( groupTwo ) );
		scene.addCollisionEndListener( newEndCollisionAdapter( listener ), toArray( groupOne ), toArray( groupTwo ) );
	}
	public void register( WhileProximityListener listener, ArrayList<SThing> groupOne, ArrayList<SThing> groupTwo, Double dist, Double frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		scene.addProximityEnterListener( newEnterProximityAdapter( listener ), toArray( groupOne ), toArray( groupTwo ), dist );
		scene.addProximityExitListener( newExitProximityAdapter( listener ), toArray( groupOne ), toArray( groupTwo ), dist );
	}
	public void register( WhileOcclusionListener listener, ArrayList<SModel> groupOne, ArrayList<SModel> groupTwo, Double frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		scene.addOcclusionStartListener( newEnterOcclusionAdapter( listener ), (SModel[])toArray( groupOne ), (SModel[])toArray( groupTwo ) );
		scene.addOcclusionEndListener( newExitOcclusionAdapter( listener ), (SModel[])toArray( groupOne ), (SModel[])toArray( groupTwo ) );
	}

	public void register( WhileInViewListener listener, ArrayList<SModel> group, Double frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		scene.addViewEnterListener( newEnterViewAdapter( listener ), (SModel[])toArray( group ) );
		scene.addViewExitListener( newExitViewAdapter( listener ), (SModel[])toArray( group ) );
	}

	private ViewExitListener newExitViewAdapter( final WhileInViewListener listener ) {
		return new ViewExitListener() {
			public void leftView( LeavesViewEvent e ) {
				timer.deactivate( listener );
			}
		};
	}

	private ViewEnterListener newEnterViewAdapter( final WhileInViewListener listener ) {
		return new ViewEnterListener() {
			public void viewEntered( ComesIntoViewEvent e ) {
				timer.activate( listener );
			}
		};
	}

	private OcclusionStartListener newEnterOcclusionAdapter( final WhileOcclusionListener listener ) {
		return new OcclusionStartListener() {
			public void occlusionStarted( StartOcclusionEvent e ) {
				timer.activate( listener );
			}
		};
	}

	private OcclusionEndListener newExitOcclusionAdapter( final WhileOcclusionListener listener ) {
		return new OcclusionEndListener() {
			public void occlusionEnded( EndOcclusionEvent e ) {
				timer.deactivate( listener );
			}
		};
	}

	private ProximityEnterListener newEnterProximityAdapter( final WhileProximityListener listener ) {
		return new ProximityEnterListener() {
			public void proximityEntered( EnterProximityEvent e ) {
				timer.activate( listener );
			}
		};
	}

	private ProximityExitListener newExitProximityAdapter( final WhileProximityListener listener ) {
		return new ProximityExitListener() {
			public void proximityExited( ExitProximityEvent e ) {
				timer.deactivate( listener );
			}
		};
	}

	private CollisionEndListener newEndCollisionAdapter( final WhileCollisionListener listener ) {
		return new CollisionEndListener() {
			public void collisionEnded( EndCollisionEvent e ) {
				timer.deactivate( listener );
			}
		};
	}

	private CollisionStartListener newStartCollisionAdapter( final WhileCollisionListener listener ) {
		return new CollisionStartListener() {
			public void collisionStarted( StartCollisionEvent e ) {
				timer.activate( listener );
			}
		};
	}

	private SThing[] toArray( ArrayList<? extends SThing> arr ) {
		SThing[] rv = new SThing[ arr.size() ];
		for( int i = 0; i != arr.size(); ++i ) {
			rv[ i ] = arr.get( i );
		}
		return rv;
	}

	public void setScene( SceneImp scene ) {
		this.scene = scene.getAbstraction();
	}
}
