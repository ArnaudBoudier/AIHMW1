
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
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
