package com.pelco.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MemLeakTest implements EntryPoint {

    private final Image[] images = new Image[MAX_VIEWERS];
    private static final int MAX_VIEWERS = 16;
    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        for (int i = 0; i < MAX_VIEWERS; i++) {
            SimplePanel panel = new SimplePanel();
            panel.setHeight("108px");
            panel.setWidth("192px");
            final Image me = new Image();
            images[i] = me;
            images[i].setWidth("192px");
            images[i].setHeight("108px");
            images[i].addLoadHandler(new LoadHandler() {

                @Override
                public void onLoad(final LoadEvent event) {
                    fetch(me);
                }

            });
            images[i].addErrorHandler(new ErrorHandler() {

                @Override
                public void onError(final ErrorEvent event) {
                    fetch(me);
                }

            });
            panel.add(images[i]);
            RootPanel.get().add(panel);

            fetch(images[i]);
        }

    }

    private void fetch(final Image source) {
        source.setUrl("/jpeg.jpg?uniq=" + (new Date()).getTime());
        source.setWidth("192px");
        source.setHeight("108px");
    }
}
