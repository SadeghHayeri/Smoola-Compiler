class MainClass {
    def main(): int {
        writeln(new SecondMain().main());
        return 0;
    }
}

class SecondMain {
    var s : Rectangle;
    def main() : int {
        var x : int;
        s = new Rectangle();
        x = s.constructor(10, 5);
        return s.area();
    }
}

class Rectangle {
    var x1 : int;
    var y1 : int;
    var name : string;
    def constructor(x : int, y : int) : int {
        x1 = x;
        y1 = y;
        return 0;
    }
    def area() : int {
        return x * y;
    }
}