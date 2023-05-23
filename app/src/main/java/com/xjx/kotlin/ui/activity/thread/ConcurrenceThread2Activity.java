package com.xjx.kotlin.ui.activity.thread;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.android.apphelper2.utils.LogUtil;
import com.xjx.kotlin.R;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrenceThread2Activity extends FragmentActivity {

    private Boolean isPauseFlag = false;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);// 慢
    private final HashSet<String> mSet = new HashSet<String>();
    private String SOCKET_SERVICE = "socket-service";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concurrence_thread2);

        initData();
    }

    private void initData() {
        findViewById(R.id.btn_start).setOnClickListener(v -> start());
        findViewById(R.id.btn_pause).setOnClickListener(v -> pause());

        executorService.execute(new ExecutorRunnable(SOCKET_SERVICE));
    }

    class ExecutorRunnable implements Runnable {
        private String tag;
        private int count = 0;

        ExecutorRunnable(String tag) {
            this.tag = tag;
            LogUtil.e(" 创建线程： ---> " + tag);
        }

        @Override
        public void run() {
            if (Objects.equals(tag, SOCKET_SERVICE)) {
                while (true) {
                    Iterator<String> iterator = mSet.iterator();
                    if (iterator.hasNext()) {
                        if (!isPauseFlag) {
                            String next = iterator.next();
                            LogUtil.e("next: " + next);
                        }
                    }
                }
            } else {
                // client
                while (true) {
                    mSet.add("tag = " + tag + "  item: " + count);
                    count++;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        LogUtil.e("send error: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void start() {
        isPauseFlag = false;
        executorService.execute(new ExecutorRunnable("OBJ - 1"));
        executorService.execute(new ExecutorRunnable("OBJ - 2"));
        executorService.execute(new ExecutorRunnable("OBJ - 3"));
        executorService.execute(new ExecutorRunnable("OBJ - 4"));
    }

    private void pause() {
        isPauseFlag = true;
        LogUtil.e(" 暂停了   当前的flag ---> pause : true");
//        executorService.shutdown();
        executorService.shutdown();
    }
}