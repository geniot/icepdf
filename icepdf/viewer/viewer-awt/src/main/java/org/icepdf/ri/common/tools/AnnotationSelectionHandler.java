/*
 * Copyright 2006-2017 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icepdf.ri.common.tools;

import org.icepdf.ri.common.utility.annotation.destinations.NameTreeEditDialog;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ResourceBundle;

/**
 * The AnnotationSelectionHandler is responsible for deselecting all annotations
 * when the a mouse click event has been fired.
 *
 * @since 5.0
 */
public class AnnotationSelectionHandler extends CommonToolHandler
        implements ToolHandler, ActionListener {

    protected DocumentViewController documentViewController;

    protected AbstractPageViewComponent pageViewComponent;

    private JPopupMenu contextMenu;
    private JMenuItem addMenuItem;
    private int x, y;

    public AnnotationSelectionHandler(DocumentViewController documentViewController,
                                      AbstractPageViewComponent pageViewComponent) {
        super(documentViewController, pageViewComponent);
        this.documentViewController = documentViewController;
        this.pageViewComponent = pageViewComponent;

        ResourceBundle messageBundle = documentViewController.getParentController().getMessageBundle();
        contextMenu = new JPopupMenu();
        addMenuItem = new JMenuItem(messageBundle.getString(
                "viewer.utilityPane.destinations.view.selectionTool.contextMenu.add.label"));
        addMenuItem.addActionListener(this);
        contextMenu.add(addMenuItem);
    }

    public void mouseClicked(MouseEvent e) {
        documentViewController.clearSelectedAnnotations();
        if (pageViewComponent != null) {
            pageViewComponent.requestFocus();
        }
        if (pageViewComponent != null &&
                e.getButton() == MouseEvent.BUTTON3) {
            x = e.getX();
            y = e.getY();
            contextMenu.show(e.getComponent(), x, y);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addMenuItem) {
            // create popup for adding a new destination.
            AffineTransform pageTransform = getPageTransformInverse();
            AffineTransform pageInverseTransform = getPageTransform();

            // convert bbox and start and end line points.
            Rectangle bBox = new Rectangle(x, y, 1, 1);
            Rectangle tBbox = convertToPageSpace(bBox).getBounds();
            new NameTreeEditDialog(documentViewController.getParentController(),
                    pageViewComponent.getPage(), tBbox.x, tBbox.y).setVisible(true);
        }
    }

    public void paintTool(Graphics g) {
        // nothing to paint
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    protected void checkAndApplyPreferences() {

    }


    public void installTool() {

    }

    public void uninstallTool() {

    }
}
