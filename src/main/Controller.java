package main;

import java.util.LinkedList;

/**
 * Created by Andreas Appelqvist on 2016-01-08.
 */
public class Controller {

    private Buffer buffer;
    private Writer writer;
    private Reader reader;
    private Modifier modifier;
    private GUIMonitor gui;

    public Controller(){
        LinkedList<String> txt;
        txt = new LinkedList<String>();
        txt.addFirst("HEJ");
        txt.addFirst("Jag");
        txt.addFirst("hETER");
        txt.addFirst("Andreas");
        txt.addFirst("du");
        txt.addFirst("är");
        txt.addFirst("en");
        txt.addFirst("prick");
        buffer = new Buffer();
        writer = new Writer(buffer, txt);
        reader = new Reader(buffer);
        modifier = new Modifier(buffer);

        writer.startThread();
        reader.startThread();
        modifier.startThread();
    }
}
