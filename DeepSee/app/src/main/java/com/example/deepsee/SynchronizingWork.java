package com.example.deepsee;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.LinkedList;
import java.util.List;

public class SynchronizingWork extends Worker {
    private static final List<Runnable> tasks = new LinkedList<>();
    public SynchronizingWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void addTask(Runnable task){
        tasks.add(task);
    }

    @NonNull
    @Override
    public Result doWork() {
        for (Runnable t:tasks){
            t.run();
        }

        return Result.success();
    }
}
