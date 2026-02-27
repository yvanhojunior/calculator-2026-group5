package visitor;

import calculator.MyNumber;
import calculator.Notation;
import calculator.Operation;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;

/** NotationVisitor is a concrete visitor that serves to
 * convert arithmetic expressions into their string representation in a given notation.
 */
public class NotationVisitor extends Visitor {
    
    private final Notation notation;
    private final Deque<String> stack = new ArrayDeque<>();

    public NotationVisitor(Notation notation) {
        this.notation = notation;
    }

    /**
     * Use the visitor design pattern to visit a number.
     * The number is simply pushed onto the stack as a string.
     * @param n The number being visited
     */
    @Override
    public void visit(MyNumber n) {
        stack.push(Integer.toString(n.getValue()));
    }

    @Override
    public void visit(Operation o) {
        int numArgs = o.getArgs().size();

        // Pop the required number of arguments from the stack
        Deque<String> args = new ArrayDeque<>();
        for (int i = 0; i < numArgs; i++) {
            args.push(stack.pop());
        }

        String result = switch (notation) {
            case PREFIX -> 
            // ex for 3 + 4: + ( 3, 4 ) | ex for 3 + 4 + 5: + ( 3, 4, 5 )
                o.getSymbol() + " (" + args.stream().collect(Collectors.joining(", ")) + ")";
            case INFIX -> 
            // ex for 3 + 4 | ex for 3 + 4 + 5: ( 3 + 4 + 5 )
                "( " + args.stream().collect(Collectors.joining(" " + o.getSymbol() + " ")) + " )";
            case POSTFIX -> 
            // ex for 3 + 4: ( 3, 4 ) + | ex for 3 + 4 + 5: ( 3, 4 , 5 ) + 
                "(" + args.stream().collect(Collectors.joining(", ")) + ") " + o.getSymbol();
        };

        stack.push(result);
    }

    /**
     * Return the string representation of the visited expression.
     * @return The string result
     */
    public String getResult() {
        return stack.peek();
    }
}
