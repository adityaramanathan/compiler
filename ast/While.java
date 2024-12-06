package ast;
import environment.*;

/**
 * The While class stores the various parts of an While loop: the condition and
 * the statement(s) till the condition is not met anymore.
 * @author Aditya Ramanathan
 * @version 4/10/24
 */
public class While extends Statement
{
    private Condition cond;
    private Statement st;

    /**
     * Constructs a While object which stores the condition to enter the while
     * loop and the statement(s) to execute till the condition is not met anymore.
     * @param cond the condition to enter the while loop.
     * @param st the statement(s) to execute.
     */
    public While(Condition cond, Statement st)
    {
        this.cond = cond;
        this.st = st;
    }

    /**
     * Executes the statement(s) till the conditon is no longer met.
     * @param env the environment containing all the variables needed to
     * evaluate the expression.
     * @postcondition the statement(s) have been run some number of times.
     */
    public void exec(Environment env)
    {
        while(cond.eval(env))
        {
            st.exec(env);
        }
    }

    /**
     * Compiles the While loop by producing the corresponding MIPS
     * instructions using the emitter.
     * @param e the emitter used to emit the MIPS instructions.
     */
    public void compile(Emitter e)
    {
        int label = e.nextLabelID();
        e.emit("while" + label + ":");
        cond.compile(e, "endwhile" + label);
        st.compile(e);
        e.emit("j while" + label);
        e.emit("endwhile" + label + ":");
    }
}
