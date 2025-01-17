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

import edu.cmu.cs.dennisc.java.util.BufferUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AbstractMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.BooleanProperty;
import edu.cmu.cs.dennisc.property.DoubleBufferProperty;
import edu.cmu.cs.dennisc.property.FloatBufferProperty;
import edu.cmu.cs.dennisc.property.IntBufferProperty;
import edu.cmu.cs.dennisc.property.IntegerProperty;
import edu.cmu.cs.dennisc.scenegraph.bound.BoundUtilities;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

public class Mesh extends Geometry {

  public Mesh() {
    super();
  }

  public Mesh(Mesh m) {
    super(m);
    if (m != null) {
      useAlphaTest.setValue(m.useAlphaTest.getValue());
      textureId.setValue(m.textureId.getValue());
      cullBackfaces.setValue(m.cullBackfaces.getValue());
      indexBuffer.setValue(BufferUtilities.copyIntBuffer(m.indexBuffer.getValue()));
      normalBuffer.setValue(BufferUtilities.copyFloatBuffer(m.normalBuffer.getValue()));
      vertexBuffer.setValue(BufferUtilities.copyDoubleBuffer(m.vertexBuffer.getValue()));
      textCoordBuffer.setValue(BufferUtilities.copyFloatBuffer(m.textCoordBuffer.getValue()));
      setName(m.getName());
    }
  }

  @Override
  protected void updateBoundingBox(AxisAlignedBox boundingBox) {
    BoundUtilities.getBoundingBox(boundingBox, vertexBuffer.getValue());
  }

  @Override
  protected void updateBoundingSphere(edu.cmu.cs.dennisc.math.Sphere boundingSphere) {
    BoundUtilities.getBoundingSphere(boundingSphere, vertexBuffer.getValue().array());
  }

  @Override
  protected void updatePlane(Vector3 forward, Vector3 upGuide, Point3 translation) {

    double[] xyzs = vertexBuffer.getValue().array();
    float[] ijks = normalBuffer.getValue().array();

    assert xyzs.length >= 6;
    assert ijks.length >= 3;

    forward.set(ijks[0], ijks[1], ijks[2]);
    forward.normalize();
    forward.negate();

    translation.set(xyzs[0], xyzs[1], xyzs[2]);
    upGuide.set(translation.x - xyzs[3], translation.y - xyzs[4], translation.z - xyzs[5]);
    upGuide.normalize();

  }

  @Override
  public void transform(AbstractMatrix4x4 trans) {
    DoubleBuffer buffer = vertexBuffer.getValue();

    buffer.rewind();

    int n = buffer.remaining();

    Point3 p = Point3.createNaN();
    for (int i = 0; i < n; i += 3) {
      p.set(buffer.get(i), buffer.get(i + 1), buffer.get(i + 2));
      trans.transform(p);

      buffer.put(i, p.x);
      buffer.put(i + 1, p.y);
      buffer.put(i + 2, p.z);
    }

    vertexBuffer.setValue(buffer);
  }

  public Mesh createCopy() {
    return new Mesh(this);
  }

  public void scale(Vector3 scale) {
    DoubleBuffer buffer =  vertexBuffer.getValue();

    buffer.rewind();

    int n = buffer.remaining();

    for (int i = 0; i < n; i += 3) {
      buffer.put(i, buffer.get(i) * scale.x);
      buffer.put(i + 1, buffer.get(i + 1) * scale.y);
      buffer.put(i + 2, buffer.get(i + 2) * scale.z);
    }

    vertexBuffer.setValue(buffer);
  }

  public void invertNormals() {
    FloatBuffer buffer = normalBuffer.getValue();

    buffer.rewind();

    int n = buffer.remaining();

    for (int i = 0; i < n; i += 3) {
      buffer.put(i, -buffer.get(i));
      buffer.put(i + 1, -buffer.get(i + 1));
      buffer.put(i + 2, -buffer.get(i + 2));
    }
    normalBuffer.setValue(buffer);
  }

  public void invertIndices() {
    IntBuffer buffer = indexBuffer.getValue();

    buffer.rewind();

    int n = buffer.remaining();

    for (int i = 0; i < n; i += 3) {
      // Leave buffer[i] alone
      // Swap the values of i + 1 and i + 2
      int temp = buffer.get(i + 1);
      buffer.put(i + 1, buffer.get(i + 2));
      buffer.put(i + 2, temp);
    }
    indexBuffer.setValue(buffer);
  }

  public List<Integer> getReferencedTextureIds() {
    List<Integer> referencedTextureIds = new LinkedList<>();
    if (textureIdArray.isEmpty()) {
      referencedTextureIds.add(textureId.getValue());
    } else {
      for (Integer textureId : textureIdArray) {
        if (!referencedTextureIds.contains(textureId)) {
          referencedTextureIds.add(textureId);
        }
      }
    }
    return referencedTextureIds;
  }

  public final DoubleBufferProperty vertexBuffer = new DoubleBufferProperty(this, (DoubleBuffer) null) {
    @Override
    public void setValue(DoubleBuffer value) {
      Mesh.this.markBoundsDirty();
      super.setValue(value);
      Mesh.this.fireBoundChanged();
    }
  };
  public final FloatBufferProperty normalBuffer = new FloatBufferProperty(this, (FloatBuffer) null);
  public final FloatBufferProperty textCoordBuffer = new FloatBufferProperty(this, (FloatBuffer) null);
  public final IntBufferProperty indexBuffer = new IntBufferProperty(this, (IntBuffer) null);
  public final IntegerProperty textureId = new IntegerProperty(this, -1);
  //textureIdArray is currently only used for model exporting. Alice rendering does not support multiple textures per mesh
  public ArrayList<Integer> textureIdArray = new ArrayList<>(); //Similar to vertex buffer and normal buffer, this encodes the textureId for a given index
  public final BooleanProperty cullBackfaces = new BooleanProperty(this, Boolean.TRUE);
  public final BooleanProperty useAlphaTest = new BooleanProperty(this, Boolean.FALSE);

  public Integer getTextureId(int index) {
    if (textureIdArray.isEmpty()) {
      return textureId.getValue();
    }
    if (!textureIdArray.get(index).equals(textureIdArray.get(index + 1)) || !textureIdArray.get(index).equals(textureIdArray.get(index + 2))) {
      Logger.severe("Triangle material mapping isn't consistent: " + index + "=" + textureIdArray.get(index) + ", " + (index + 1) + "=" + textureIdArray.get(index + 1) + ", " + (index + 2) + "=" + textureIdArray.get(index + 2));
    }
    return textureIdArray.get(index);
  }
}
