package threadpool;

public class ForLoop {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new EveryTaskOneThread.Task());
            thread.start();
        }
        //如果任务数量达到1000或以上，这样开销太大(消耗内存)
    }

    static class Task implements Runnable{

        @Override
        public void run() {
            System.out.println("执行了任务");
        }
    }
}
