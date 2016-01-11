package main;

import sun.awt.Mutex;

/**
 * Created by Andreas Appelqvist on 2016-01-08.
 */
public class Buffer {

    private int max;
    private int writePos = 0;
    private int readPos = 0;
    private int findPos = 0;

    private String findString = "är";
    private String replaceString = "dum";

    private Word[] buffer;

    private Mutex lock = new Mutex();

    public Buffer() {
        this.max = 10;
        buffer = new Word[max];
        for(int i = 0; i < buffer.length; i++){
            buffer[i] = new Word();
        }
    }

    public synchronized void write(String str) {
        //System.out.println("början på write");
        //showBuffer();
        while (buffer[writePos].getStatus() != 0) {
            try {
                notify();
                Thread.sleep(0);
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        lock.lock();
        buffer[writePos].setWord(str);
        buffer[writePos].setStatus(1);
        lock.unlock();



        writePos = (writePos + 1) % max;
    }

    public synchronized void checking() {
        while (buffer[findPos].getStatus() != 1) {
            try {
                notify();
                Thread.sleep(0);
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String str = "";
        lock.lock();
        if (!findString.equals("")) {
            str = buffer[findPos].getWord();
            if (!replaceString.equals("")) {
                if (str.equals(findString)) {
                    buffer[findPos].setWord(replaceString);
                }
            }
        }
        buffer[findPos].setStatus(2);
        lock.unlock();
        findPos = (findPos+1)%max;
    }

    public synchronized void read() {
        while (buffer[readPos].getStatus() != 2) {
            try {
                notify();
                Thread.sleep(0);
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String str = "";
        lock.lock();
            str = buffer[readPos].getWord();
            buffer[readPos].setStatus(0);
        lock.unlock();

        readPos = (readPos+1)%max;
        System.out.println(str);

        showBuffer();
    }


    public void showBuffer(){
        System.out.print("\n**Buffer**\n");
        for(int i = 0; i < buffer.length; i++){
            System.out.print(buffer[i].getStatus()+" ");
        }
        System.out.print("\n**Buffer**\n");
    }
}
