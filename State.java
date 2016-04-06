
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mukesh Kumar Sunder
 */
public class State {
    char[] state;
    int id;
    State(char[] s){
        this.state = Arrays.copyOf(s, s.length);
    }
    State(char[] s,int i){
        this.state = Arrays.copyOf(s, s.length);
        this.id = i;
    }
    State(State s){
        this.id = s.id;
        this.state = s.state;
    }
}
