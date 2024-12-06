package ast;
import environment.*;

import java.util.List;

/**
 * A ProcedureDeclaration stores the name, parameters, local variables
 * and statement(s) of the procedure.
 * @author Aditya Ramanathan
 * @version 4/13/24
 */
public class ProcedureDeclaration extends Statement
{
    private String name;
    private Statement st;
    private List<String> parms;
    private List<String> localVariables; // includes the parameters

    /**
     * Constructs a ProcedureDeclaration object with a specified name of
     * the procedure.
     * @param name the name of the procedure.
     * @param st the statement(s) in the procedure.
     * @param parms the list of parameters passed into the procedure.
     * @param localVariables the list of local variables in the procedure.
     */
    public ProcedureDeclaration(String name, Statement st, List<String> parms, List<String> localVariables)
    {
        this.name = name;
        this.st = st;
        this.parms = parms;
        this.localVariables = localVariables;
    }

    /**
     * Retrieves the name of the procedure.
     * @return the name of the procedure.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Retrieves the list of parameters passed into the procedure.
     * @return the list of parameters passed into the procedure.
     */
    public List<String> getParms()
    {
        return parms;
    }

    /**
     * Retrieves the list of local variables defined in the procedure.
     * @return the list of local variables defined in the procedure.
     */
    public List<String> getLocalVariables()
    {
        return localVariables;
    }

    /**
     * Retrieves the statement(s) of the procedure.
     * @return the statement(s) of the procedure.
     */
    public Statement getStatement()
    {
        return st;
    }

    /**
     * The exec method executes the statements in the procedure.
     * @param env the environment containing all the variables
     * needed to execute the statement.
     * @postcondition the procedure has been initialized into the
     * environment.
     */
    public void exec(Environment env)
    {
        env.setProcedure(name, this);
    }

    /**
     * Compiles the procedure declaration by producing the corresponding
     * MIPS instructions.
     * @param e the emitter used to emit the MIPS instructions.
     */
    public void compile(Emitter e)
    {
        e.emit("proc" + name + ":");
        e.emitPush("$ra");
        e.setProcedureContext(this);
        for(int i = 0; i < localVariables.size(); i++)
        {
            e.emitPush("$0");
        }
        st.compile(e);
        for(int i = 0; i < localVariables.size(); i++)
        {
            e.emitPop("$t0");
        }
        e.emitPop("$ra");
        e.emit("jr $ra");
        e.clearProcedureContext();
    }
}
