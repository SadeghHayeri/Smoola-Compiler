class RunThis {
    def main() : int {
        new XXX().main();
        return 0;
    }
}
class XXX {
    var a : boolean;
    var b : boolean;
    var c : int;
    var d : string;
    var k : int;

	def main() : int {
	    var hhh : Helper;
	    var x : X;
	    var linkedList1 : LinkedList;
	    hhh = new Helper();

	    linkedList1 = new LinkedList();
	    k = linkedList1.constructor();

	    if("a" <> "a") then {
	        writeln("ok");
	    } else {
	        writeln("nnoooooo");
	    }


	    if((c > 100) &&
	     (hhh.changeAll(10, 20, "salam") > 0)
	     && (hhh.printAll() <> "KHERS")) then
	        writeln("xxxxxxxxxxxxxxx");

	    if(hhh.changeAll(-10, 20, "salam") > 0 && hhh.printAll() <> "KHERS") then
	        writeln("yyyyyyyyyyyy");

	    k = hhh.changeAll(1000, -20, this.toString());
	    writeln(hhh.printAll());

	    c = 0;
	    while(c < 10) {

	        if(c < 5) then
	            d = "salaaam 1";
	        else {
	            if(c > 10) then
	                d = "hello!";
	            else
	                d = "khers!";
	        }

	        k = hhh.changeAll(c, c + 10, d);
	        d = hhh.printAll();
	        c = c + 1;
	    }

	    x = new X();
	    k = x.changeSecret("this is secret!");
	    writeln(x.getSecret());

	    c = 0;
	    while(c < 10) {
	        k = hhh.changeAll2(c + 10, 10);
	        writeln(hhh.getArray());
	        c = c + 1;
	    }


        k = linkedList1.printAllNode();
	    k = linkedList1.push(1);
	    k = linkedList1.push(10);
	    k = linkedList1.push(100);
	    k = linkedList1.push(1000);
	    k = linkedList1.push(10000);
	    k = linkedList1.push(100000);
	    k = linkedList1.push(1000000);
	    k = linkedList1.printAllNode();

		return 11;
	}
}

class Helper {
    var b : boolean;
    var i : int;
    var s : string;
    var a : int[];
    var x : X;
    var tmp : int;

    def pow(a : int, b : int) : int {
        var result : int;

        if(b == 0) then
            result = 1;
        if(b == 1) then
            result = a;
        else
            result = this.pow(a, b-1);

        return result;
    }

    def someFunc(a : int, i : int) : int {
        a = 231 / (i + a * i + 1) + 1;
        i = 234 / (i+1) / (i+1) / (i+1);
        return i / (i+1);
    }

    def changeAll2(aVal : int, bVal : int) : int {
        var i : int;
        tmp = this.someFunc(31, 3123);

        a = new int[1000];

        i = 0;
        while(i < aVal) {
            a[i] = i * bVal + aVal;
            i = i + 1;
        }

        i = 0;
        while(i < aVal-1) {
            a[i] = a[i+1] + 100 + aVal;
            i = i + 2;
        }

        i = 1;
        while(i < aVal) {
            a[i / 2] = a[i -1];
            i = i + 1;
        }

        return 0;
    }

    def changeAll(xx : int, yy : int, ss : string) : int {
        b = true;
        i = 10001;
        s = ss;
        a = new int[150];
        a[0] = xx + yy;
        a[10] = xx / yy;
        a[100] = (((xx - yy))) + (2 + a[10] - 100) + 34 / 3;
        a[2] = a[0] + a[1] * a[100];
        x = new X();
        tmp = x.setI(-50);
        return xx;
    }

    def printAll() : string {
        writeln("------------");
        writeln(a.length);
        writeln(3 + 3 - 5 * 6 / 7 + a[3] / (a[4] * i + i) + 1);
        writeln(a);
        writeln(this.pow(12, 6));
        writeln(new int[10][5] / 32);
        x = new X();
        tmp = x.setI(1000);
        writeln(x.getI());
        writeln(i);
        writeln(s);
        writeln(a);
        writeln(this.toString());
        i = -i;
        writeln(i);
        if(b) then
            writeln("true");
        else
            writeln("false");

        i = i + i / 34;
        s = "fsdffs";
        s = x.toString();
        a = new int[100];

        {
            writeln("this is a block!");
        }

        return "ok";
    }

    def getArray() : int[] {
        return a;
    }
}

class Y {
    var secret : string;

    def changeSecret(s : string) : int {
        secret = s;
        return 0;
    }
}

class X extends Y {
    var i : int;

    def setI(i : int) : int {
        return i = i;
    }

    def getI() : int {
        return i;
    }

    def getParentSecret() : string {
        return secret;
    }

    def getSecret() : string {
        return secret;
    }
}

class LinkedList {
    var head : Node;
    var tmp : int;

    def constructor() : int {
        head = new Node();
        tmp = head.constructor();
        return 0;
    }

    def getLastNode() : Node {
        var currNode : Node;
        currNode = head;
        while(currNode.hasNext())
            currNode = currNode.getNext();
        return currNode;
    }

    def push(data : int) : int {
        var newNode : Node;
        var lastNode : Node;

        newNode = new Node();
        tmp = newNode.constructor();
        tmp = newNode.setData(data);
        tmp = this.getLastNode().setNextNode(newNode);

        return 0;
    }

    def printAllNode() : int {
        var currNode : Node;
        currNode = head;
        while(currNode.hasNext()) {
            writeln(currNode.getData());
            currNode = currNode.getNext();
        }
        return 0;
    }
}

class Node {
    var hasNext : boolean;
    var next : Node;
    var data : int;

    def constructor() : int {
        hasNext = false;
        next = this;
        data = -1;
        return 0;
    }

    def setData(i : int) : int {
        data = i;
        return 0;
    }

    def getData() : int {
        return data;
    }

    def setNextNode(node : Node) : int {
        next = node;
        hasNext = true;
        return 0;
    }

    def unSetNext() : int {
        hasNext = false;
        return 0;
    }

    def hasNext() : boolean {
        return hasNext;
    }

    def getNext() : Node {
        return next;
    }
}