package main;

import java.util.LinkedList;

/**
 * Created by Andreas Appelqvist on 2016-01-11.
 */
public class Writer extends Thread{
    private Buffer buffer;
    private LinkedList<String> textToWrite;
    private boolean running = false;

    public Writer(Buffer buffer, LinkedList<String> txt){
        this.buffer = buffer;
        this.textToWrite = txt;
    }

    public void startThread(){
        running = true;
        this.start();
    }

    public void stopThread(){
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        super.run();
        String str = "";
        while(running && !textToWrite.isEmpty()){
            System.out.println(textToWrite.size());
            str = textToWrite.removeFirst();
            buffer.write(str);
        }
    }
}
