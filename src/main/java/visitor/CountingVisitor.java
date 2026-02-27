package visitor;

import java.util.ArrayDeque;
import java.util.Deque;

import calculator.MyNumber;
import calculator.Operation;

/** CountingVisitor is a concrete visitor that serves to
 * count the number of operations, numbers and depth of an arithmetic expression.
 */
public class CountingVisitor extends Visitor {

	private int opsCount = 0;
	private int nbsCount = 0;
	private final Deque<Integer> depthStack = new ArrayDeque<>();

	@Override
	public void visit(MyNumber n) {
		nbsCount++;
		depthStack.push(0);
	}

	@Override
	public void visit(Operation o) {
		opsCount++;
		int argCount = o.getArgs().size();
		int maxChildDepth = 0;
		for (int i = 0; i < argCount; i++) {
			if (!depthStack.isEmpty()) {
				maxChildDepth = Math.max(maxChildDepth, depthStack.pop());
			}
		}
		depthStack.push(1 + maxChildDepth);
	}

	/** @return The number of operations in the expression */
	public int getOps() {
		return opsCount;
	}

	/** @return The number of numbers in the expression */
	public int getNbs() {
		return nbsCount;
	}

	/** @return The depth of the expression tree */
	public int getDepth() {
		return depthStack.isEmpty() ? 0 : depthStack.peek();
	}
}