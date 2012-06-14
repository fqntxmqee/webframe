package org.webframe.support.util;

import java.text.NumberFormat;

/**
 * StopWatch
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-6-8 上午8:43:47
 * @version
 */
public class StopWatch extends org.springframework.util.StopWatch {

	public final static ThreadLocal<StopWatch>	stopWatchThreadLocal	= new ThreadLocal<StopWatch>();

	private StopWatch										child						= null;

	private int												childIndex				= -1;

	private int												childDeep				= 0;

	public StopWatch() {
		super();
	}

	public StopWatch(String id) {
		super(id);
	}

	public static StopWatch getStopWatch(String defaultId, boolean fetchChild) {
		StopWatch stopWatch = StopWatch.stopWatchThreadLocal.get();
		if (stopWatch == null) {
			stopWatch = new StopWatch(defaultId);
			StopWatch.stopWatchThreadLocal.set(stopWatch);
		} else if (fetchChild) {
			if (!stopWatch.hasChildStopWatch()) {
				stopWatch = stopWatch.initChildStopWatch();
			} else {
				stopWatch = stopWatch.child;
			}
		}
		if (stopWatch.isRunning()) {
			stopWatch.stop();
		}
		return stopWatch;
	}

	public boolean hasChildStopWatch() {
		return child != null;
	}

	public StopWatch initChildStopWatch() {
		if (child == null) {
			child = new StopWatch("child");
			childIndex = getTaskCount();
			child.childDeep = childDeep + 1;
		}
		return child;
	}

	public void print() {
		if (isRunning()) {
			stop();
		}
		SystemLogUtils.println(prettyPrint());
	}

	@Override
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder(shortSummary());
		sb.append('\n');
		sb.append("---------------------------------------------\n");
		sb.append("ms       %       Task name\n");
		sb.append("---------------------------------------------\n");
		sb.append(formatPrint(this));
		return sb.toString();
	}

	protected String formatPrint(StopWatch stopWatch) {
		StringBuilder sb = new StringBuilder();
		String _blank = getBlank(stopWatch.childDeep);
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumIntegerDigits(5);
		nf.setGroupingUsed(false);
		NumberFormat pf = NumberFormat.getPercentInstance();
		pf.setMinimumIntegerDigits(3);
		pf.setGroupingUsed(false);
		for (int i = 0; i < stopWatch.getTaskInfo().length; i++) {
			TaskInfo task = stopWatch.getTaskInfo()[i];
			sb.append(_blank)
				.append(nf.format(task.getTimeMillis()))
				.append("    ");
			sb.append(pf.format(task.getTimeSeconds() / getTotalTimeSeconds()))
				.append("    ");
			sb.append(task.getTaskName()).append("\n");
			if (stopWatch.childIndex == i) {
				sb.append(formatPrint(stopWatch.child));
			}
		}
		return sb.toString();
	}

	private String getBlank(int i) {
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < i; j++) {
			sb.append(" ");
		}
		return sb.toString();
	}
}
