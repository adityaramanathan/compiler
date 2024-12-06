package ast;
import environment.*;

/**
 * The Number class stores the value of a number encountered when parsing
 * through the stream of tokens.
 * @author Aditya Ramanathan
 * @version 4/1/24
 */
public class Number extends Expression
{
    private int value;

    /**
     * Instantiates a Number object with a specific integer value.
     * @param value the value of the number.
     */
    public Number(int value)
    {
        this.value = value;
    }

    /**
     * Retrieves the value of the Number.
     * @return the value of the Number.
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Evaluates the number - in other words it just returns the
     * value of the number. The eval method is not really needed here,
     * but since Number is an Expression, it needs to implement the
     * abstract method.
     * @param env the environment needed to evaluate an expression. Once
     * again, not needed for the Number class.
     * @return the value of the number.
     */
    public int eval(Environment env)
    {
        return value;
    }

    /**
     * Compiles the number by producing the corresponding MIPS instructions
     * using the emitter, which is just loading the value into the register $v0.
     * @param e the emitter used to emit the MIPS instructions.
     */
    public void compile(Emitter e)
    {
        e.emit("li $v0 " + value);
    }
}
