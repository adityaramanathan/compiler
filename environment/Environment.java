package environment;
import ast.*;

import java.util.Map;
import java.util.HashMap;

/**
 * The Environment class is what remembers the values of variables and
 * the definitions of procedures that are contained in the program.
 * It uses a hashmap to store the variable name and its value. It also
 * uses a hashmap to store the name of the procedure to its
 * ProcedureDeclaration object containing the statements contained within
 * the procedure.
 *
 * Usage:
 * Environment env = new Environment(null);
 *
 * @author Aditya Ramanathan
 * @version 4/13/24
 */
public class Environment
{
    private Map<String, Integer> variables;
    private Map<String, ProcedureDeclaration> procedures; // null if not global environment
    private Environment parent;

    /**
     * Constructs an environment by instantiating the hashmaps that
     * will be used to perform variable lookup and procedure lookup.
     * @param parent the parent environment to this. It will be null
     * for the global environment as shown in the usage for this class.
     */
    public Environment(Environment parent)
    {
        variables = new HashMap<String, Integer>();
        procedures = new HashMap<String, ProcedureDeclaration>();
        this.parent = parent;
    }

    /**
     * Declares the variable and its value as provided into the variables hashmap.
     * @param variable the name of the variable.
     * @param value the value in the variable.
     * @postcondition the variables hashmap contains the variable and its value.
     */
    public void declareVariable(String variable, int value)
    {
        variables.put(variable, value);
    }

    /**
     * Updates the value for a specific variable or declares the variable in
     * the appropriate environment. If the value is not in the current
     * environment, it searches in parent environments till it finds the value
     * or reaches the global environment.
     * @param variable the name of the variable.
     * @param value the value in the variable.
     * @postcondition the variables hashmap in the appropriate environment has been
     * updated or declared.
     */
    public void setVariable(String variable, int value)
    {
        Environment curr = this;
        while(curr != null && !curr.variables.containsKey(variable))
        {
            curr = curr.getParent();
        }

        if(curr == null)
        {
            declareVariable(variable, value);
        }
        else
        {
            curr.declareVariable(variable, value);
        }
    }

    /**
     * Retrieves the value associated with the variable specified.
     * @param variable the name of the variable.
     * @return the value of the variable or 0 if the variable was never
     * given a value.
     */
    public int getVariable(String variable)
    {
        if(!variables.containsKey(variable) && parent != null)
        {
            return parent.getVariable(variable);
        }
        else if(variables.containsKey(variable))
        {
            return variables.get(variable);
        }
        return 0;
    }

    /**
     * Associates the given procedure name with the procedure declaration, which
     * contains the statements to be executed. The procedure name and declaration
     * are only stored in the global environment.
     * @param procedureName the name of the procedure.
     * @param procedureDec the declaration of the procedure.
     * @postcondition the procedures hashmap contains the name of the procedure
     * and its respective declaration.
     */
    public void setProcedure(String procedureName, ProcedureDeclaration procedureDec)
    {
        Environment env = this;
        while(env.parent != null)
        {
            env = env.parent;
        }
        env.procedures.put(procedureName, procedureDec);
    }

    /**
     * Retrieves the procedure declaration given the name of the procedure.
     * @param procedureName the name of the procedure.
     * @return the procedure declaration object corresponding to the name.
     */
    public ProcedureDeclaration getProcedure(String procedureName)
    {
        Environment env = this;
        while(env.parent != null)
        {
            env = env.parent;
        }
        return env.procedures.get(procedureName);
    }

    /**
     * Retrieves the parent environment.
     * @return the parent environment.
     */
    public Environment getParent()
    {
        return parent;
    }
}
