package ast;
import environment.*;

import java.util.List;

/**
 * A ProcedureCall stores the name of the procedure when it is actually invoked
 * during the program. The respective ProcedureDeclaration can then be found in
 * the environment and the procedure can be executed.
 * @author Aditya Ramanathan
 * @version 4/13/24
 */
public class ProcedureCall extends Expression
{
    private String name;
    private List<Expression> args;

    /**
     * Constructs a ProcedureCall object with the specified name.
     * @param name the name of the procedure being invoked.
     * @param args the list of values passed into the procedure when
     * it is invoked in the main body of code or the body of another
     * procedure.
     */
    public ProcedureCall(String name, List<Expression> args)
    {
        this.name = name;
        this.args = args;
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
     * Retrieves the list of values of parameters passed into the procedure.
     * @return the list of values of parameters passed into the procedure.
     */
    public List<Expression> getArgs()
    {
        return args;
    }

    /**
     * Evaluates the procedure call. It creates the child environment for the
     * procedure, and computes the values of the parameters using the
     * procedureDeclaration.
     * @param env the environment containing the variables needed to perform the
     * operation and evaulate the expressions.
     * @return the value obtained after the code within the procedure is executed.
     */
    public int eval(Environment env)
    {
        Environment child = new Environment(env);
        ProcedureDeclaration procedureDec = env.getProcedure(name);
        List<String> parms = procedureDec.getParms();
        for(int i = 0; i < parms.size(); i++)
        {
            child.declareVariable(parms.get(i), args.get(i).eval(env));
        }
        child.declareVariable(name, 0);
        procedureDec.getStatement().exec(child);
        return child.getVariable(name);
    }

    /**
     * Compiles the Procedure Call by producing the corresponding MIPS
     * instructions.
     * @param e the emitter used to emit the MIPS instructions.
     */
    public void compile(Emitter e)
    {
        for(int i = 0; i < args.size(); i++)
        {
            args.get(i).compile(e);
            e.emitPush("$v0");
        }
        e.emitPush("$ra");
        e.emit("jal proc" + name);
        e.emitPop("$ra");
        for(int i = 0; i < args.size(); i++)
        {
            e.emitPop("$t0");
        }
    }
}
