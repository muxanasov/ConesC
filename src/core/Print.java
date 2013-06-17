// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package core;

public class Print {
	
	private static int _level = LogLevel.LOG_DEBUG;
	
	public static void init(int level) {
		_level = level;
	}
	
	public static void error(Class<?> className, Object msg) {
		if (_level < LogLevel.LOG_ERRORS) return;
		System.err.println(className + ": " + msg);
	}
	
	public static void error(String component, Object msg) {
		if (_level < LogLevel.LOG_ERRORS) return;
		System.err.println(component + ": " + msg);
	}
	
	public static void info(Class<?> className, Object msg) {
		if (_level < LogLevel.LOG_INFO) return;
		System.out.println(className + ": " + msg);
	}
	
	public static void debug(Class<?> className, Object msg) {
		if (_level < LogLevel.LOG_DEBUG) return;
		System.out.println(className + ": " + msg);
	}
	
	public class LogLevel {
		public static final int LOG_NOTHING = 0;
		public static final int LOG_ERRORS = 1;
		public static final int LOG_INFO = 2;
		public static final int LOG_DEBUG = 3;
	}

}
