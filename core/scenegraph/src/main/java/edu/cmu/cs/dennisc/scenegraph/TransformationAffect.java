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

package edu.cmu.cs.dennisc.scenegraph;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

/**
 * @author Dennis Cosgrove
 */

public enum TransformationAffect {
  AFFECT_TRANSLATION_X_ONLY(false, true, false, false), AFFECT_TRANSLATION_Y_ONLY(false, false, true, false), AFFECT_TRANSLATION_Z_ONLY(false, false, false, true), AFFECT_TRANSLATION_XY_ONLY(false, true, true, false), AFFECT_TRANSLATION_XZ_ONLY(false, true, false, true), AFFECT_TRANSLATION_YZ_ONLY(false, false, true, true), AFFECT_TRANSLATION_ONLY(false, true, true, true), AFFECT_ORIENTAION_ONLY(true, false, false, false), AFFECT_ALL(true, true, true, true);
  private boolean m_isAffectOrientationDesired;
  private boolean m_isAffectTranslationXDesired;
  private boolean m_isAffectTranslationYDesired;
  private boolean m_isAffectTranslationZDesired;

  TransformationAffect(boolean isAffectOrientationDesired, boolean isAffectTranslationXDesired, boolean isAffectTranslationYDesired, boolean isAffectTranslationZDesired) {
    m_isAffectOrientationDesired = isAffectOrientationDesired;
    m_isAffectTranslationXDesired = isAffectTranslationXDesired;
    m_isAffectTranslationYDesired = isAffectTranslationYDesired;
    m_isAffectTranslationZDesired = isAffectTranslationZDesired;
  }

  public void set(AffineMatrix4x4 dst, AffineMatrix4x4 src) {
    if (m_isAffectOrientationDesired) {
      dst.orientation.setValue(src.orientation);
    }
    if (m_isAffectTranslationXDesired) {
      dst.translation.x = src.translation.x;
    }
    if (m_isAffectTranslationYDesired) {
      dst.translation.y = src.translation.y;
    }
    if (m_isAffectTranslationZDesired) {
      dst.translation.z = src.translation.z;
    }
  }

  public static TransformationAffect getTranslationAffect(double x, double y, double z) {
    if (Double.isNaN(x)) {
      if (Double.isNaN(y)) {
        if (Double.isNaN(z)) {
          return null;
        } else {
          return AFFECT_TRANSLATION_Z_ONLY;
        }
      } else {
        if (Double.isNaN(z)) {
          return AFFECT_TRANSLATION_Y_ONLY;
        } else {
          return AFFECT_TRANSLATION_YZ_ONLY;
        }
      }
    } else {
      if (Double.isNaN(y)) {
        if (Double.isNaN(z)) {
          return AFFECT_TRANSLATION_X_ONLY;
        } else {
          return AFFECT_TRANSLATION_XZ_ONLY;
        }
      } else {
        if (Double.isNaN(z)) {
          return AFFECT_TRANSLATION_XY_ONLY;
        } else {
          return AFFECT_TRANSLATION_ONLY;
        }
      }
    }
  }
}
