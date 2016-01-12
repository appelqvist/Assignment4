package main;

/**
 * Created by Andreas Appelqvist on 2016-01-11.
 *
 * Word-object
 *
 */
public class Word {
    private int status; //0 = Empty, 1 = New, 2 = Checked
    private String word;

    /**
     * Konstruktor
     */
    public Word(){
        this(0);
    }

    /**
     * Konstruktor
     * @param status
     */
    public Word(int status){
        this.status = status;
        word = "";
    }

    public int getStatus(){
        return status;
    }

    public String getWord(){
       return word;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public void setWord(String str){
        this.word = str;
    }
}
