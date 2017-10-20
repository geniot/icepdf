/*
 * Copyright (c) 2006-2008, Alexander Potochkin
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *   * Neither the name of the JXLayer project nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.icepdf.ri.util.jxlayer.plaf;

import org.icepdf.ri.util.jxlayer.JXLayer;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * The base class for all {@link JXLayer}'s UI delegates.
 * <br>
 * {@link #paint(java.awt.Graphics, javax.swing.JComponent)} method performes the
 * painting of the {@code JXLayer}
 * and {@link #eventDispatched(AWTEvent, JXLayer)} method is notified
 * about any {@code AWTEvent}s which have been generated by a {@code JXLayer}
 * or any of its subcomponents.
 * <br>
 * The {@code LayerUI} is different from UI delegates of the other components,
 * because it is LookAndFeel independent and is not updated by default when
 * the system LookAndFeel is changed.
 * <br>
 * The subclasses of {@code LayerUI} can either be stateless and shareable
 * by multiple {@code JXLayer}s or not shareable.
 *
 * @param <V> one of the super types of {@code JXLayer}'s view component
 * @author Alexander Potochkin
 * @see JXLayer#setUI(LayerUI)
 * @see JXLayer#setView(Component)
 * @see JXLayer#getView()
 */
public abstract class LayerUI<V extends Component>
        extends ComponentUI implements Serializable {

    private final PropertyChangeSupport propertyChangeSupport =
            new PropertyChangeSupport(this);

    /**
     * Paints the specified component.
     * Subclasses should override this method and use
     * the specified {@code Graphics} object to
     * render the content of the component.
     *
     * @param g the {@code Graphics} context in which to paint;
     * @param c the component being painted;
     *          it can be safely cast to the {@code JXLayer<V>}
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        c.paint(g);
    }

    /**
     * Dispatches {@code AWTEvent}s for {@code JXLayer}
     * and <b>all it subcomponents</b> to this {@code LayerUI}.
     * <br>
     * To enable the {@code AWTEvent} of particular type,
     * you should call {@link JXLayer#setLayerEventMask}
     * in {@link #installUI(javax.swing.JComponent)}
     * and set the layer event mask to {@code 0}
     * in {@link #uninstallUI(javax.swing.JComponent)} after that
     *
     * @param e the event to be dispatched
     * @param l the layer this LayerUI is set to
     * @see JXLayer#setLayerEventMask(long)
     * @see JXLayer#getLayerEventMask()
     */
    public void eventDispatched(AWTEvent e, JXLayer<? extends V> l) {
    }

    /**
     * Invoked when {@link JXLayer#updateUI()} is called
     * from the {@code JXLayer} this {@code LayerUI} is set to.
     *
     * @param l the {@code JXLayer} which UI is updated
     */
    public void updateUI(JXLayer<? extends V> l) {
    }

    /**
     * Configures the {@code JXLayer} this {@code LayerUI} is set to.
     * The default implementation registers the {@code LayerUI}
     * as a property change listener for the passed {@code JXLayer}
     *
     * @param c the {@code JXLayer} where this UI delegate is being installed
     */
    public void installUI(JComponent c) {
        addPropertyChangeListener((JXLayer) c);
    }

    /**
     * {@inheritDoc}
     */
    public void uninstallUI(JComponent c) {
        removePropertyChangeListener((JXLayer) c);
    }

    /**
     * Adds a PropertyChangeListener to the listener list. The listener is
     * registered for all bound properties of this class.
     * <br>
     * If <code>listener</code> is <code>null</code>,
     * no exception is thrown and no action is performed.
     *
     * @param listener the property change listener to be added
     * @see #removePropertyChangeListener
     * @see #getPropertyChangeListeners
     * @see #addPropertyChangeListener(String, java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the listener list. This method
     * should be used to remove PropertyChangeListeners that were registered
     * for all bound properties of this class.
     * <br>
     * If listener is null, no exception is thrown and no action is performed.
     *
     * @param listener the PropertyChangeListener to be removed
     * @see #addPropertyChangeListener
     * @see #getPropertyChangeListeners
     * @see #removePropertyChangeListener(String, PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Returns an array of all the property change listeners
     * registered on this component.
     *
     * @return all of this ui's <code>PropertyChangeListener</code>s
     *         or an empty array if no property change
     *         listeners are currently registered
     * @see #addPropertyChangeListener
     * @see #removePropertyChangeListener
     * @see #getPropertyChangeListeners(String)
     */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return propertyChangeSupport.getPropertyChangeListeners();
    }

    /**
     * Adds a PropertyChangeListener to the listener list for a specific
     * property.
     * <br>
     * If <code>propertyName</code> or <code>listener</code> is <code>null</code>,
     * no exception is thrown and no action is taken.
     *
     * @param propertyName one of the property names listed above
     * @param listener     the property change listener to be added
     * @see #removePropertyChangeListener(String, PropertyChangeListener)
     * @see #getPropertyChangeListeners(String)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName,
                                          PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Removes a <code>PropertyChangeListener</code> from the listener
     * list for a specific property. This method should be used to remove
     * <code>PropertyChangeListener</code>s
     * that were registered for a specific bound property.
     * <br>
     * If <code>propertyName</code> or <code>listener</code> is <code>null</code>,
     * no exception is thrown and no action is taken.
     *
     * @param propertyName a valid property name
     * @param listener     the PropertyChangeListener to be removed
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #getPropertyChangeListeners(String)
     * @see #removePropertyChangeListener(PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName,
                                             PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * Returns an array of all the listeners which have been associated
     * with the named property.
     *
     * @return all of the <code>PropertyChangeListener</code>s associated with
     *         the named property; if no such listeners have been added or
     *         if <code>propertyName</code> is <code>null</code>, an empty
     *         array is returned
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #removePropertyChangeListener(String, PropertyChangeListener)
     * @see #getPropertyChangeListeners
     */
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return propertyChangeSupport.getPropertyChangeListeners(propertyName);
    }

    /**
     * Support for reporting bound property changes for Object properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName the property whose value has changed
     * @param oldValue     the property's previous value
     * @param newValue     the property's new value
     */
    protected void firePropertyChange(String propertyName,
                                      Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Notifies the {@code LayerUI} when any of its property is changed
     * and allows to update every {@code JXLayer} this {@code LayerUI} is set to.
     *
     * @param evt the PropertyChangeEvent generated by this {@code LayerUI}
     * @param l   the {@code JXLayer} this LayerUI is set to
     */
    public void handlePropertyChangeEvent(PropertyChangeEvent evt, JXLayer<? extends V> l) {
    }

    /**
     * Returns the preferred size of the viewport for a view component.
     *
     * @return the preferred size of the viewport for a view component
     * @see Scrollable#getPreferredScrollableViewportSize ()
     */
    public Dimension getPreferredScrollableViewportSize(JXLayer<? extends V> l) {
        if (l.getView() instanceof Scrollable) {
            return ((Scrollable) l.getView()).getPreferredScrollableViewportSize();
        }
        return l.getPreferredSize();
    }

    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one block
     * of rows or columns, depending on the value of orientation.
     *
     * @return the "block" increment for scrolling in the specified direction
     * @see Scrollable#getScrollableBlockIncrement(Rectangle, int, int)
     */
    public int getScrollableBlockIncrement(JXLayer<? extends V> l,
                                           Rectangle visibleRect,
                                           int orientation, int direction) {
        if (l.getView() instanceof Scrollable) {
            return ((Scrollable) l.getView()).getScrollableBlockIncrement(
                    visibleRect, orientation, direction);
        }
        return (orientation == SwingConstants.VERTICAL) ? visibleRect.height :
                visibleRect.width;
    }

    /**
     * Returns false to indicate that the height of the viewport does not
     * determine the height of the layer, unless the preferred height
     * of the layer is smaller than the viewports height.
     *
     * @return whether the layer should track the height of the viewport
     * @see Scrollable#getScrollableTracksViewportHeight()
     */
    public boolean getScrollableTracksViewportHeight(JXLayer<? extends V> l) {
        if (l.getView() instanceof Scrollable) {
            return ((Scrollable) l.getView()).getScrollableTracksViewportHeight();
        }
        if (l.getParent() instanceof JViewport) {
            return (((JViewport) l.getParent()).getHeight() > l.getPreferredSize().height);
        }
        return false;
    }

    /**
     * Returns false to indicate that the width of the viewport does not
     * determine the width of the layer, unless the preferred width
     * of the layer is smaller than the viewports width.
     *
     * @return whether the layer should track the width of the viewport
     * @see Scrollable
     * @see LayerUI#getScrollableTracksViewportWidth(JXLayer)
     */
    public boolean getScrollableTracksViewportWidth(JXLayer<? extends V> l) {
        if (l.getView() instanceof Scrollable) {
            return ((Scrollable) l.getView()).getScrollableTracksViewportWidth();
        }
        return l.getParent() instanceof JViewport && ((l.getParent()).getWidth() > l.getPreferredSize().width);
    }

    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one new row
     * or column, depending on the value of orientation.  Ideally,
     * components should handle a partially exposed row or column by
     * returning the distance required to completely expose the item.
     * <br>
     * Scrolling containers, like JScrollPane, will use this method
     * each time the user requests a unit scroll.
     *
     * @return The "unit" increment for scrolling in the specified direction.
     *         This value should always be positive.
     * @see Scrollable#getScrollableUnitIncrement(Rectangle, int, int)
     */
    public int getScrollableUnitIncrement(JXLayer<? extends V> l,
                                          Rectangle visibleRect,
                                          int orientation, int direction) {
        if (l.getView() instanceof Scrollable) {
            return ((Scrollable) l.getView()).getScrollableUnitIncrement(
                    visibleRect, orientation, direction);
        }
        return 1;
    }
}
