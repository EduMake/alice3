/*
 * Alice 3 End User License Agreement
 *
 * Copyright (c) 2006-2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may "Alice" appear in their name, without prior written permission of Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must display the following acknowledgement: "This product includes software developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is contributed by Electronic Arts Inc. and may be used for personal, non-commercial, and academic use only. Redistributions of any program source code that utilizes The Sims 2 Assets must also retain the copyright notice, list of conditions and the disclaimer contained in The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.lgna.story.resources.prop;

import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.SJointedModel;
import org.lgna.story.implementation.BasicJointedModelImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.PropResource;

public enum HauntedHouseResource implements PropResource {
  DEFAULT;

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId ROOT = new JointId(null, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId RIGHT_HINGE = new JointId(ROOT, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId RIGHT_DOOR_FRONT = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId RIGHT_SHUTTER_FRONT_BOTTOM = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId RIGHT_SHUTTER_RIGHT_SIDE_FIRST = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LEFT_SHUTTER_RIGHT_SIDE_FIRST = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LEFT_SHUTTER_RIGHT_SIDE_SECOND = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId RIGHT_SHUTTER_RIGHT_SIDE_SECOND = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId RIGHT_SHUTTER_FRONT_TOP = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId RIGHT_DOOR_BACK = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Y_00 = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Y_01 = new JointId(STAIRS_Y_00, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Y_02 = new JointId(STAIRS_Y_01, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Y_03 = new JointId(STAIRS_Y_02, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Y_04 = new JointId(STAIRS_Y_03, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Y_05 = new JointId(STAIRS_Y_04, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Y_06 = new JointId(STAIRS_Y_05, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Y_07 = new JointId(STAIRS_Y_06, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Y_08 = new JointId(STAIRS_Y_07, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Y_09 = new JointId(STAIRS_Y_08, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Y_10 = new JointId(STAIRS_Y_09, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_X_0 = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_X_1 = new JointId(STAIRS_X_0, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_X_2 = new JointId(STAIRS_X_1, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_X_3 = new JointId(STAIRS_X_2, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_X_4 = new JointId(STAIRS_X_3, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_W_0 = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_W_1 = new JointId(STAIRS_W_0, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_W_2 = new JointId(STAIRS_W_1, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_W_3 = new JointId(STAIRS_W_2, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_W_4 = new JointId(STAIRS_W_3, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId MARKER_TOP_W = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId MARKER_BASE_W = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId MARKER_BASE_X = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId MARKER_TOP_X = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId MARKER_BASE_Y = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId MARKER_TOP_Y = new JointId(RIGHT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LEFT_HINGE = new JointId(ROOT, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LEFT_DOOR_FRONT = new JointId(LEFT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LEFT_SHUTTER_FRONT_BOTTOM = new JointId(LEFT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LEFT_SHUTTER_FRONT_TOP = new JointId(LEFT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId RIGHT_SHUTTER_LEFT_SIDE_FIRST = new JointId(LEFT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LEFT_SHUTTER_LEFT_SIDE_FIRST = new JointId(LEFT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId RIGHT_SHUTTER_LEFT_SIDE_SECOND = new JointId(LEFT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LEFT_SHUTTER_LEFT_SIDE_SECOND = new JointId(LEFT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId MARKER_TOP_Z = new JointId(LEFT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId MARKER_BASE_Z = new JointId(LEFT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LEFT_DOOR_BACK = new JointId(LEFT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Z_00 = new JointId(LEFT_HINGE, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Z_01 = new JointId(STAIRS_Z_00, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Z_02 = new JointId(STAIRS_Z_01, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Z_03 = new JointId(STAIRS_Z_02, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Z_04 = new JointId(STAIRS_Z_03, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Z_05 = new JointId(STAIRS_Z_04, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Z_06 = new JointId(STAIRS_Z_05, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Z_07 = new JointId(STAIRS_Z_06, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Z_08 = new JointId(STAIRS_Z_07, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Z_09 = new JointId(STAIRS_Z_08, HauntedHouseResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId STAIRS_Z_10 = new JointId(STAIRS_Z_09, HauntedHouseResource.class);

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId[] JOINT_ID_ROOTS = {ROOT};

  public static final JointId[] STAIRS_W_ARRAY = {STAIRS_W_0, STAIRS_W_1, STAIRS_W_2, STAIRS_W_3, STAIRS_W_4};

  public static final JointId[] STAIRS_X_ARRAY = {STAIRS_X_0, STAIRS_X_1, STAIRS_X_2, STAIRS_X_3, STAIRS_X_4};

  public static final JointId[] STAIRS_Y_ARRAY = {STAIRS_Y_00, STAIRS_Y_01, STAIRS_Y_02, STAIRS_Y_03, STAIRS_Y_04, STAIRS_Y_05, STAIRS_Y_06, STAIRS_Y_07, STAIRS_Y_08, STAIRS_Y_09, STAIRS_Y_10};

  public static final JointId[] STAIRS_Z_ARRAY = {STAIRS_Z_00, STAIRS_Z_01, STAIRS_Z_02, STAIRS_Z_03, STAIRS_Z_04, STAIRS_Z_05, STAIRS_Z_06, STAIRS_Z_07, STAIRS_Z_08, STAIRS_Z_09, STAIRS_Z_10};

  private final ImplementationAndVisualType resourceType;

  HauntedHouseResource() {
    this(ImplementationAndVisualType.ALICE);
  }

  HauntedHouseResource(ImplementationAndVisualType resourceType) {
    this.resourceType = resourceType;
  }

  @Override
  public JointId[] getRootJointIds() {
    return HauntedHouseResource.JOINT_ID_ROOTS;
  }

  @Override
  public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
    return this.resourceType.getFactory(this);
  }

  @Override
  public BasicJointedModelImp createImplementation(SJointedModel abstraction) {
    return new BasicJointedModelImp(abstraction, this.resourceType.getFactory(this));
  }
}
