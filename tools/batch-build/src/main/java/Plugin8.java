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

/**
 * @author Dennis Cosgrove
 */
public class Plugin8 extends Plugin {
	public Plugin8( Config config, java.io.File repoRoot ) {
		super( config, repoRoot, 8 );
		this.suiteDir = new java.io.File( repoRoot, "alice/netbeans/8/Alice3ModuleSuite" );
		assert this.suiteDir.exists() : this.suiteDir;
		assert this.suiteDir.isDirectory() : this.suiteDir;

		this.projectTemplateDir = new java.io.File( repoRoot, "alice/netbeans/8/ProjectTemplate" );
		assert this.projectTemplateDir.exists() : this.projectTemplateDir;
		assert this.projectTemplateDir.isDirectory() : this.projectTemplateDir;

		this.dstManifestFile = new java.io.File( this.getSuiteDir(), "Alice3Module/manifest.mf" );
		this.dstLibraryXmlFile = new java.io.File( this.getSuiteDir(), "Alice3Module/src/org/alice/netbeans/Alice3Library.xml" );
		this.dstProjectXmlFile = new java.io.File( this.getSuiteDir(), "Alice3Module/nbproject/project.xml" );

		java.io.InputStream manifestInputStream = Build.class.getResourceAsStream( "NetBeans8Plugin/manifest.mf" );
		assert manifestInputStream != null;
		this.manifestText = PluginCommon.substituteVersionTexts( config, edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( manifestInputStream ) );
		assert this.manifestText != null;
		assert this.manifestText.length() > 0;

		java.io.InputStream libraryXmlInputStream = Build.class.getResourceAsStream( "NetBeans8Plugin/Alice3Library.xml" );
		assert libraryXmlInputStream != null;
		this.libraryXmlText = PluginCommon.substituteVersionTexts( config, edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( libraryXmlInputStream ) );
		assert this.libraryXmlText != null;
		assert this.libraryXmlText.length() > 0;

		java.io.InputStream projectXmlInputStream = Build.class.getResourceAsStream( "NetBeans8Plugin/project.xml" );
		assert projectXmlInputStream != null;
		this.projectXmlText = PluginCommon.substituteVersionTexts( config, edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( projectXmlInputStream ) );
		assert this.projectXmlText != null;
		assert this.projectXmlText.length() > 0;
	}

	@Override
	protected java.io.File getSuiteDir() {
		return this.suiteDir;
	}

	@Override
	protected java.io.File getJarsDir() {
		return new java.io.File( this.suiteDir, "Alice3Module/release/modules/ext" );
	}

	@Override
	protected java.io.File getDistributionDir() {
		return new java.io.File( this.suiteDir, "Alice3Module/release/src/aliceSource.jar_root" );
	}

	@Override
	protected java.io.File getJavaDocZipFile() {
		return new java.io.File( this.getSuiteDir(), "Alice3Module/release/doc/aliceDocs.zip" );
	}

	@Override
	protected java.io.File getSrcZipFile() {
		return new java.io.File( this.getSuiteDir(), "Alice3Module/release/src/aliceSource.jar" );
	}

	private java.io.File getProjectTemplateDir() {
		return this.projectTemplateDir;
	}

	@Override
	protected java.io.File getJdkToUseForNbmAntCommand() {
		return JdkUtils.getJdk8HomeDir();
	}

	@Override
	protected java.io.File getNbmFile() {
		return new java.io.File( this.getSuiteDir(), "build/updates/org-alice-netbeans.nbm" );
	}

	@Override
	public void prepareFiles() throws java.io.IOException {
		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( this.dstManifestFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( this.dstManifestFile, this.manifestText );
		assert this.dstManifestFile.exists() : this.dstManifestFile;

		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( this.dstLibraryXmlFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( this.dstLibraryXmlFile, this.libraryXmlText );
		assert this.dstLibraryXmlFile.exists() : this.dstLibraryXmlFile;

		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( this.dstProjectXmlFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( this.dstProjectXmlFile, this.projectXmlText );
		assert this.dstProjectXmlFile.exists() : this.dstProjectXmlFile;

		java.io.File projectZip = new java.io.File( this.getSuiteDir(), "Alice3Module/src/org/alice/netbeans/ProjectTemplate.zip" );
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.zip( this.getProjectTemplateDir(), projectZip );
		assert projectZip.exists() : projectZip;

		java.io.File userPropertiesFile = NetBeans8Utils.getUserPropertiesFile();
		java.io.File platformPrivatePropertiesFile = new java.io.File( this.getSuiteDir(), "nbproject/private/platform-private.properties" );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( platformPrivatePropertiesFile, "user.properties.file=" + userPropertiesFile.getAbsolutePath().replaceAll( "\\\\", "\\\\\\\\" ) );
	}

	private final java.io.File suiteDir;
	private final java.io.File projectTemplateDir;

	private final String manifestText;
	private final String libraryXmlText;
	private final String projectXmlText;
	private final java.io.File dstManifestFile;
	private final java.io.File dstLibraryXmlFile;
	private final java.io.File dstProjectXmlFile;
}
