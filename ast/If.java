package ast;
import environment.*;

/**
 * The If class stores the various parts of an If statement: the condition,
 * the statement to be executed if the condition is true, and the else statement
 * that is executed if the condition is false.
 * @author Aditya Ramanathan
 * @version 4/10/24
 */
public class If extends Statement
{
    private Condition cond;
    private Statement st;
    private Statement elseSt;
    private boolean hasElse;

    /**
     * Instantiates an If Statement without an Else clause.
     * @param cond the condition to run the statement.
     * @param st the statement to be executed if the if condition evaluates
     * to true.
     */
    public If(Condition cond, Statement st)
    {
        this.cond = cond;
        this.st = st;
    }

    /**
     * Instantiates an If Statement that also has an Else clause.
     * @param cond the condition to run the statement in the If clause.
     * @param st the statement to execute if the if condition evaluates to true.
     * @param elseSt the statement to execute if the if condition evaluates
     * to false.
     */
    public If(Condition cond, Statement st, Statement elseSt)
    {
        this.cond = cond;
        this.st = st;
        this.elseSt = elseSt;
        hasElse = true;
    }

    /**
     * Evaluates the condition whether it be the statement in the IF clause
     * or the statement in the ELSE clause.
     * @param env the environment containing all the variables needed to
     * evaluate the expression.
     * @postcondition either the statement or the else statement has been
     * executed.
     */
    public void exec(Environment env)
    {
        if(elseSt == null)
        {
            if(cond.eval(env))
            {
                st.exec(env);
            }
        }
        else
        {
            if(cond.eval(env))
            {
                st.exec(env);
            }
            else
            {
                elseSt.exec(env);
            }
        }
    }

    /**
     * Compiles the If Statement by producing the corresponding MIPS
     * instructions.
     * @param e the emitter used to emit the MIPS instructions.
     */
    public void compile(Emitter e)
    {
        int label = e.nextLabelID();
        if(hasElse)
        {
            cond.compile(e, "else" + label);
            st.compile(e);
            e.emit("j endif" + label);
            e.emit("else" + label + ":");
            elseSt.compile(e);
        }
        else
        {
            cond.compile(e, "endif" + label);
            st.compile(e);
        }
        e.emit("endif" + label + ":");
    }
}
