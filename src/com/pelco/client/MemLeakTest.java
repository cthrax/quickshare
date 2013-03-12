package com.pelco.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MemLeakTest implements EntryPoint {

    private final MyImage[] images = new MyImage[MAX_VIEWERS];
    private final MyImage[] hiddenImages = new MyImage[MAX_VIEWERS];
    private static final int MAX_VIEWERS = 16;
    
    /** The loaded handler. */
    private final HandlerRegistration[] loadedHandler = new HandlerRegistration[MAX_VIEWERS];

    /** The loaded handler. */
    private final HandlerRegistration[] errorHandler = new HandlerRegistration[MAX_VIEWERS];
    
    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        for (int i = 0; i < MAX_VIEWERS; i++) {
            FlowPanel panel = new FlowPanel();
            panel.getElement().getStyle().setFloat(Style.Float.LEFT);
            panel.getElement().getStyle().setMargin(8, Unit.PX);
            panel.setHeight("108px");
            panel.setWidth("192px");
            final MyImage me = new MyImage();
            me.setIndex(i);
            me.getElement().getStyle().setProperty("border", "2px dashed orange");
            final MyImage hidden = new MyImage();
            hidden.setIndex(i);
            hidden.getElement().getStyle().setProperty("border", "2px solid firebrick");
            hidden.setVisible(false);
            images[i] = me;
            hiddenImages[i] = hidden;
            images[i].setWidth("192px");
            images[i].setHeight("108px");
            
            final LoadHandler successfulHandler = new LoadHandler() {
                @Override
                public void onLoad(final LoadEvent event) {
                    swap(me);
                }
            };

            final ErrorHandler failedHandler = new ErrorHandler() {
                @Override
                public void onError(final ErrorEvent event) {
                    swap(me);
                }
            };
            
            loadedHandler[i] = hiddenImages[i].addLoadHandler(successfulHandler);
            errorHandler[i] = hiddenImages[i].addErrorHandler(failedHandler);
            images[i].addLoadHandler(successfulHandler);
            images[i].addErrorHandler(failedHandler);
            panel.add(images[i]);
            panel.add(hiddenImages[i]);
            RootPanel.get().add(panel);

            fetch(hiddenImages[i]);
        }

    }

    private void fetch(final MyImage source) {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                if (hiddenImages[source.getIndex()].isVisible()) {
                    swap(source);
                } else {
                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                        @Override
                        public void execute() {
                            Timer delay = new Timer() {
                                @Override
                                public void run() {
                                    source.setUrl("/jpeg.jpg?uniq=" + (new Date()).getTime());
                                }
                            };
                            delay.schedule(10);
                        }});
                }
            }});
    }
    
    private void swap(final MyImage source) {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                int index = source.getIndex();
                final MyImage t = images[index];
                images[index] = hiddenImages[index];
                images[index].setWidth("192px");
                images[index].setHeight("108px");
                images[index].setVisible(true);
                hiddenImages[index] = t;
                hiddenImages[index].setHeight(0 + "px");
                hiddenImages[index].setWidth(0 + "px");
                hiddenImages[index].setVisible(false);
                fetch(hiddenImages[index]);
            }});
    }
    
    private class MyImage extends Image {
        int index;
        
        void setIndex(final int index) {
            this.index = index;
        }
        
        int getIndex() {
            return index;
        }
    }
}
