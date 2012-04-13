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

/**
 * @author Dennis Cosgrove
 */
public class ProjectFileEditor extends org.lgna.croquet.simple.SimpleApplication {
	private static final String PROGRAM_TYPE_ENTRY_NAME = "programType.xml";
	private static final javax.swing.text.Highlighter.HighlightPainter HIGHLIGHT_PAINTER = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter( java.awt.Color.YELLOW.darker() );
	
	private final javax.swing.JEditorPane editor = new javax.swing.JEditorPane();
	private final javax.swing.JTextField find = new javax.swing.JTextField();

	private final javax.swing.event.DocumentListener documentListener = new javax.swing.event.DocumentListener() {
		public void changedUpdate( javax.swing.event.DocumentEvent e ) {
			ProjectFileEditor.this.handleUpdate( e );
		}
		public void insertUpdate( javax.swing.event.DocumentEvent e ) {
			ProjectFileEditor.this.handleUpdate( e );
		}
		public void removeUpdate( javax.swing.event.DocumentEvent e ) {
			ProjectFileEditor.this.handleUpdate( e );
		}
	};

	private java.util.zip.ZipFile zipFile;
	
	private void handleUpdate( javax.swing.event.DocumentEvent e ) {
		javax.swing.text.Highlighter highlighter = this.editor.getHighlighter();
		javax.swing.text.Highlighter.Highlight[] highlights = highlighter.getHighlights();
		for( javax.swing.text.Highlighter.Highlight highlight : highlights ) {
			if( highlight.getPainter() == HIGHLIGHT_PAINTER ) {
				highlighter.removeHighlight( highlight );
			}
		}
		javax.swing.text.Document findDocument = e.getDocument();
		javax.swing.text.Document sourceDocument = this.editor.getDocument();
		
		try {
			String findText = findDocument.getText( 0, findDocument.getLength() );
			String sourceText = sourceDocument.getText( 0, sourceDocument.getLength() );
			findText = findText.toLowerCase();
			sourceText = sourceText.toLowerCase();
			
			int firstIndex = -1;
			final int N = findText.length();
			if( N > 0 ) {
				int prevIndex = 0;
				while( true ) {
					int index = sourceText.indexOf( findText, prevIndex );
					if( index != -1 ) {
						prevIndex = index + N;
						highlighter.addHighlight( index, prevIndex, HIGHLIGHT_PAINTER );
						if( firstIndex == -1 ) {
							firstIndex = index;
							this.editor.setCaretPosition( firstIndex );
						}
					} else {
						break;
					}
				}
			}
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( e.toString(), ble );
		}
	}
	private void load( String path ) {
		try {
			this.zipFile = new java.util.zip.ZipFile( path );
			java.util.zip.ZipEntry entry = this.zipFile.getEntry( PROGRAM_TYPE_ENTRY_NAME );
			java.io.InputStream is = this.zipFile.getInputStream( entry );
			String text = edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( is );
			editor.setText( text );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( path, ioe );
		}
	}

	private void store() {
	}

	@Override
	protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
		super.handleWindowOpened( e );
		this.find.requestFocusInWindow();
	}
	@Override
	public void initialize( java.lang.String[] args ) {
		super.initialize( args );

		//this.editor.getDocument().putProperty(javax.swing.text.DefaultEditorKit.EndOfLineStringProperty, "\n");

		java.awt.Container contentPanel = this.getFrame().getContentPanel().getAwtComponent();
		contentPanel.add( this.find, java.awt.BorderLayout.PAGE_END );
		contentPanel.add( new javax.swing.JScrollPane( this.editor ), java.awt.BorderLayout.CENTER );
		this.find.getDocument().addDocumentListener( this.documentListener );
		
		System.err.println( this.editor.getHighlighter() );
		
		java.awt.Font font = new java.awt.Font( "Monospaced", 0, 18 );
		this.editor.setFont( font );
		this.find.setFont( font );
		
		String path;
		if( args.length > 0 ) {
			path = args[ 0 ];
		} else {
			java.io.File directory = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "Alice3/MyProjects" );
			String filename = null;
			String extension = org.lgna.project.io.IoUtilities.PROJECT_EXTENSION;
			boolean isSharingDesired = false;
			java.io.File file = edu.cmu.cs.dennisc.java.awt.FileDialogUtilities.showOpenFileDialog( contentPanel, directory, filename, extension, isSharingDesired );
			path = file.getAbsolutePath();
		}
		this.load( path );
	}
	public static void main( String[] args ) {
		ProjectFileEditor app = new ProjectFileEditor();
		app.initialize( args );
		app.getFrame().setSize( 800, 1200 );
		app.getFrame().setVisible( true );
	}
}
