class IDENTIFIER {
    def main() : int {
        return new SecondMain().main();
    }
}

class SecondMain {
    var arr : int[];
    var x : int;
    var y : boolean;

    def main(): int {
        x = (2 + (3 || 4)) / 7;
        arr = new int[10];
        if(arr.length == 10 * x) then
            y = true;
        else
            y = false;
        return y;
    }
}