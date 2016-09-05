
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Abstract classe use for exercice resolution, when you begin a new exercice, you juste have to
 * create a new classe which extends this classe. Then you have acces to the data parsed easily.
 * @author Arnaud
 */
public abstract class Exercice {

    public InputParser data;

    // We parse the data first
    public Exercice() {
        Scanner input = new Scanner(System.in);
        this.data = new InputParser(input);
    }

    // Abstract Function which resolve the exercice
    public abstract void resolve();

}
