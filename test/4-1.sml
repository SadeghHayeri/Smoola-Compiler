class Test {
	def main() : int {
		writeln(new Test2().print(1, "string input"));
		new Test2().add();
		new conditionalClass().checkBooleanStuff();
		new Test2().classCreationStuff();
		writeln(new TestWrite().test());
		return 0;
	}
}

class TestWrite{
	var cvar: int;
	def test(): int{
		var s: string;
		var x: int;
		x = 12;
		cvar = 1;
		writeln(cvar);
		s = "my name is N";
		writeln(s);
		writeln("hi");
		return x;
	}
}

class ArrayStuff{
	def x(y :int[]):int[]{
		var b: boolean;
		var i: int[];
		b = true || false;
		if(b) then {
			writeln("local boolean variable");
		}
		writeln(y[0]);
		i = new int[4];
		i[0] = 11;
		i[1] = 22;
		i[3] = 33;
		return i;
	}
}

class thisStuff extends TestWrite{
	def another(): int{
		var x2: int;
		var arr:int[];
		arr = new int[3];
		arr[0] = 1000;
		arr[1] = 2000;
		arr[2] = 3000;
		writeln("=============================================");
		writeln((new ArrayStuff().x(arr))[1]);
		writeln("=============================================");
		return 12;
	}
	def aFunc(): int{
		var x3: int;
		x3 = this.test();
		return this.another();
	}
}

class Test2 {
	def add(): int{
		var arr: int[];
		var i:int;
		var t: thisStuff;
		writeln("IN TEST2 ADD");
		arr = new int[10];
		writeln(arr.length);
		i = 1 + 3;
		writeln(i);
		t = new thisStuff();
		writeln(t.aFunc());
		return 14;
	}
	def print(g :int,k: string):string{
		writeln(g);
		writeln(k);
		return "this is a string return type";
	}
	def classCreationStuff():int{
		var x: WhileClass;
		var y: int;
		x = new WhileClass();
		y = x.printInWhile();
		writeln(y);
		return 0;
	}
}

class WhileClass{
	def printInWhile(): int{
		var i: int;
		i = 0;
		while(i<4){
			writeln("In a loop!!");
			i = i + 1;
			if(i==3) then{
				writeln("3");
			}
			else {
				writeln("not 3");
			}
		}
		return -1;
	}
}

class conditionalClass{
	def boolReturnType(b: boolean): boolean{
		if(b) then {
			writeln("boolReturnType TRUE input value");
		}
		else {
			writeln("boolReturnType FALSE input value");
		}
		return true;
	}
	def checkBooleanStuff(): int{
		if(this.boolReturnType(false)) then {
			writeln("this.boolReturnType()");
		}
		if(true) then{
			writeln("true");
		}
		if(true && false) then{
			writeln("true && false");
		}
		if(false) then{
			writeln("false");
		}
		if(true || false) then{
			writeln("true || false");
		}
		if(true && !false) then{
			writeln("true && !false");
		}
		if(1>4) then {
			writeln("1>4");
		}
		if(7+1>6) then{
			writeln("7+1>6");
		}
		if(1<4) then {
			writeln("1<4");
		}
		if(1 <> new WhileClass().printInWhile()) then {
			writeln("1 <> new WhileClass.printInWhile()");
		}
		if(1 == new WhileClass().printInWhile()) then {
			writeln("1 == new WhileClass.printInWhile()");
		}
		return 0;
	}
}