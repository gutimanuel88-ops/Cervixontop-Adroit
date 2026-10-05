package com.cervixontop.mobile;

public final class NodeRuntime {
    static { System.loadLibrary("node"); System.loadLibrary("native-lib"); }
    private NodeRuntime() {}
    public static native int startNode(String[] args);
}
