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
package org.alice.media.youtube.croquet.views;

/**
 * @author Matt May
 */
public class UploadView extends org.lgna.croquet.components.BorderPanel {
	private final org.lgna.croquet.components.Panel youtubeDetailsPanel;

	public UploadView( org.alice.media.youtube.croquet.UploadComposite composite ) {
		super( composite, 24, 0 );

		org.lgna.croquet.components.MigPanel loginPanel = new org.lgna.croquet.components.MigPanel( null, "fill, inset 0", "", "[]0[]4[]0[]4[]" );
		loginPanel.addComponent( composite.getLoginComposite().getView(), "wrap" );

		org.lgna.croquet.components.JComponent<?> titleSidekickLabel = composite.getTitleState().getSidekickLabel().createImmutableTextField();
		org.lgna.croquet.components.TextField titleTextField = composite.getTitleState().createTextField();

		org.lgna.croquet.components.JComponent<?> descriptionSidekickLabel = composite.getDescriptionState().getSidekickLabel().createImmutableTextField();
		org.lgna.croquet.components.TextArea descriptionTextArea = composite.getDescriptionState().createTextArea();
		descriptionTextArea.getAwtComponent().setRows( 4 );
		//description.getAwtComponent().setLineWrap( true );
		org.lgna.croquet.components.ScrollPane descriptionScrollPane = new org.lgna.croquet.components.ScrollPane( descriptionTextArea );

		org.lgna.croquet.components.JComponent<?> tagsSidekickLabel = composite.getTagsState().getSidekickLabel().createImmutableTextField();
		org.lgna.croquet.components.TextArea tagsTextArea = composite.getTagsState().createTextArea();
		tagsTextArea.getAwtComponent().setRows( 2 );
		//tags.getAwtComponent().setLineWrap( true );
		org.lgna.croquet.components.ScrollPane tagsScrollPane = new org.lgna.croquet.components.ScrollPane( tagsTextArea );

		//		org.lgna.croquet.components.JComponent<?> categoryLabel = composite.getVideoCategoryState().getSidekickLabel().createImmutableTextField();
		//		org.lgna.croquet.components.ComboBox<String> categoryComboBox = composite.getVideoCategoryState().getPrepModel().createComboBox();

		org.lgna.croquet.components.CheckBox isPrivateCheckBox = composite.getIsPrivateState().createCheckBox();

		final boolean IS_MIG_PANEL_WORKING_WITH_TEXT_AREAS = true;
		if( IS_MIG_PANEL_WORKING_WITH_TEXT_AREAS ) {
			org.lgna.croquet.components.MigPanel migPanel = new org.lgna.croquet.components.MigPanel( null, "insets 0, fill" );
			migPanel.addComponent( titleSidekickLabel, "wrap" );
			migPanel.addComponent( titleTextField, "wrap, growx" );
			migPanel.addComponent( descriptionSidekickLabel, "wrap" );
			migPanel.addComponent( descriptionScrollPane, "wrap, grow" );
			migPanel.addComponent( tagsSidekickLabel, "wrap" );
			migPanel.addComponent( tagsScrollPane, "wrap, grow" );
			//			migPanel.addComponent( categoryLabel, "wrap" );
			//			migPanel.addComponent( categoryComboBox, "wrap, growx" );
			migPanel.addComponent( isPrivateCheckBox, "wrap" );
			this.youtubeDetailsPanel = migPanel;
		} else {
			org.lgna.croquet.components.GridBagPanel gridBagPanel = new org.lgna.croquet.components.GridBagPanel();

			final int TOP_INSET = 2;
			java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
			gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
			gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gbc.fill = java.awt.GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;

			gbc.insets.top = TOP_INSET;
			gridBagPanel.addComponent( titleSidekickLabel, gbc );
			gbc.insets.top = 0;

			gridBagPanel.addComponent( titleTextField, gbc );

			gbc.insets.top = TOP_INSET;
			gridBagPanel.addComponent( descriptionSidekickLabel, gbc );
			gbc.insets.top = 0;

			gbc.weighty = 1.0;
			descriptionTextArea.setMaximumSizeClampedToPreferredSize( false );

			gridBagPanel.addComponent( descriptionScrollPane, gbc );
			gbc.weighty = 0.0;

			gbc.insets.top = TOP_INSET;
			gridBagPanel.addComponent( tagsSidekickLabel, gbc );
			gbc.insets.top = 0;

			gbc.weighty = 1.0;
			gridBagPanel.addComponent( tagsScrollPane, gbc );

			gbc.weighty = 0.0;

			gbc.insets.top = TOP_INSET;
			//			gridBagPanel.addComponent( categoryLabel, gbc );
			//			gbc.insets.top = 0;
			//
			//			gridBagPanel.addComponent( categoryComboBox, gbc );

			gbc.insets.top = TOP_INSET;
			gridBagPanel.addComponent( isPrivateCheckBox, gbc );
			gbc.insets.top = 0;
			this.youtubeDetailsPanel = gridBagPanel;
		}

		this.addCenterComponent( new org.lgna.croquet.components.BorderPanel.Builder().pageStart( loginPanel ).center( this.youtubeDetailsPanel ).build() );

		org.lgna.croquet.components.MigPanel lineStartPanel = new org.lgna.croquet.components.MigPanel( null, "insets 0, fill", "", "" );
		lineStartPanel.addComponent( new org.lgna.croquet.components.Label( "Preview:" ), "wrap" );
		lineStartPanel.addComponent( composite.getVideoComposite().getView(), "wrap" );
		lineStartPanel.addComponent( composite.getExportToFileOperation().createButton(), "wrap" );
		lineStartPanel.addComponent( new org.lgna.croquet.components.Label(), "push" );

		this.addLineStartComponent( lineStartPanel );
	}

	public org.lgna.croquet.components.Panel getYoutubeDetailsPanel() {
		return this.youtubeDetailsPanel;
	}
}
