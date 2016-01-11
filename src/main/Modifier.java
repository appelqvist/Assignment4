package main;

/**
 * Created by Andreas Appelqvist on 2016-01-11.
 */
public class Modifier extends Thread {
    private Buffer buffer;
    private int count;
    private boolean running = false;

    public Modifier(Buffer buffer){
        this.buffer = buffer;
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
        while(running){
            buffer.checking();
            count++;
        }
    }
}
