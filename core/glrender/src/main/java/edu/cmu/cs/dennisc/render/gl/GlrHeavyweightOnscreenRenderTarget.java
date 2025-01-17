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

package edu.cmu.cs.dennisc.render.gl;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.awt.GLCanvas;
import edu.cmu.cs.dennisc.render.HeavyweightOnscreenRenderTarget;
import edu.cmu.cs.dennisc.render.RenderCapabilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * @author Dennis Cosgrove
 */
class GlrHeavyweightOnscreenRenderTarget extends GlrOnscreenRenderTarget<Component> implements HeavyweightOnscreenRenderTarget {

  /* package-private */ GlrHeavyweightOnscreenRenderTarget(GlrRenderFactory lookingGlassFactory, RenderCapabilities requestedCapabilities) {
    super(lookingGlassFactory, requestedCapabilities);
    m_glCanvas = GlDrawableUtils.createGLCanvas(requestedCapabilities);
    //m_glCanvas.getChosenGLCapabilities().getDepthBits();
    //m_glCanvas.setAutoSwapBufferMode( false );
    m_glCanvas.addComponentListener(new ComponentListener() {
      @Override
      public void componentShown(ComponentEvent e) {
      }

      @Override
      public void componentHidden(ComponentEvent e) {
      }

      @Override
      public void componentMoved(ComponentEvent e) {
      }

      @Override
      public void componentResized(ComponentEvent e) {
        m_glCanvas.setMinimumSize(new Dimension(0, 0));
        m_glCanvas.repaint();
      }
    });
  }

  @Override
  public Component getAwtComponent() {
    return m_glCanvas;
  }

  @Override
  protected Dimension getSurfaceSize(Dimension rv) {
    return m_glCanvas.getSize(rv);
  }

  @Override
  protected Dimension getDrawableSize(Dimension rv) {
    rv.setSize(m_glCanvas.getDelegatedDrawable().getSurfaceWidth(), m_glCanvas.getDelegatedDrawable().getSurfaceHeight());
    return rv;
  }

  @Override
  public void repaint() {
    getAwtComponent().repaint();
  }

  @Override
  public GLAutoDrawable getGLAutoDrawable() {
    return m_glCanvas;
  }

  private GLCanvas m_glCanvas;
}
