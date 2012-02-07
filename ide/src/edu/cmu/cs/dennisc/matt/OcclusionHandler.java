package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.OcclusionEvent;
import org.lgna.story.event.OcclusionEventListener;
import org.lgna.story.implementation.AbstractTransformableImp;
import org.lgna.story.implementation.CameraImp;

import edu.cmu.cs.dennisc.java.util.Collections;

public class OcclusionHandler extends TransformationChangedHandler < OcclusionEventListener, OcclusionEvent > {
	

	private OcclusionEventHandler occlusionEventHandler;
	private CameraImp camera;
	
	public OcclusionHandler(CameraImp cameraImp) {
		this.camera = cameraImp;
	}

	public void addOcclusionEvent( OcclusionEventListener occlusionEventListener, List< Entity > groupOne, List< Entity > groupTwo ) {
		registerIsFiringMap(occlusionEventListener);
		registerPolicyMap(occlusionEventListener, MultipleEventPolicy.IGNORE);
		List< Entity > allObserving = Collections.newArrayList( groupOne );
		allObserving.addAll( groupTwo );
		for( Entity m : allObserving ) {
			if( !modelList.contains( m ) ) {
				modelList.add( m );
				ImplementationAccessor.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
				occlusionEventHandler.register( occlusionEventListener, groupOne, groupTwo );
			}
		}
	}
	
	@Override
	protected void check(Entity changedEntity) {
		occlusionEventHandler.check(changedEntity);
	}

	@Override
	protected void nameOfFireCall(OcclusionEventListener listener, OcclusionEvent event) {
		listener.whenTheseOcclude( event );
	}
	
	private class OcclusionEventHandler {

		private HashMap< Entity, LinkedList< Entity >> checkMap = new HashMap< Entity, LinkedList< Entity >>();
		private HashMap< Entity, HashMap< Entity, LinkedList< OcclusionEventListener >>> eventMap = new HashMap< Entity, HashMap< Entity, LinkedList< OcclusionEventListener >>>();

		public void check( Entity changedEntity ) {
			for( Entity m : checkMap.get( changedEntity ) ) {
				if( AabbCollisionDetector.doTheseCollide( m, changedEntity ) ) {
					LinkedList< Entity > models = new LinkedList< Entity >();
					if ( camera.getDistanceTo( (AbstractTransformableImp) ImplementationAccessor.getImplementation( m ) ) <
							camera.getDistanceTo( (AbstractTransformableImp) ImplementationAccessor.getImplementation( changedEntity ) ) ){
						models.add( m );
						models.add( changedEntity );
					} else {
						models.add( m );
						models.add( changedEntity );
					}
					for( OcclusionEventListener colList : eventMap.get( changedEntity ).get( m ) ) {
						fireEvent( colList, new OcclusionEvent( models ) );
					}
				}
			}
		}

		public void register( OcclusionEventListener occlusionListener, List< Entity > groupOne, List< Entity > groupTwo ) {
			for( Entity m : groupOne ) {
				if( eventMap.get( m ) == null ) {
					eventMap.put( m, new HashMap< Entity, LinkedList< OcclusionEventListener >>() );
					checkMap.put( m, new LinkedList< Entity >() );
				}
				for( Entity t : groupTwo ) {
					if( eventMap.get( m ).get( t ) == null ) {
						eventMap.get( m ).put( t, new LinkedList< OcclusionEventListener >() );
					}
					if( !m.equals( t ) ) {
						eventMap.get( m ).get( t ).add( occlusionListener );
						if(!checkMap.get( m ).contains( t )){
							checkMap.get( m ).add( t );
						}
					}
				}
			}
			for( Entity m : groupTwo ) {
				if( eventMap.get( m ) == null ) {
					eventMap.put( m, new HashMap< Entity, LinkedList< OcclusionEventListener >>() );
					checkMap.put( m, new LinkedList< Entity >() );
				}
				for( Entity t : groupOne ) {
					if( eventMap.get( m ).get( t ) == null ) {
						eventMap.get( m ).put( t, new LinkedList< OcclusionEventListener >() );
					}
					if( !m.equals( t ) ) {
						eventMap.get( m ).get( t ).add( occlusionListener );
						if(!checkMap.get( m ).contains( t )){
							checkMap.get( m ).add( t );
						}
					}
				}
			}
		}
	}

}
