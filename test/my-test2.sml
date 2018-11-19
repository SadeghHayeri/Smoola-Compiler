class Test {
	def main() : int {
		i = 3+5 * 5 || i + (34/5 && 2);
		arr = new int[666];
		writeln(arr.length); # the output is 666
		return 0;
	}
}

class Test2 {
	def main2() : int {
		i = 3+5 * 5 || i + (34/5 && 2)+0;
		arr = new int[666];
		writeln(arr.length); # the output is 666
		return 0;
	}
}