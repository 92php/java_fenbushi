package threadpool;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 演示每个任务执行前后放钩子函数
 */
public class PauseableThreadPool extends ThreadPoolExecutor {

    //标记位
    private Boolean isPaused = false;
    //为了让布尔值并发修改是安全的，上锁
    private final ReentrantLock lock = new ReentrantLock();

    private Condition unpaused = lock.newCondition();

    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    //钩子方法
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        lock.lock();
        try {
            while (isPaused){
                //休眠线程
                unpaused.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

    //暂停
    private void pause(){
        lock.lock();
        try {
            isPaused = true;
        } finally {
            lock.unlock();
        }
    }

    //恢复
    private void resume(){
        lock.lock();
        try{
            isPaused = false;
            //唤醒
            unpaused.signalAll();
        }finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) {
        PauseableThreadPool pauseableThreadPool = new PauseableThreadPool(10,20,10l,TimeUnit.SECONDS,new LinkedBlockingDeque<>());
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                System.out.println("我被执行");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        for (int i = 0; i < 10000; i++) {
            pauseableThreadPool.execute(runnable);
        }
        try {
            Thread.sleep(1500);
            pauseableThreadPool.pause();
            System.out.println("线程池被暂停了");
            Thread.sleep(1500);
            pauseableThreadPool.resume();
            System.out.println("线程池被恢复了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
