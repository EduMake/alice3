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
package org.alice.stageide.gallerybrowser.uri;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.ast.type.croquet.ImportTypeWizard;
import org.alice.ide.ast.type.merge.core.MergeUtilities;
import org.alice.nonfree.NebulousIde;
import org.alice.stageide.ast.declaration.AddResourceKeyManagedFieldComposite;
import org.alice.stageide.modelresource.ClassResourceKey;
import org.alice.stageide.modelresource.EnumConstantResourceKey;
import org.alice.stageide.modelresource.ResourceKey;
import org.lgna.common.Resource;
import org.lgna.croquet.Application;
import org.lgna.croquet.Operation;
import org.lgna.croquet.SingleThreadIteratingOperation;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.io.IoUtilities;
import org.lgna.project.io.TypeResourcesPair;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class ResourceKeyUriIteratingOperation extends SingleThreadIteratingOperation {
	public static ResourceKeyUriIteratingOperation getInstance( ResourceKey resourceKey, Class<?> thingCls, URI uri ) {
		ResourceKeyUriIteratingOperation rv;
		if( resourceKey != null ) {
			if( resourceKey instanceof ClassResourceKey ) {
				//				org.alice.stageide.modelresource.ClassResourceKey classResourceKey = (org.alice.stageide.modelresource.ClassResourceKey)resourceKey;
				rv = ClassResourceKeyUriIteratingOperation.getInstance();
			} else if( resourceKey instanceof EnumConstantResourceKey ) {
				rv = EnumConstantResourceKeyUriIteratingOperation.getInstance();
			} else if( NebulousIde.nonfree.isInstanceOfPersonResourceKey( resourceKey ) ) {
				rv = NebulousIde.nonfree.getPersonResourceKeyUriIteratingOperation();
			} else {
				rv = null;
			}
		} else {
			rv = ThingClsUriIteratingOperation.getInstance();
		}
		if( rv != null ) {
			rv.setResourceKeyThingClsAndUri( resourceKey, thingCls, uri );
		}
		return rv;
	}

	public ResourceKeyUriIteratingOperation( UUID migrationId ) {
		super( Application.PROJECT_GROUP, migrationId );
	}

	public ResourceKey getResourceKey() {
		return this.resourceKey;
	}

	public Class<?> getThingCls() {
		return this.thingCls;
	}

	public URI getUri() {
		return this.uri;
	}

	private void setResourceKeyThingClsAndUri( ResourceKey resourceKey, Class<?> thingCls, URI uri ) {
		this.resourceKey = resourceKey;
		this.thingCls = thingCls;
		this.uri = uri;
	}

	protected abstract int getStepCount();

	@Override
	protected boolean hasNext( List<UserActivity> subSteps, Iterator<Triggerable> iteratingData ) {
		return subSteps.size() < this.getStepCount();
	}

	protected Operation getAddResourceKeyManagedFieldCompositeOperation( EnumConstantResourceKey enumConstantResourceKey ) {
		AddResourceKeyManagedFieldComposite addResourceKeyManagedFieldComposite = AddResourceKeyManagedFieldComposite.getInstance();
		addResourceKeyManagedFieldComposite.setResourceKeyToBeUsedByGetInitializerInitialValue( enumConstantResourceKey, false );
		return addResourceKeyManagedFieldComposite.getLaunchOperation();
	}

	protected Operation getMergeTypeOperation() {
		TypeResourcesPair typeResourcesPair;
		try {
			typeResourcesPair = IoUtilities.readType( new File( this.uri ) );
		} catch( IOException ioe ) {
			typeResourcesPair = null;
			Logger.throwable( ioe, this.uri );
		} catch( VersionNotSupportedException vnse ) {
			typeResourcesPair = null;
			Logger.throwable( vnse, this.uri );
		}
		if( typeResourcesPair != null ) {
			NamedUserType importedRootType = typeResourcesPair.getType();
			Set<Resource> importedResources = typeResourcesPair.getResources();
			NamedUserType srcType = importedRootType;
			NamedUserType dstType = MergeUtilities.findMatchingTypeInExistingTypes( srcType );
			ImportTypeWizard wizard = new ImportTypeWizard( this.uri, importedRootType, importedResources, srcType, dstType );
			return wizard.getLaunchOperation();
		} else {
			return null;
		}
	}

	@Override
	protected void handleSuccessfulCompletionOfSubModels( UserActivity activity, List<UserActivity> subSteps ) {
		super.handleSuccessfulCompletionOfSubModels( activity, subSteps );
		this.resourceKey = null;
	}

	protected ResourceKey resourceKey;
	protected Class<?> thingCls;
	private URI uri;
}
