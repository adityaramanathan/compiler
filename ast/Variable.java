package ast;
import environment.*;

/**
 * The Variable class stores the name of a variable defined in the program.
 * The value of the variable is accessed through the environment class.
 * @author Aditya Ramanathan
 * @version 4/10/24
 */
public class Variable extends Expression
{
    private String name;

    /**
     * Constructs a Variable object with a specified name.
     * @param name the name of the variable.
     */
    public Variable(String name)
    {
        this.name = name;
    }

    /**
     * Retrieves the name of the variable.
     * @return the name of the variable.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Evaluates the number - in other words it just returns the
     * value of the variable. The eval method is not really needed here,
     * but since Variable is an Expression, it needs to implement the
     * abstract method.
     * @param env the environment needed to evaluate an expression. Once
     * again, not needed for the Variable class.
     * @return the value of the variable.
     */
    public int eval(Environment env)
    {
        return env.getVariable(name);
    }

    /**
     * Compiles the variable object by loading the value associated with
     * the variable's address in the register $v0 in the MIPS program.
     * @param e the emitter used to emit the MIPS instructions.
     */
    public void compile(Emitter e)
    {
        if(e.isLocalVariable(name))
        {
            e.emit("lw $v0 " + e.getOffset(name) + "($sp)");
        }
        else
        {
            e.emit("la $t0 " + name);
            e.emit("lw $v0 ($t0)");
        }
    }
}
