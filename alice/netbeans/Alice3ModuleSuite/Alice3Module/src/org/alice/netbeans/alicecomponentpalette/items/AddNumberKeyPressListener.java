package org.alice.netbeans.alicecomponentpalette.items;

public class AddNumberKeyPressListener extends AbstractActiveEditorDrop {
	@Override
	protected String[] getImports() {
		return new String[] {
			"org.lgna.story.event.NumberKeyPressListener",
			"org.lgna.story.event.NumberKeyEvent"
		};
	}
}
