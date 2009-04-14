/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.common;


/**
 * @author Dennis Cosgrove
 */
public class AssignmentExpressionPane extends swing.LineAxisPane  {
	private edu.cmu.cs.dennisc.alice.ast.AssignmentExpression assignmentExpression;
	public AssignmentExpressionPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.AssignmentExpression assignmentExpression ) {
		this.assignmentExpression = assignmentExpression;
		edu.cmu.cs.dennisc.alice.ast.Expression left = this.assignmentExpression.leftHandSide.getValue();
		
		edu.cmu.cs.dennisc.alice.ast.Expression expression;
		javax.swing.JComponent parent;
		if( left instanceof edu.cmu.cs.dennisc.alice.ast.ArrayAccess ) {
			edu.cmu.cs.dennisc.alice.ast.ArrayAccess arrayAccess = (edu.cmu.cs.dennisc.alice.ast.ArrayAccess)left;
			parent = new swing.LineAxisPane();
			this.add( parent );
			expression = arrayAccess.array.getValue();
		} else {
			parent = this;
			expression = left;
		}
		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
			edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = (edu.cmu.cs.dennisc.alice.ast.FieldAccess)expression;
			org.alice.ide.common.NodeNameLabel nameLabel = new org.alice.ide.common.NodeNameLabel( fieldAccess.field.getValue() );
			nameLabel.setFontToScaledFont( 1.5f );
			parent.add( factory.createExpressionPropertyPane( fieldAccess.expression, true, null ) );
			if( org.alice.ide.IDE.getSingleton().isJava() ) {
				parent.add( new zoot.ZLabel( " . " ) );
			}
			parent.add( nameLabel );
		} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.VariableAccess ) {
			edu.cmu.cs.dennisc.alice.ast.VariableAccess variableAccess = (edu.cmu.cs.dennisc.alice.ast.VariableAccess)expression;
			parent.add( new VariablePane( variableAccess.variable.getValue() ) );
		} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.ParameterAccess ) {
			edu.cmu.cs.dennisc.alice.ast.ParameterAccess parameterAccess = (edu.cmu.cs.dennisc.alice.ast.ParameterAccess)expression;
			parent.add( new ParameterPane( parameterAccess.parameter.getValue() ) );
		} else {
			parent.add( new zoot.ZLabel( "TODO" ) );
		}
		if( left instanceof edu.cmu.cs.dennisc.alice.ast.ArrayAccess ) {
			edu.cmu.cs.dennisc.alice.ast.ArrayAccess arrayAccess = (edu.cmu.cs.dennisc.alice.ast.ArrayAccess)left;
			parent.add( new zoot.ZLabel( "[ " ) );
			parent.add( factory.createExpressionPropertyPane( arrayAccess.index, true, null ) );
			parent.add( new zoot.ZLabel( " ]" ) );
		}
		if( "java".equals( org.alice.ide.IDE.getSingleton().getLocale().getVariant() ) ) {
			parent.add( new zoot.ZLabel( " = " ) );
		} else {
			parent.add( new org.alice.ide.common.GetsPane( true ) );
		}
		parent.add( factory.createExpressionPropertyPane( this.assignmentExpression.rightHandSide, true, null ) );
	}
}
