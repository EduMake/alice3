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
package org.alice.ide.x.components;

import org.alice.ide.common.AbstractArgumentListPropertyPane;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.x.ImmutableAstI18nFactory;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.project.annotations.AddEventListenerTemplate;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractParameter;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Code;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.SimpleArgumentListProperty;

/**
 * @author Dennis Cosgrove
 */
public class ArgumentListPropertyPane extends AbstractArgumentListPropertyPane {
  public ArgumentListPropertyPane(ImmutableAstI18nFactory factory, SimpleArgumentListProperty property) {
    super(factory, property);
  }

  @Override
  protected boolean isComponentDesiredFor(SimpleArgument argument, int i, int N) {
    if (i == 0) {
      if (argument != null) {
        AbstractParameter parameter = argument.parameter.getValue();
        if (parameter != null) {
          Code code = parameter.getCode();
          if (code instanceof JavaMethod) {
            JavaMethod javaMethod = (JavaMethod) code;
            if (javaMethod.isAnnotationPresent(AddEventListenerTemplate.class)) {
              AbstractType<?, ?, ?> parameterType = parameter.getValueType();
              if (parameterType != null) {
                if (parameterType.isInterface()) {
                  //assume it is going to be a lambda
                  return parameterType.getDeclaredMethods().size() != 1;
                }
              }
            }
          }
        }
      }
    }
    return super.isComponentDesiredFor(argument, i, N);
  }

  @Override
  protected AwtComponentView<?> createComponent(SimpleArgument argument) {
    AwtComponentView<?> expressionComponent = this.getFactory().createExpressionPane(argument.expression.getValue());
    AbstractParameter parameter = argument.parameter.getValue();
    final boolean IS_PARAMETER_NAME_DESIRED = parameter.getParent() instanceof AbstractMethod;
    if (IS_PARAMETER_NAME_DESIRED) {
      String parameterName = FormatterState.getInstance().getValue().getNameForDeclaration(parameter);
      if ((parameterName != null) && (parameterName.length() > 0)) {
        LineAxisPanel rv = new LineAxisPanel();
        rv.addComponent(new Label(parameterName + ": "));
        rv.addComponent(expressionComponent);
        return rv;
      } else {
        return expressionComponent;
      }
    } else {
      return expressionComponent;
    }
  }
}
