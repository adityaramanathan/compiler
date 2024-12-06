package ast;
import environment.*;

/**
 * The Assignment class models an assignment instruction by storing
 * a specific variable and the expression being assigned to it.
 * @author Aditya Ramanathan
 * @version 4/1/24
 */
public class Assignment extends Statement
{
    private String var;
    private Expression exp;

    /**
     * Creates an Assignment object with the variable name and the expression
     * that is its value.
     * @param var the variable name.
     * @param exp the value stored in the variable.
     */
    public Assignment(String var, Expression exp)
    {
        this.var = var;
        this.exp = exp;
    }

    /**
     * Retrieves the variable name.
     * @return the variable name.
     */
    public String getVarName()
    {
        return var;
    }

    /**
     * Sets the variable to the expression in the environment.
     * @param env the environment containing the variables needed to perform the
     * operation and evaulate the expressions.
     * @postcondition the environment contains the variable and its computed value.
     */
    public void exec(Environment env)
    {
        env.setVariable(var, exp.eval(env));
    }

    /**
     * Compiles the Assignment object by compiling the expression and assigning
     * the value in that expression to the local variable or global variable.
     * @param e the emitter used to emit the MIPS instructions.
     */
    public void compile(Emitter e)
    {
        exp.compile(e);
        if(e.isLocalVariable(var))
        {
            e.emit("sw $v0 " + e.getOffset(var) + "($sp)");
        }
        else
        {
            e.emit("sw $v0 " + var);
        }
    }
}
