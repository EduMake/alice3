/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.ide.croquet.models.project;

import java.util.Locale;

import org.alice.ide.ProjectApplication;
import org.lgna.croquet.InformationDialogOperation;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.TabComposite;
import org.lgna.croquet.TabSelectionState;
import org.lgna.croquet.components.Container;
import org.lgna.croquet.components.Dialog;
import org.lgna.croquet.history.CompletionStep;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;

/**
 * @author Matt May
 */
public class StatisticsOperation extends InformationDialogOperation {

	public StatisticsOperation() {
		super( java.util.UUID.fromString( "d17d2d7c-ecae-4869-98e6-cc2d4c2fe517" ) );
	}

	private static class SingletonHolder {
		private static StatisticsOperation instance = new StatisticsOperation();

	}

	public static StatisticsOperation getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	protected Container<?> createContentPane( CompletionStep<?> step, Dialog dialog ) {
		FlowControlFrequency flowControlFrequencyTab = new FlowControlFrequency();
		MethodFrequencyTab methodTab = new MethodFrequencyTab();
		TabSelectionState<TabComposite<?>> state = new TabSelectionState<TabComposite<?>>( ProjectApplication.UI_STATE_GROUP, java.util.UUID.fromString( "6f6d1d21-dcd3-4c79-a2f8-7b9b7677f64d" ), new ItemCodec<TabComposite<?>>() {

			public Class<TabComposite<?>> getValueClass() {
				return null;
			}

			public TabComposite<?> decodeValue( BinaryDecoder binaryDecoder ) {
				return null;
			}

			public void encodeValue( BinaryEncoder binaryEncoder, TabComposite<?> value ) {
			}

			public StringBuilder appendRepresentation( StringBuilder rv, TabComposite<?> value, Locale locale ) {
				return null;
			}
		} );
		state.addItem( methodTab );
		state.addItem( flowControlFrequencyTab );
		return state.createFolderTabbedPane();
	}
	@Override
	protected void releaseContentPane( CompletionStep<?> step, Dialog dialog, Container<?> contentPane ) {
	}
}
