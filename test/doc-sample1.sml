class Test extends x {
    def main(): int {
        writeln((new BabyTest()).testMethod(1, 2));
        return 0;
    }
}

class BabyTest {
    var test1 : int[];
    var test2 : boolean;
    def testMethod(f1 : int, f2 : int) : int {
        var i : int;
        i = 0;
        test1 = new int[1];

        while(i <> 10) {
            test1[i] = i;
        }
        if(test1[1] == 1) then
            test2 = true;
        else {
            test2 = false;
        }
        return test2;
    }
}