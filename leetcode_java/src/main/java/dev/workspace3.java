package dev;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

// https://javaguide.cn/java/concurrent/java-concurrent-questions-01.html#%E4%BD%95%E4%B8%BA%E7%BA%BF%E7%A8%8B

/** Progress, Thread test */

public class workspace3 {
    public static void main(String[] args) {

        // 获取 Java 线程管理 MXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 不需要获取同步的 monitor 和 synchronizer 信息，仅获取线程和线程堆栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        // 遍历线程信息，仅打印线程 ID 和线程名称信息
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
        }
    }

    // LC 125
    public boolean isPalindrome(String s) {

        if (s == null || s.length() == 0){
            return true;
        }
        String s_updated = "";
        StringBuilder sb = new StringBuilder();
        //String[] s_split = s.split(",");
        char[] array = s.toCharArray();
        for (char x : array){
            String x_ = Character.toString(x);
            if (x_ != null && x_ != "" && Character.isAlphabetic(x)){
                sb.append(x_.toLowerCase());
            }
        }

        System.out.println(sb.toString());
        return sb.toString() == sb.reverse().toString();
    }

}
