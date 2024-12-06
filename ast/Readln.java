package ast;
import environment.*;

/**
 * The Readln class allows a variable to be assigned to a value inputted by
 * the user.
 * @author Aditya Ramanathan
 * @version 4/12/24
 */
public class Readln extends Statement
{
    private String var;

    /**
     * Creates a Readln object with the name of the variable.
     * @param var the name of the variable.
     */
    public Readln(String var)
    {
        this.var = var;
    }

    /**
     * Assignes the variable to the value inputted by the user. It prompts
     * the user to input a value into the terminal.
     * @param env the environment containing the variables needed to perform the
     * operation and evaulate the expressions.
     * @postcondition the environment contains the variable set to its new value.
     */
    public void exec(Environment env)
    {
        java.util.Scanner scTemp = new java.util.Scanner(System.in);
        int value = scTemp.nextInt();
        scTemp.close();
        env.setVariable(var, value);
    }

    /**
     * Compiles the Readln statement by producing the corresponding MIPS
     * instructions using the emitter.
     * @param e the emitter used to emit the MIPS instructions.
     */
    public void compile(Emitter e)
    {
        e.emit("li $v0 5");
        e.emit("syscall");
    }
}
