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
package org.alice.stageide.modelresource;

/**
 * @author Dennis Cosgrove
 */
public final class ClassResourceKey extends ResourceKey {
	private final Class<? extends org.lgna.story.resources.ModelResource> cls;

	public ClassResourceKey( Class<? extends org.lgna.story.resources.ModelResource> cls ) {
		this.cls = cls;
	}

	public Class<? extends org.lgna.story.resources.ModelResource> getCls() {
		return this.cls;
	}

	public org.lgna.project.ast.JavaType getType() {
		return org.lgna.project.ast.JavaType.getInstance( this.cls );
	}

	@Override
	public String getText() {
		return cls.getSimpleName().replace( "Resource", "" );
	}

	@Override
	public org.lgna.croquet.icon.IconFactory getIconFactory() {
		if( this.isLeaf() ) {
			return org.alice.stageide.icons.IconFactoryManager.getIconFactoryForResourceInstance( cls.getEnumConstants()[ 0 ] );
		} else {
			return org.alice.stageide.icons.IconFactoryManager.getIconFactoryForResourceCls( cls );
		}
	}

	@Override
	public org.lgna.project.ast.InstanceCreation createInstanceCreation() {
		throw new Error();
	}

	@Override
	public boolean isLeaf() {
		return this.cls.isEnum() && ( this.cls.getEnumConstants().length == 1 );
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) {
			return true;
		}
		if( o instanceof ClassResourceKey ) {
			ClassResourceKey other = (ClassResourceKey)o;
			return this.cls.equals( other.cls );
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.cls.hashCode();
	}

	@Override
	protected void appendRep( StringBuilder sb ) {
		sb.append( this.cls.getSimpleName() );
	}
}
