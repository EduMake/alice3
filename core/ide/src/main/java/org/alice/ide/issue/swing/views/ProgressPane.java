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

package org.alice.ide.issue.swing.views;

import edu.cmu.cs.dennisc.issue.IssueReportWorker;
import edu.cmu.cs.dennisc.issue.ReportGenerator;
import edu.cmu.cs.dennisc.issue.ReportSubmissionConfiguration;
import edu.cmu.cs.dennisc.issue.WorkerListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.net.URL;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class ProgressPane extends JPanel {
  private JTextPane console = new JTextPane();
  private IssueReportWorker issueReportWorker;
  private boolean isDone = false;
  private boolean isSuccessful = false;
  private URL urlResult = null;

  public ProgressPane() {
    this.console.setPreferredSize(new Dimension(400, 240));
    this.setLayout(new BorderLayout());
    this.add(new JScrollPane(this.console), BorderLayout.CENTER);
  }

  public void initializeAndExecuteWorker(ReportGenerator issueReportGenerator, ReportSubmissionConfiguration reportSubmissionConfiguration) {
    this.issueReportWorker = new IssueReportWorker(new WorkerListener() {
      @Override
      public void process(List<String> chunks) {
        handleProcess(chunks);
      }

      @Override
      public void done(boolean isSuccessful, URL urlResult) {
        handleDone(isSuccessful, urlResult);
      }
    }, issueReportGenerator, reportSubmissionConfiguration);
    this.issueReportWorker.execute();
  }

  private void hideRoot() {
    Component root = SwingUtilities.getRoot(this);
    if (root != null) {
      root.setVisible(false);
    }
  }

  public void handleProcess(List<String> chunks) {
    for (String chunk : chunks) {
      Document document = ProgressPane.this.console.getDocument();
      try {
        document.insertString(document.getLength(), chunk, null);
      } catch (BadLocationException ble) {
        throw new RuntimeException(ble);
      }
      System.out.print(chunk);
    }
  }

  public void handleDone(boolean isSuccessful, URL urlResult) {
    this.isDone = true;
    this.isSuccessful = isSuccessful;
    this.urlResult = urlResult;
    this.hideRoot();
  }

  public boolean isDone() {
    return this.isDone;
  }

  public boolean isSuccessful() {
    return this.isSuccessful;
  }

  public URL getURLResult() {
    return this.urlResult;
  }
}
