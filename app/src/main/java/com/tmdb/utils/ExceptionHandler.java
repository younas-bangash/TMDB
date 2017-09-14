/*
 * Copyright (C) 2007-2011 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */
package com.tmdb.utils;

import android.content.Context;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The Class ExceptionHandler.
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    /** The my context. */
    private final Context myContext;

    /** The Constant CRASH_REPORT. */
    public static final String CRASH_REPORT = "crashReport";

    /**
     * Instantiates a new exception handler.
     *
     * @param context the context
     */
    public ExceptionHandler(Context context) {
        myContext = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        final StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        Log.e("Error",stackTrace.toString());
    }
}