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
package org.alice.ide.declarationseditor.code.components;

import edu.cmu.cs.dennisc.pattern.Criterion;
import org.alice.ide.ast.draganddrop.CodeDragModel;
import org.alice.ide.code.UserFunctionStatusComposite;
import org.alice.ide.codedrop.CodePanelWithDropReceptor;
import org.alice.ide.codeeditor.ExpressionPropertyDropDownPane;
import org.alice.ide.controlflow.ControlFlowComposite;
import org.alice.ide.croquet.models.IdeDragModel;
import org.alice.ide.declarationseditor.CodeComposite;
import org.alice.ide.declarationseditor.components.DeclarationView;
import org.lgna.croquet.DropReceptor;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.HierarchyUtilities;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaType;

import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractCodeDeclarationView extends DeclarationView {
  public AbstractCodeDeclarationView(CodeComposite composite, CodePanelWithDropReceptor codePanelWithDropReceptor) {
    super(composite);
    this.codePanelWithDropReceptor = codePanelWithDropReceptor;

    AbstractCode code = composite.getDeclaration();

    SwingComponentView<?> controlFlowComponent = ControlFlowComposite.getInstance(code).getView();

    UserFunctionStatusComposite userFunctionStatusComposite = composite.getUserFunctionStatusComposite();
    SwingComponentView<?> pageEndComponent;
    if (userFunctionStatusComposite != null) {
      if (controlFlowComponent != null) {
        pageEndComponent = new BorderPanel.Builder().center(userFunctionStatusComposite.getView()).pageEnd(controlFlowComponent).build();
      } else {
        pageEndComponent = userFunctionStatusComposite.getView();
      }
    } else {
      pageEndComponent = controlFlowComponent;
    }

    if (pageEndComponent != null) {
      this.addPageEndComponent(pageEndComponent);
    }
    this.setBackgroundColor(this.codePanelWithDropReceptor.getBackgroundColor());
  }

  @Deprecated
  public final CodePanelWithDropReceptor getCodePanelWithDropReceptor() {
    return this.codePanelWithDropReceptor;
  }

  @Override
  public void addPotentialDropReceptors(List<DropReceptor> out, IdeDragModel dragModel) {
    if (dragModel instanceof CodeDragModel) {
      CodeDragModel codeDragModel = (CodeDragModel) dragModel;
      final AbstractType<?, ?, ?> type = codeDragModel.getType();
      if (type != JavaType.VOID_TYPE) {
        var list = HierarchyUtilities.findAllMatches(
            this,
            ExpressionPropertyDropDownPane.class,
            (Criterion<ExpressionPropertyDropDownPane>) expressionPropertyDropDownPane -> {
              AbstractType<?, ?, ?> expressionType = expressionPropertyDropDownPane.getExpressionProperty().getExpressionType();
              return expressionType != null && expressionType.isAssignableFrom(type);
        });
        for (ExpressionPropertyDropDownPane pane : list) {
          out.add(pane.getDropReceptor());
        }
      }
      CodePanelWithDropReceptor codePanelWithDropReceptor = this.getCodePanelWithDropReceptor();
      DropReceptor dropReceptor = codePanelWithDropReceptor.getDropReceptor();
      if (dropReceptor.isPotentiallyAcceptingOf(codeDragModel)) {
        out.add(dropReceptor);
      }
    }
  }

  @Override
  protected void setJavaCodeOnTheSide(boolean value, boolean isFirstTime) {
    super.setJavaCodeOnTheSide(value, isFirstTime);
    this.codePanelWithDropReceptor.setJavaCodeOnTheSide(value, isFirstTime);
  }

  private final CodePanelWithDropReceptor codePanelWithDropReceptor;
}
