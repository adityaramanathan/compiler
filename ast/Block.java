package ast;
import environment.*;

import java.util.List;

/**
 * A Block represents statement(s) of code that are surrounded by a "BEGIN" token
 * and an "END" token. It stores these statement(s) in a list of statements.
 * @author Aditya Ramanathan
 * @version 4/1/24
 */
public class Block extends Statement
{
    private List<Statement> stmts;

    /**
     * Creates a Block object using the list of statements passed into
     * this constructor.
     * @param stmts the list of statements enclosed in the block.
     */
    public Block(List<Statement> stmts)
    {
        this.stmts = stmts;
    }

    /**
     * Retrieves the list of statements within the block.
     * @return the list of statements within the block.
     */
    public List<Statement> getStatements()
    {
        return stmts;
    }

    /**
     * Executes each individual statement in the list of statements.
     * @param env the environment containing the variables needed to perform the
     * operation and evaulate the expressions.
     * @postcondition the statement(s) has(ve) been executed and any variables
     * in the environment have been updated.
     */
    public void exec(Environment env)
    {
        for(Statement st: stmts)
        {
            st.exec(env);
        }
    }

    /**
     * Compiles the block object by compiling all individual statements.
     * @param e the emitter used to emit the MIPS instructions.
     */
    public void compile(Emitter e)
    {
        for(Statement st: stmts)
        {
            st.compile(e);
        }
    }
}
