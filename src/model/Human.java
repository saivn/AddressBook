package model;

/**
 * Created by sasha on 18.05.2017.
 */
public class Human {
    private String famyli;
    private String name;
    private String patron;
    private String imageFace;

    public Human(String famyli, String name, String patron, String imageFace) {
        this.famyli = famyli;
        this.name = name;
        this.patron = patron;
        this.imageFace = imageFace;
    }

    public String getFamyli() {
        return famyli;
    }

    public void setFamyli(String famyli) {
        this.famyli = famyli;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatron(String patron) {
        this.patron = patron;
    }

    public String getName() {
        return name;
    }

    public String getPatron() {
        return patron;
    }

    public String getImageFace() {return imageFace;}



}
