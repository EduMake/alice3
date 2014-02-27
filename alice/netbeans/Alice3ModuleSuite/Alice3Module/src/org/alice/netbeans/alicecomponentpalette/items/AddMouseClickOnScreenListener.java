package org.alice.netbeans.alicecomponentpalette.items;

/**
 * @author Dennis Cosgrove
 */
public class AddMouseClickOnScreenListener extends AbstractActiveEditorDrop {
	@Override
	protected String[] getImports() {
		return new String[] {
			"org.lgna.story.event.MouseClickOnScreenListener",
			"org.lgna.story.event.MouseClickOnScreenEvent"
		};
	}
}
