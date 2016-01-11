package main;

/**
 * Created by Andreas Appelqvist on 2016-01-11.
 */
public class Reader extends Thread {

    private Buffer buffer;
    private int count;
    private boolean running = false;

    public Reader(Buffer buffer) {
        this.buffer = buffer;
    }

    public void startThread() {
        running = true;
        this.start();
    }

    public void stopThread() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        super.run();
        while (running) {
            buffer.read();
            count++;
        }
    }
}

