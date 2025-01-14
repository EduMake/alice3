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

package org.alice.ide.resource.manager.views;

import edu.cmu.cs.dennisc.media.Player;
import org.alice.ide.ast.rename.components.RenamePanel;
import org.alice.ide.resource.manager.RenameResourceComposite;
import org.alice.imageeditor.croquet.views.ImageView;
import org.lgna.common.Resource;
import org.lgna.common.resources.ImageResource;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.story.implementation.ImageFactory;

import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.image.BufferedImage;

/**
 * @author Dennis Cosgrove
 */
public class ResourceRenamePanel extends RenamePanel {
  private static final int SIZE = 128;
  private final BorderPanel centerPanel = new BorderPanel();
  private final ImageView imageView = new ImageView();
  private Player audioPlayer = null;

  public ResourceRenamePanel(RenameResourceComposite composite) {
    super(composite);
    this.centerPanel.setMinimumPreferredHeight(SIZE);
    this.centerPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
    this.addCenterComponent(this.centerPanel);
  }

  public void setResource(Resource resource) {
    if (this.audioPlayer != null) {
      this.audioPlayer.stop();
      this.audioPlayer = null;
    }
    this.centerPanel.forgetAndRemoveAllComponents();
    Component awtComponent;
    String constraint;
    if (resource instanceof ImageResource) {
      ImageResource imageResource = (ImageResource) resource;
      BufferedImage bufferedImage = ImageFactory.getBufferedImage(imageResource);
      imageView.setImage(bufferedImage);
      awtComponent = imageView.getAwtComponent();
      constraint = BorderLayout.CENTER;
   // else if (resource instanceof AudioResource) TODO restore player to this dialog when JMF is replaced - T925
    } else {
      awtComponent = null;
      constraint = null;
    }
    if (awtComponent != null) {
      this.centerPanel.getAwtComponent().add(awtComponent, constraint);
    }
  }

  public void onHide() {
    if (this.audioPlayer != null) {
      this.audioPlayer.stop();
      this.audioPlayer = null;
    }
  }
}
