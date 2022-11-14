package com.example.project;

import org.junit.platform.launcher.LauncherInterceptor;

public class MyLauncherInterceptor implements LauncherInterceptor {
    @Override
    public <T> T intercept(Invocation<T> invocation) {
        System.out.println("MyLauncherInterceptor.intercept");
        return invocation.proceed();
    }

    @Override
    public void close() {
        System.out.println("MyLauncherInterceptor.close");
    }
}
