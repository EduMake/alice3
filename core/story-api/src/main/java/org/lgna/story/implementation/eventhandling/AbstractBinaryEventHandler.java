/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.story.implementation.eventhandling;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SThing;
import org.lgna.story.event.AbstractEvent;

/**
 * @author Matt May
 */
public abstract class AbstractBinaryEventHandler<L, E extends AbstractEvent, T extends SThing> extends TransformationChangedHandler<L, E> {

  private static final long MINIMUM_MILLIS_BETWEEN_CHECKS = 100;
  private final Map<SThing, Long> lastCheckTimes = Maps.newConcurrentHashMap();
  protected final Map<T, Map<T, Set<Object>>> interactionListeners = Maps.newConcurrentHashMap();

  protected void startTrackingListener(L listener, List<T> groupA, List<T> groupB, MultipleEventPolicy policy) {
    registerIsFiringMap(listener);
    registerPolicyMap(listener, policy);
    startTrackingThings(groupA);
    startTrackingThings(groupB);
    for (T a : groupA) {
      for (T b : groupB) {
        if (!a.equals(b)) {
          startTrackingInteraction(a, b, listener);
        }
      }
    }
  }

  private void startTrackingThings(List<T> things) {
    for (T thing : things) {
      startTrackingThing(thing);
    }
  }

  protected void startTrackingThing(T thing) {
    if (!getModelList().contains(thing)) {
      getModelList().add(thing);
      EmployeesOnly.getImplementation(thing).getSgComposite().addAbsoluteTransformationListener(this);
    }
    if (interactionListeners.get(thing) == null) {
      interactionListeners.put(thing, new ConcurrentHashMap<>());
    }
  }

  protected void startTrackingInteraction(T a, T b, Object listener) {
    interactionListeners.get(a).computeIfAbsent(b, k -> new LinkedHashSet<>()).add(listener);
    interactionListeners.get(b).computeIfAbsent(a, k -> new LinkedHashSet<>()).add(listener);
  }

  @Override
  protected void check(SThing changedThing) {
    long now = System.currentTimeMillis();
    long millisSinceLastUpdate = lastCheckTimes.getOrDefault(changedThing, 0L);
    if (now - millisSinceLastUpdate < MINIMUM_MILLIS_BETWEEN_CHECKS) {
      return;
    }
    lastCheckTimes.put(changedThing, now);
    checkForEvents(changedThing);
  }

  protected abstract void checkForEvents(SThing changedThing);

  protected boolean wasTrue(Map<T, Map<T, Boolean>> previousState, T a, T b) {
    final Map<T, Boolean> determinedStates = previousState.get(a);
    // If there is no entry, then this was not previously determined
    return determinedStates.containsKey(b) && determinedStates.get(b);
  }

  protected boolean wasFalse(Map<T, Map<T, Boolean>> previousState, T a, T b) {
    final Map<T, Boolean> determinedStates = previousState.get(a);
    // If there is no entry, then this was not previously determined
    return determinedStates.containsKey(b) && !determinedStates.get(b);
  }
}
