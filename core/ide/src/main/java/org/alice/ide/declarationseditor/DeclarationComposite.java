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

package org.alice.ide.declarationseditor;

import edu.cmu.cs.dennisc.property.StringProperty;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import org.alice.ide.IDE;
import org.alice.ide.declarationseditor.components.DeclarationView;
import org.lgna.croquet.AbstractTabComposite;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.NamedUserType;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class DeclarationComposite<D extends AbstractDeclaration, V extends DeclarationView> extends AbstractTabComposite<V> {
  @Deprecated
  public static synchronized DeclarationComposite<?, ?> getInstance(AbstractDeclaration declaration) {
    if (declaration instanceof AbstractCode) {
      return CodeComposite.getInstance((AbstractCode) declaration);
    } else if (declaration instanceof NamedUserType) {
      return TypeComposite.getInstance((NamedUserType) declaration);
    } else {
      if (declaration != null) {
        throw new RuntimeException("todo " + declaration);
      } else {
        return null;
      }
    }
  }

  private final D declaration;
  private final Class<D> declarationCls;

  public DeclarationComposite(UUID id, D declaration, Class<D> declarationCls) {
    super(id);
    this.declaration = declaration;
    this.declarationCls = declarationCls;

    StringProperty nameProperty = this.declaration.getNamePropertyIfItExists();
    if (nameProperty != null) {
      PropertyListener nameListener = e -> {
        String nextName = (String) e.getValue();
        IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getItemSelectedState(DeclarationComposite.this).setTextForBothTrueAndFalse(nextName);
      };
      nameProperty.addPropertyListener(nameListener);
    }
  }

  @Override
  protected String getSubKeyForLocalization() {
    return declaration.getName();
  }

  public D getDeclaration() {
    return this.declaration;
  }

  public abstract AbstractType<?, ?, ?> getType();

  public abstract boolean isValid();

  @Override
  protected final ScrollPane createScrollPaneIfDesired() {
    return null;
  }
}
