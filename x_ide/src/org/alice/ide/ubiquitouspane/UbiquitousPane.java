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
package org.alice.ide.ubiquitouspane;

import org.alice.ide.ubiquitouspane.templates.*;

/**
 * @author Dennis Cosgrove
 */
public class UbiquitousPane extends swing.LineAxisPane {
	private DoInOrderTemplate doInOrderTemplate = new DoInOrderTemplate();
	private CountLoopTemplate countLoopTemplate = new CountLoopTemplate();
	private WhileLoopTemplate whileLoopTemplate = new WhileLoopTemplate();
	private ForEachInArrayLoopTemplate forEachInArrayLoopTemplate = new ForEachInArrayLoopTemplate();
	private ConditionalStatementTemplate conditionalStatementTemplate = new ConditionalStatementTemplate();
	private DoTogetherTemplate doTogetherTemplate = new DoTogetherTemplate();
	private DeclareLocalTemplate declareLocalTemplate = new DeclareLocalTemplate();
	public UbiquitousPane() {
		this.add( this.doInOrderTemplate );
		this.add( this.countLoopTemplate );
		this.add( this.whileLoopTemplate );
		this.add( this.forEachInArrayLoopTemplate );
		this.add( this.conditionalStatementTemplate );
		this.add( this.doTogetherTemplate );
		this.add( this.declareLocalTemplate );
	}
}
