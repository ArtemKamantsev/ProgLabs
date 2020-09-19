package lab1;

import java.io.PrintStream;

class PrintNumberRunnable implements Runnable {
    private PrintStream out;
    private Condition condition;

    PrintNumberRunnable(PrintStream out, Condition condition) {
        this.out = out;
        this.condition = condition;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        out.printf("Started %s\n", name);
        for (int i = 0; condition.check(i); i++) {
            out.printf("%d %s\n", i, name);
        }
        out.printf("Finished %s\n", name);
    }
}