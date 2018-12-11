package symbolTable;

import java.util.*;

public class SymbolTable {

	boolean strict;
	public SymbolTable pre;
	public HashMap<String, SymbolTableItem> items;

	// Static members region

	public static SymbolTable top;
	private static Stack<SymbolTable> stack = new Stack<SymbolTable>();

	// Use it in pass 1 scope start
	public static void push(SymbolTable symbolTable) {
		if(top != null)
			stack.push(top);
		top = symbolTable;
	}

	// Use it in pass1 scope end
	public static void pop() {
		top = stack.pop();
	}

	// End of static members region

	public SymbolTable(boolean strict) {
		this(null, strict);
	}

	public SymbolTable(SymbolTable pre, boolean strict) {
		this.strict = strict;
		this.pre = pre;
		this.items = new HashMap<String, SymbolTableItem>();
	}

	public void put(SymbolTableItem item) throws ItemAlreadyExistsException {
        if(items.containsKey(item.getKey()))
            throw new ItemAlreadyExistsException();

        if(strict) {
            try {
                get(item.getKey());
                // item found!
                throw new ItemAlreadyExistsException();
            } catch (ItemNotFoundException ignored) {
                // item not found in current and all pre current
            }
        }

        items.put(item.getKey(), item);
	}

	public SymbolTableItem getInCurrentScope(String key) {
		return items.get(key);
	}

	public SymbolTableItem get(String key) throws ItemNotFoundException {
		SymbolTableItem value = items.get(key);
		if(value == null && pre != null)
			return pre.get(key);
		else if(value == null)
			throw new ItemNotFoundException();
		else
			return value;
	}

	public SymbolTable getPreSymbolTable() {
		return pre;
	}
}