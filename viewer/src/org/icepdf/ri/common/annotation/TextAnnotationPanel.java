/*
 * Copyright 2006-2012 ICEsoft Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icepdf.ri.common.annotation;

import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.annotations.AnnotationState;
import org.icepdf.core.pobjects.annotations.TextAnnotation;
import org.icepdf.core.views.AnnotationComponent;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AbstractDocumentViewModel;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;

/**
 * TextAnnotationPanel is a configuration panel for changing the properties
 * of a TextAnnotationComponent and the underlying annotation component.
 *
 * @since 5.0
 */
public class TextAnnotationPanel extends AnnotationPanelAdapter implements ItemListener,
        ActionListener {

    // default list values.
    private static final int DEFAULT_ICON_NAME = 0;

    // line thicknesses.
    private final ValueLabelItem[] TEXT_ICON_LIST = new ValueLabelItem[]{
            new ValueLabelItem(TextAnnotation.COMMENT_ICON, TextAnnotation.COMMENT_ICON.getName()),
            new ValueLabelItem(TextAnnotation.CHECK_ICON, TextAnnotation.CHECK_ICON.getName()),
            new ValueLabelItem(TextAnnotation.CHECK_MARK_ICON, TextAnnotation.CHECK_MARK_ICON.getName()),
            new ValueLabelItem(TextAnnotation.CIRCLE_ICON, TextAnnotation.CIRCLE_ICON.getName()),
            new ValueLabelItem(TextAnnotation.CROSS_ICON, TextAnnotation.CROSS_ICON.getName()),
            new ValueLabelItem(TextAnnotation.CROSS_HAIRS_ICON, TextAnnotation.CROSS_HAIRS_ICON.getName()),
            new ValueLabelItem(TextAnnotation.HELP_ICON, TextAnnotation.HELP_ICON.getName()),
            new ValueLabelItem(TextAnnotation.INSERT_ICON, TextAnnotation.INSERT_ICON.getName()),
            new ValueLabelItem(TextAnnotation.KEY_ICON, TextAnnotation.KEY_ICON.getName()),
            new ValueLabelItem(TextAnnotation.NEW_PARAGRAPH_ICON, TextAnnotation.NEW_PARAGRAPH_ICON.getName()),
            new ValueLabelItem(TextAnnotation.PARAGRAPH_ICON, TextAnnotation.PARAGRAPH_ICON.getName()),
            new ValueLabelItem(TextAnnotation.RIGHT_ARROW_ICON, TextAnnotation.RIGHT_ARROW_ICON.getName()),
            new ValueLabelItem(TextAnnotation.RIGHT_POINTER_ICON, TextAnnotation.RIGHT_POINTER_ICON.getName()),
            new ValueLabelItem(TextAnnotation.STAR_ICON, TextAnnotation.STAR_ICON.getName()),
            new ValueLabelItem(TextAnnotation.UP_LEFT_ARROW_ICON, TextAnnotation.UP_LEFT_ARROW_ICON.getName()),
            new ValueLabelItem(TextAnnotation.UP_ARROW_ICON, TextAnnotation.UP_ARROW_ICON.getName())};

    private SwingController controller;
    private ResourceBundle messageBundle;

    // action instance that is being edited
    private AnnotationComponent currentAnnotationComponent;

    // link action appearance properties.
    private JComboBox<ValueLabelItem> iconNameBox;

    private TextAnnotation annotation;

    public TextAnnotationPanel(SwingController controller) {
        super(new GridLayout(1, 2, 5, 2), true);

        this.controller = controller;
        this.messageBundle = this.controller.getMessageBundle();

        // Setup the basics of the panel
        setFocusable(true);

        // Add the tabbed pane to the overall panel
        createGUI();

        // Start the panel disabled until an action is clicked
        setEnabled(false);

        revalidate();
    }


    /**
     * Method that should be called when a new AnnotationComponent is selected by the user
     * The associated object will be stored locally as currentAnnotation
     * Then all of it's properties will be applied to the UI pane
     * For example if the border was red, the color of the background button will
     * be changed to red
     *
     * @param newAnnotation to set and apply to this UI
     */
    public void setAnnotationComponent(AnnotationComponent newAnnotation) {

        if (newAnnotation == null || newAnnotation.getAnnotation() == null) {
            setEnabled(false);
            return;
        }
        // assign the new action instance.
        this.currentAnnotationComponent = newAnnotation;

        // For convenience grab the Annotation object wrapped by the component
        annotation = (TextAnnotation)
                currentAnnotationComponent.getAnnotation();

        applySelectedValue(iconNameBox, annotation.getIconName());

        // disable appearance input if we have a invisible rectangle
        safeEnable(iconNameBox, true);
    }

    public void itemStateChanged(ItemEvent e) {
        ValueLabelItem item = (ValueLabelItem) e.getItem();
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (e.getSource() == iconNameBox) {
                annotation.setIconName((Name) item.getValue());
            }
            // save the action state back to the document structure.
            updateAnnotationState();
            currentAnnotationComponent.resetAppearanceShapes();
            currentAnnotationComponent.repaint();
        }
    }

    public void actionPerformed(ActionEvent e) {

    }

    /**
     * Method to create link annotation GUI.
     */
    private void createGUI() {

        // Create and setup an Appearance panel
        setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED),
                messageBundle.getString("viewer.utilityPane.annotation.text.appearance.title"),
                TitledBorder.LEFT,
                TitledBorder.DEFAULT_POSITION));
        // Line thickness
        iconNameBox = new JComboBox<ValueLabelItem>(TEXT_ICON_LIST);
        iconNameBox.setSelectedIndex(DEFAULT_ICON_NAME);
        iconNameBox.addItemListener(this);
        add(new JLabel(messageBundle.getString("viewer.utilityPane.annotation.text.iconName")));
        add(iconNameBox);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        safeEnable(iconNameBox, enabled);
    }

    private void updateAnnotationState() {
        // store old state
        AnnotationState oldState = new AnnotationState(currentAnnotationComponent);
        // store new state from panel
        AnnotationState newState = new AnnotationState(currentAnnotationComponent);
        // todo: update how state is stored as we have a lot of annotations...
//        AnnotationState changes = new AnnotationState(
//                linkType, null, 0, textMarkupType, color);
        // apply new properties to the action and the component
//        newState.apply(changes);
        // temporary apply new state info


        // Add our states to the undo caretaker
        ((AbstractDocumentViewModel) controller.getDocumentViewController().
                getDocumentViewModel()).getAnnotationCareTaker()
                .addState(oldState, newState);

        // Check with the controller whether we can enable the undo/redo menu items
        controller.reflectUndoCommands();
    }


    /**
     * Convenience method to ensure a component is safe to toggle the enabled state on
     *
     * @param comp    to toggle
     * @param enabled the status to use
     * @return true on success
     */
    protected boolean safeEnable(JComponent comp, boolean enabled) {
        if (comp != null) {
            comp.setEnabled(enabled);
            return true;
        }
        return false;
    }

    private void applySelectedValue(JComboBox comboBox, Object value) {
        comboBox.removeItemListener(this);
        ValueLabelItem currentItem;
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            currentItem = (ValueLabelItem) comboBox.getItemAt(i);
            if (currentItem.getValue().equals(value)) {
                comboBox.setSelectedIndex(i);
                break;
            }
        }
        comboBox.addItemListener(this);
    }

}