package ast;
import environment.*;

/**
 * The Condition class represents the comparison between two expressions
 * that follows a "IF" or "WHILE" token in order to determine whether a
 * line(s) of code should be executed.
 * @author Aditya Ramamanthan
 * @version 4/10/24
 */
public class Condition
{
    private Expression left;
    private String relop;
    private Expression right;

    /**
     * Instantiates a Condition object with the left expression, the right
     * expression, and the relop which is the comparison operator between them.
     * @param left the expression on the left side of the operator.
     * @param relop the comparison operator.
     * @param right the expression on the right side of the operator.
     */
    public Condition(Expression left, String relop, Expression right)
    {
        this.left = left;
        this.right = right;
        this.relop = relop;
    }

    /**
     * Evaluates the expression and determines whether the "IF" statement
     * should be executed or the "WHILE" loop should be executed.
     * @param env the environment containing all the variables needed to
     * evaluate the expression.
     * @return true if the condition is true, false if the condition is false.
     */
    public boolean eval(Environment env)
    {
        if(relop.equals(">"))
        {
            return left.eval(env) > right.eval(env);
        }
        else if(relop.equals("<"))
        {
            return left.eval(env) < right.eval(env);
        }
        else if(relop.equals("<="))
        {
            return left.eval(env) <= right.eval(env);
        }
        else if(relop.equals(">="))
        {
            return left.eval(env) >= right.eval(env);
        }
        else if(relop.equals("="))
        {
            return left.eval(env) == right.eval(env);
        }
        else
        {
            return left.eval(env) != right.eval(env);
        }
    }

    /**
     * Compiles the Condition object by compiling the left statement and
     * using the stack to have $v0 open to compile the right statement. Then
     * it produces the correct statement in MIPS based on the operator.
     * @param e the emitter used to emit the MIPS instructions.
     */
    public void compile(Emitter e, String label)
    {
        left.compile(e);
        e.emitPush("$v0");
        right.compile(e);
        e.emitPop("$t0");
        if(relop.equals(">"))
        {
            e.emit("ble $t0 $v0 " + label);
        }
        else if(relop.equals("<"))
        {
            e.emit("bge $t0 $v0 " + label);
        }
        else if(relop.equals("<="))
        {
            e.emit("bgt $t0 $v0 " + label);
        }
        else if(relop.equals(">="))
        {
            e.emit("ble $t0 $v0 " + label);
        }
        else if(relop.equals("="))
        {
            e.emit("bne $t0 $v0 " + label);
        }
        else
        {
            e.emit("beq $t0 $v0 " + label);
        }
    }
}
