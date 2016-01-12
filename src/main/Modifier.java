package main;

/**
 * Created by Andreas Appelqvist on 2016-01-11.
 *
 * Modifier
 */
public class Modifier extends Thread {
    private Buffer buffer;
    private int count;
    private boolean running = false;

    /**
     * Konstruktor
     * @param buffer
     */
    public Modifier(Buffer buffer){
        this.buffer = buffer;
    }

    /**
     * Startar tråden
     */
    public void startThread(){
        running = true;
        this.start();
    }

    /**
     * Stoppa tråden
     */
    public void stopThread(){
        running = false;
        this.interrupt();
    }

    /**
     * Checking loop
     */
    @Override
    public void run() {
        super.run();
        while(running){
            buffer.checking();
            count++;
        }
    }
}
