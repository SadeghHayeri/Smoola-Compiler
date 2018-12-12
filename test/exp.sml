class EXP {
    def main(): int {
        var b : boolean;
        var i : int;
        var e : EXP;
        var a : int[];

        b = true;
        b = true || false && false;
        b = 23 < 34;
        b = 23 > 23;
        b = 23 == 23;
        b = 432 <> 3432;
        b = (this.main()) > 0 || false;
        b = (b && b && b) || b || b || !b && (b = b = b);
        b = this.main() > 334 && b || (!b) && (b || b) || -1 > -3;

        i = 123;
        i = 1234 + 432;
        i = 3423 + 324 * 324 / 34 - 342 - 423;
        i = ---432423--432432-4-32-4-23-4-2-34--3-4-3;
        i = new EXP().main() + 342;
        i = a[0];
        i = a[-3];
        i = a[313 + 432];
        i = a[a + 1];
        i = a[i];
        i = a[this.main()];
        i = a[b + 0];
        i = a.length;
        i = a.length + 324 / 324;
        i = this.length;
        i = (a + 0).length;
        i = xxx.length;
        i = "fdsf".length;
        i = (a = a)[i];
        i = (a = a = a = a)[a.length-1];
        i = new int[new int[a.length][a.length]].length;
        i = (new int[(new int[a.length])[10]]).length;
        i = i = new int[new int[xxx][xxx]].length;

        e = this;
        e = this + this - this / this * this;
        e = this.f();
        e = this.main();
        e = new EXP();
        e = new EXP().main();
        e = new EXP().main(423);
        e = new EXP().main(notDeclareVariable);

        a = new int[34];
        a = new int[a];
        a = new int[i];
        a = new int[this];



        return 0;
    }
}