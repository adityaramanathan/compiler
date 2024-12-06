package ast;
import environment.*;

/**
 * The Statement class is an abstract class to represent all forms of
 * statements, including Writeln, Assignment, If, While, For, Readln, or a
 * block of statements.
 * @author Aditya Ramanathan
 * @version 3/22/24
 */
public abstract class Statement
{
    /**
     * The exec method executs the statement. This is used for the
     * Interpreter.
     * @param env the environment containing all the variables
     * needed to execute the statement.
     * @postcondition the statement has been executed and any variables
     * in the environment have been updated.
     */
    public abstract void exec(Environment env);

    /**
     * Takes in an Emitter and uses it to emit the sequence of MIPS
     * instructions corresponding to the AST component. This is used for
     * the Compiler.
     * @param e the emitter being used to produce the sequence of
     * MIPS instructions.
     */
    public abstract void compile(Emitter e);
}
