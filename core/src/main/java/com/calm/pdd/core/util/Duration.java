package com.calm.pdd.core.util;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class Duration {
	public static String humanize(long duration) {
		return DurationFormatUtils.formatDuration(duration, "HH:mm:ss");
	}
}
