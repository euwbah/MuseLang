package Core;

public class Metronome {

    public final int PPQ = 96;

    public double BPM = 120;

    public long currTick;

    private static long previousSysTime;
    private static Thread timerThread;
    private static long nanosPerTick;
    private static boolean clocking;

    public Metronome(double BPM) {
        this.BPM = BPM;
        nanosPerTick = (long) (1000000000 * 60 / (BPM * PPQ));
        currTick = 0;
        clocking = false;
    }

    public void start() {
        Runnable runnable = () -> {
            while(clocking) {
                long now = System.nanoTime();
                if(now - previousSysTime > nanosPerTick) {
                    previousSysTime = now;
                    currTick++;
                }
            }
        };

        timerThread = new Thread(runnable);
        previousSysTime = System.nanoTime();
        clocking = true;
        timerThread.start();
    }

    public void stop() {
        clocking = false;//This should stop the thread...
    }
}
