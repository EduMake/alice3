import java
import edu
import ecc

def createAndShowIDE():
	ide = ecc.dennisc.alice.ide.barebones.BarebonesIDE()
	ide.setSize( 1280, 1024 )
	import sys
	ide.handleArgs( sys.argv[1:] )
	ide.loadProjectFrom( file )
	ide.setVisible( True )
	
import javax
javax.swing.SwingUtilities.invokeLater( ecc.dennisc.lang.ApplyRunnable( createAndShowIDE ) )
