class Test {
	def main() : int {
	    if(1 < 2) then
	        writeln(new T().x(30+8-21));
		return 11;
	}
}

class S {
    var d : int;
    var k : int;
    def init(i : int) : int{
        k = i;

        return 0;
    }
    def sag(a : T) : string{
        var t : string;
        if(k == 1) then{
            t = "first";
            k = k + 1;
         }
        else
            t = "sec";
        return t;
    }
}


class T {
    var a : int;
    var f : S;
    def x(c : int) : int {
        var ne : Next;
        var j : T;
        var b : int;
        j = new T();

        b = 3;
        a = -b;
        if(a <> 0 && b == 3 && b < 4 && a > -3 || true) then{
            f = new S();
            ne = new Next();
            a = f.init(3);
            a = ne.met(f);
        }
		return 3;
	}
}


class Next{
    var t : T;
    var n : int;
    var b : int;
    def met(s : S):int{
        t = new T();
        b = 3;
        writeln(b);
        writeln(b);
        while(b <> 1){
            writeln("salam");
            b = -b ;
            writeln(b);
        }
        return 0;
    }
}