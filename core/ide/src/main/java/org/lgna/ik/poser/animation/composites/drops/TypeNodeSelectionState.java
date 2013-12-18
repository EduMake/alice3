/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.ik.poser.animation.composites.drops;

import java.util.List;
import java.util.Map;

import org.alice.stageide.type.croquet.TypeNode;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CustomItemStateWithInternalBlank;
import org.lgna.croquet.Group;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.imp.cascade.BlankNode;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.java.util.Maps;

/**
 * @author Matt May
 */
public class TypeNodeSelectionState extends CustomItemStateWithInternalBlank<TypeNode> {

	private TypeNode value;
	private final List<TypeNode> possibleStates;
	private final Map<TypeNode, Integer> nodeToIndentLevelMap = Maps.newHashMap();

	public TypeNodeSelectionState( Group group, TypeNode initialValue, TypeNode typeSelectionRoot ) {
		super( group, java.util.UUID.fromString( "2ed5d986-95a8-460a-8636-9af46071e966" ), initialValue, new ItemCodec<TypeNode>() {

			public Class<TypeNode> getValueClass() {
				return TypeNode.class;
			}

			public TypeNode decodeValue( BinaryDecoder binaryDecoder ) {
				throw new RuntimeException( "decode not yet supported" );
			}

			public void encodeValue( BinaryEncoder binaryEncoder, TypeNode value ) {
				throw new RuntimeException( "encode not yet supported" );
			}

			public void appendRepresentation( StringBuilder sb, TypeNode value ) {
				sb.append( "HELLO " + value.getType() );
			}
		} );
		this.possibleStates = getListFromRoot( typeSelectionRoot );
		this.initializeMap( typeSelectionRoot );
	}

	private void initializeMap( TypeNode root ) {
		nodeToIndentLevelMap.clear();
		for( TypeNode node : possibleStates ) {
			TypeNode crawler = node;
			int count = 0;
			while( crawler != root ) {
				++count;
				crawler = (TypeNode)crawler.getParent();
			}
			nodeToIndentLevelMap.put( node, count );
		}
	}

	private List<TypeNode> getListFromRoot( TypeNode root ) {
		List<TypeNode> rv = Collections.newArrayList();
		rv.add( root );
		for( int i = 0; i != root.getChildCount(); ++i ) {
			rv.addAll( getListFromRoot( (TypeNode)root.getChildAt( i ) ) );
		}
		return rv;
	}

	@Override
	protected void updateBlankChildren( List<CascadeBlankChild> rv, BlankNode<TypeNode> blankNode ) {
		for( TypeNode sphere : possibleStates ) {
			rv.add( TypeNodeFillIn.getInstance( sphere, nodeToIndentLevelMap.get( sphere ) ) );
		}
	}

	@Override
	protected TypeNode getSwingValue() {
		return this.value;
	}

	@Override
	protected void setSwingValue( TypeNode nextValue ) {
		this.value = nextValue;
	}

	public TypeNodeDropDown createDropDown() {
		return new TypeNodeDropDown( this );
	}

	public Map<TypeNode, Integer> getNodeToIndentLevelMap() {
		return this.nodeToIndentLevelMap;
	}

}
