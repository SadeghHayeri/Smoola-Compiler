class IDENTIFIER {
    def main(): int {
        return new SecondMain().main();
    }
}

class SecondMain{
    var arr: int[];
    var x: int;
    var y: boolean;
    def main(): int{
        x=(2+(3||4))/7;
        arr=new int[10];
        if(arr.length==10*x) then
            y=true;
        else
            y=false;
        return y;
    }
}

class MainClass {
    def main(): int {
        return new Test2().method2();
    }
}

class Test1{
    var i: int;
    def method(): string{
        var j: string;
        j = "hello world!";
        return j;

    }
}

class Test2 extends Test1{
    def method2(): int{
        i=10;
        return i;
    }
}

class MainClass{
    def main(): int{
        return 0;
    }
}

class Test1{
    var i: int;
    def salam(): string{
        return "salam";

    }
    def testMethod(): int{
        i=10;
        writeln(this.salam());
        return 0;
    }
}

class MainClass{
    def main(): int{
        writeln(new Math().factorial(5));
        return 0;
    }
}
class Math{
    var i: int;
    def factorial(f: int): int{
        var j: int;
        j=1;
        while(f<>0){
            j=j*f;
        }
        return j;
    }
}