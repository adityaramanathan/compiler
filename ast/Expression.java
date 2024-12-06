package ast;
import environment.*;

/**
 * The Expression class is an abstract class to represent all forms of
 * expressions such as BinOp which can perform an operation on two statements,
 * Number, and Variable.
 * @author Aditya Ramanathan
 * @version 3/22/24
 */
public abstract class Expression
{
    /**
     * Evaluates an expression correctly. This is used for the Interpreter.
     * @param env the environment containing all the variables needed to
     * evaluate the expression.
     * @return the fully simplified value of the expression.
     */
    public abstract int eval(Environment env);

    /**
     * Takes in an Emitter and uses it to emit the sequence of MIPS
     * instructions corresponding to the AST component. This is used
     * for the Compiler.
     * @param e the emitter being used to produce the sequence of
     * MIPS instructions.
     */
    public abstract void compile(Emitter e);
}
