/*
 * Copyright 2011 John Ahlroos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.jasoft.dragdroplayouts.drophandlers;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.gwt.client.MouseEventDetails;
import com.vaadin.terminal.gwt.client.ui.dd.HorizontalDropLocation;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

import fi.jasoft.dragdroplayouts.DDAbsoluteLayout;
import fi.jasoft.dragdroplayouts.DDHorizontalSplitPanel;
import fi.jasoft.dragdroplayouts.DDHorizontalSplitPanel.HorizontalSplitPanelTargetDetails;
import fi.jasoft.dragdroplayouts.events.HorizontalLocationIs;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

@SuppressWarnings("serial")
public class DefaultHorizontalSplitPanelDropHandler implements DropHandler {

    public void drop(DragAndDropEvent event) {
        // Casting to a layout bound transferable since we assume a component
        // must come from another layout
        LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                .getTransferable();
        HorizontalSplitPanelTargetDetails details = (HorizontalSplitPanelTargetDetails) event
                .getTargetDetails();
        Component component = transferable.getComponent();
        DDHorizontalSplitPanel panel = (DDHorizontalSplitPanel) details
                .getTarget();
        ComponentContainer source = (ComponentContainer) transferable
                .getSourceComponent();

        if (component == panel) {
            // Dropping myself on myself, if parent is absolute layout then
            // move
            if (component.getParent() instanceof DDAbsoluteLayout) {
                MouseEventDetails mouseDown = transferable.getMouseDownEvent();
                MouseEventDetails mouseUp = details.getMouseEvent();
                int movex = mouseUp.getClientX() - mouseDown.getClientX();
                int movey = mouseUp.getClientY() - mouseDown.getClientY();

                DDAbsoluteLayout parent = (DDAbsoluteLayout) component
                        .getParent();
                ComponentPosition position = parent.getPosition(component);

                float x = position.getLeftValue() + movex;
                float y = position.getTopValue() + movey;
                position.setLeft(x, Sizeable.UNITS_PIXELS);
                position.setTop(y, Sizeable.UNITS_PIXELS);
            }

        } else {

            // Remove component from its source
            source.removeComponent(component);

            if (details.getDropLocation() == HorizontalDropLocation.LEFT) {
                // Dropped in the left area
                panel.setFirstComponent(component);

            } else if (details.getDropLocation() == HorizontalDropLocation.RIGHT) {
                // Dropped in the right area
                panel.setSecondComponent(component);
            }
        }
    }

    public AcceptCriterion getAcceptCriterion() {
        // Only allow dropping in slots, not on the center bar
        return new Not(HorizontalLocationIs.CENTER);
    }

}