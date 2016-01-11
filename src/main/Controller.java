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


    public Controller(GUIMonitor gui){
        this.gui = gui;
        this.buffer = new Buffer(this);
    }

    public void writeToGUIDest(String str){
        gui.appendDestinationPane(str);
    }

    public LinkedList<String> stringToLinkedList(String str){
        LinkedList<String> list = new LinkedList<String>();
        String[] array = str.split(" ");
        for(int i = 0; i < array.length; i++){
            list.addLast(array[i]);
        }
        System.out.println(list);
        return list;
    }

    public void startCopy(boolean notify, String find, String replace, LinkedList<String> txt){
        if(writer != null){
            writer.stopThread();
            writer = null;
        }
        if(reader != null){
            reader.stopThread();
            reader = null;
        }

        if(modifier != null){
            modifier.stopThread();
            modifier = null;
        }

        writer = new Writer(buffer, txt);
        reader = new Reader(buffer);
        modifier = new Modifier(buffer);
        buffer.setNotify(notify);
        buffer.setFindAndReplace(find, replace);

        runThreads();

    }

    public void runThreads(){
        writer.startThread();
        reader.startThread();
        modifier.startThread();
    }
}
