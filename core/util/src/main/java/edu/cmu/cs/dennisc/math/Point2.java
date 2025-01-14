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
package edu.cmu.cs.dennisc.math;

/**
 * @author Dennis Cosgrove
 */
public final class Point2 extends Tuple2 {
  public Point2() {
  }

  public Point2(Tuple2 other) {
    super(other);
  }

  public Point2(double x, double y) {
    super(x, y);
  }

  public static Point2 createZero() {
    return (Point2) setReturnValueToZero(new Point2());
  }

  public static Point2 createNaN() {
    return (Point2) setReturnValueToNaN(new Point2());
  }

  public static Point2 createAddition(Tuple2 a, Tuple2 b) {
    return (Point2) setReturnValueToAddition(new Point2(), a, b);
  }

  public static Point2 createSubtraction(Tuple2 a, Tuple2 b) {
    return (Point2) setReturnValueToSubtraction(new Point2(), a, b);
  }

  public static Point2 createNegation(Tuple2 a) {
    return (Point2) setReturnValueToNegation(new Point2(), a);
  }

  public static Point2 createMultiplication(Tuple2 a, Tuple2 b) {
    return (Point2) setReturnValueToMultiplication(new Point2(), a, b);
  }

  public static Point2 createMultiplication(Tuple2 a, double b) {
    return (Point2) setReturnValueToMultiplication(new Point2(), a, b);
  }

  public static Point2 createDivision(Tuple2 a, Tuple2 b) {
    return (Point2) setReturnValueToDivision(new Point2(), a, b);
  }

  public static Point2 createDivision(Tuple2 a, double b) {
    return (Point2) setReturnValueToDivision(new Point2(), a, b);
  }

  public static Point2 createInterpolation(Tuple2 a, Tuple2 b, double portion) {
    return (Point2) setReturnValueToInterpolation(new Point2(), a, b, portion);
  }

  public static Point2 createNormalized(Tuple2 a) {
    return (Point2) setReturnValueToNormalized(new Point2(), a);
  }

  public static double calculateDistanceSquaredBetween(Point2 a, Point2 b) {
    double xDelta = b.x - a.x;
    double yDelta = b.y - a.y;
    return (xDelta * xDelta) + (yDelta * yDelta);
  }

  public static double calculateDistanceBetween(Point2 a, Point2 b) {
    return Math.sqrt(calculateDistanceSquaredBetween(a, b));
  }
}
