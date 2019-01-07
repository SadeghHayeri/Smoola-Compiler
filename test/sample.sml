class Test {
	def main() : int {
	    writeln(new T().x());
		return 1;
	}
}

class T {
    var a : int[];
    var b : int;
    var r : string;
    def x() : int {
        a = new int[10];
        r = "salam";
        writeln(r);
		return b;
	}
}