package ast;
import environment.*;

/**
 * The BinOp class stores the a component of the abstract syntax tree,
 * containing two expressions and an operaton that needs to be done on
 * them. (exp1 op exp2) is the result of the execution.
 * @author Aditya Ramanathan
 * @version 4/1/24
 */
public class BinOp extends Expression
{
    private String op;
    private Expression exp1;
    private Expression exp2;

    /**
     * Creates a BinOp object with the operator and the two expressions. The
     * expressions can be other BinOp objects, or Variable/Number objects.
     * @param op the operator.
     * @param exp1 an Expression object containing the first expression.
     * @param exp2 an Expression object containing the second expression.
     */
    public BinOp(String op, Expression exp1, Expression exp2)
    {
        this.op = op;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    /**
     * Performs the operation on the two expressions. In order to do so, it
     * evaluates the sub components of the tree contained within the two expressions,
     * and then performs exp1 op exp2.
     * @param env the environment containing the variables needed to perform the
     * operation and evaulate the expressions.
     * @return the value obtained after the operation is performed.
     */
    public int eval(Environment env)
    {
        if(op.equals("+"))
        {
            return exp1.eval(env) + exp2.eval(env);
        }
        else if(op.equals("-"))
        {
            return exp1.eval(env) - exp2.eval(env);
        }
        else if(op.equals("*"))
        {
            return exp1.eval(env) * exp2.eval(env);
        }
        else if(op.equals("/"))
        {
            return exp1.eval(env) / exp2.eval(env);
        }
        else
        {
            return exp1.eval(env) % exp2.eval(env);
        }
    }

    /**
     * Compiles the BinOp object by compiling the first expression, pushing
     * it to the stack, compiling the second, popping the first from the stack
     * into another register and putting the final value in $v0.
     * @param e the emitter used to emit the MIPS instructions.
     */
    public void compile(Emitter e)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");
        if(op.equals("+"))
        {
            e.emit("addu $v0 $v0 $t0");
        }
        else if(op.equals("-"))
        {
            e.emit("subu $v0 $t0 $v0");
        }
        else if(op.equals("*"))
        {
            e.emit("mult $t0 $v0");
            e.emit("mflo $v0");
        }
        else if(op.equals("/"))
        {
            e.emit("div $t0 $v0");
            e.emit("mflo $v0");
        }
        else if(op.equals("%"))
        {
            e.emit("div $t0 $v0");
            e.emit("mfhi $v0");
        }
    }
}
