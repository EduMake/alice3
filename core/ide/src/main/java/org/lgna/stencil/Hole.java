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
package org.lgna.stencil;

import org.lgna.croquet.resolvers.RuntimeResolver;
import org.lgna.croquet.views.TrackableShape;

import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;

/**
 * @author Dennis Cosgrove
 */
public class Hole extends Feature {
  private static final int PAD = 4;
  private static final int BOUNDS_PAD = PAD + 64;
  private static final Insets PAINT_INSETS = new Insets(PAD, PAD, PAD, PAD);
  private static final Insets BOUNDS_INSETS = new Insets(BOUNDS_PAD, BOUNDS_PAD, BOUNDS_PAD, BOUNDS_PAD);

  private final Painter painter;

  public Hole(RuntimeResolver<? extends TrackableShape> trackableShapeResolver, ConnectionPreference connectionPreference, Painter painter) {
    super(trackableShapeResolver, connectionPreference);
    this.painter = painter;
  }

  @Override
  protected boolean isPathRenderingDesired() {
    return true;
  }

  protected boolean isHoleRenderingDesired() {
    return true;
  }

  @Override
  protected Insets getBoundsInsets() {
    return BOUNDS_INSETS;
  }

  @Override
  protected Insets getContainsInsets() {
    return null;
  }

  @Override
  protected Insets getPaintInsets() {
    return PAINT_INSETS;
  }

  @Override
  protected void paint(Graphics2D g2, Shape shape, Connection actualConnection) {
    if (this.isHoleRenderingDesired()) {
      this.painter.paint(g2, shape);
    }
  }
}
