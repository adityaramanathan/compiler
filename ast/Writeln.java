package ast;
import environment.*;

/**
 * The Writeln allows an expression to be evaluated and printed to the
 * terminal.
 * @author Aditya Ramanathan
 * @version 4/1/24
 */
public class Writeln extends Statement
{
    private Expression exp;

    /**
     * Creates a Writeln object with an expression that needs to be evaluated
     * and then printed to the terminal.
     * @param exp the expression to be evaluated and printed.
     */
    public Writeln(Expression exp)
    {
        this.exp = exp;
    }

    /**
     * Evaluates and prints the expression.
     * @param env the environment containing the variables needed to perform the
     * operation and evaulate the expressions.
     * @postcondition the value of the expression has been printed.
     */
    public void exec(Environment env)
    {
        System.out.println(exp.eval(env));
    }

    /**
     * Compiles the Writeln statement by producing the corresponding MIPS
     * instructions using the emitter.
     * @param e the emitter used to emit the MIPS instructions.
     */
    public void compile(Emitter e)
    {
        exp.compile(e);
        e.emit("move $a0 $v0");
        e.emit("li $v0 1");
        e.emit("syscall");
        e.emit("la $a0 nl");
        e.emit("li $v0 4");
        e.emit("syscall");
    }
}
