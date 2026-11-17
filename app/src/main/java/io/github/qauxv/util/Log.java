/*
 * QAuxiliary - An Xposed module for QQ/TIM
 * Copyright (C) 2019-2022 qwq233@qwq2333.top
 * https://github.com/cinit/QAuxiliary
 *
 * This software is non-free but opensource software: you can redistribute it
 * and/or modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either
 * version 3 of the License, or any later version and our eula as published
 * by QAuxiliary contributors.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and eula along with this software.  If not, see
 * <https://www.gnu.org/licenses/>
 * <https://github.com/cinit/QAuxiliary/blob/master/LICENSE.md>.
 */

package io.github.qauxv.util;

import androidx.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Locale;

public class Log {

    private Log() {
    }

    private static final String TAG = "QAuxv";
    private static final int MAX_BUFFERED_LOGS = 2000;
    private static final ArrayDeque<String> sLogBuffer = new ArrayDeque<>(MAX_BUFFERED_LOGS);

    private static void record(char level, @NonNull String msg, Throwable e) {
        String time = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.ROOT).format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(time).append(' ').append(level).append(" [").append(Thread.currentThread().getName()).append("] ").append(msg);
        if (e != null) {
            sb.append('\n').append(android.util.Log.getStackTraceString(e));
        }
        synchronized (sLogBuffer) {
            if (sLogBuffer.size() >= MAX_BUFFERED_LOGS) {
                sLogBuffer.pollFirst();
            }
            sLogBuffer.addLast(sb.toString());
        }
    }

    /**
     * Get the logs recorded in the current process, oldest first.
     */
    @NonNull
    public static String getBufferedLogs() {
        StringBuilder sb = new StringBuilder();
        synchronized (sLogBuffer) {
            for (String line : sLogBuffer) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    public static void clearBufferedLogs() {
        synchronized (sLogBuffer) {
            sLogBuffer.clear();
        }
    }

    public static void e(@NonNull String msg) {
        record('E', msg, null);
        android.util.Log.e(TAG, msg);
    }

    public static void w(@NonNull String msg) {
        record('W', msg, null);
        android.util.Log.w(TAG, msg);
    }

    public static void i(@NonNull String msg) {
        record('I', msg, null);
        android.util.Log.i(TAG, msg);
    }

    public static void d(@NonNull String msg) {
        record('D', msg, null);
        android.util.Log.d(TAG, msg);
    }

    public static void v(@NonNull String msg) {
        record('V', msg, null);
        android.util.Log.v(TAG, msg);
    }

    public static void e(@NonNull Throwable e) {
        record('E', e.toString(), e);
        android.util.Log.e(TAG, e.toString(), e);
    }

    public static void w(@NonNull Throwable e) {
        record('W', e.toString(), e);
        android.util.Log.w(TAG, e.toString(), e);
    }

    public static void i(@NonNull Throwable e) {
        record('I', e.toString(), e);
        android.util.Log.i(TAG, e.toString(), e);
    }

    public static void d(@NonNull Throwable e) {
        record('D', e.toString(), e);
        android.util.Log.d(TAG, e.toString(), e);
    }

    public static void e(@NonNull String msg, @NonNull Throwable e) {
        record('E', msg, e);
        android.util.Log.e(TAG, msg, e);
    }

    public static void w(@NonNull String msg, @NonNull Throwable e) {
        record('W', msg, e);
        android.util.Log.w(TAG, msg, e);
    }

    public static void i(@NonNull String msg, @NonNull Throwable e) {
        record('I', msg, e);
        android.util.Log.i(TAG, msg, e);
    }

    public static void d(@NonNull String msg, @NonNull Throwable e) {
        record('D', msg, e);
        android.util.Log.d(TAG, msg, e);
    }

    @NonNull
    public static String getStackTraceString(@NonNull Throwable th) {
        return android.util.Log.getStackTraceString(th);
    }
}
