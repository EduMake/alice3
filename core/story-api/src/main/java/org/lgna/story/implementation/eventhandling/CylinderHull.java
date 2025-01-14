/*******************************************************************************
 * Copyright (c) 2021 Carnegie Mellon University. All rights reserved.
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

import edu.cmu.cs.dennisc.math.Point2;
import edu.cmu.cs.dennisc.math.Point3;

import java.util.ArrayList;
import java.util.List;

public class CylinderHull extends VerticalPrismCollisionHull {
  private static final int APPROXIMATION_VERTEX_COUNT = 12;
  private final double radius;

  public CylinderHull(Point3 centerBase, double height, double radius) {
    super(centerBase, height);
    this.radius = radius;
  }

  @Override
  public double distanceAlong(double xDistance, double zDistance) {
    return radius;
  }

  @Override
  protected List<Point2> getCrossSectionVertices(Point3 newCenter) {
    List<Point2> vertices = new ArrayList<>();
    Point3 offset = newCenter == null ? Point3.createZero() : Point3.createSubtraction(newCenter, centerBase);
    for (int i = 0; i < APPROXIMATION_VERTEX_COUNT; i++) {
      double x = Math.sin(i * 2 * Math.PI / APPROXIMATION_VERTEX_COUNT) * radius;
      double z = Math.cos(i * 2 * Math.PI / APPROXIMATION_VERTEX_COUNT) * radius;
      vertices.add(new Point2(x + offset.x, z + offset.z));
    }
    return vertices;
  }
}
