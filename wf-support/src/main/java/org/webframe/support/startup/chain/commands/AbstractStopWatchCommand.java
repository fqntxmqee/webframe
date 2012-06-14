
package org.webframe.support.startup.chain.commands;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.webframe.support.util.StopWatch;

/**
 * 框架启动日志命令
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-6-8 上午9:53:10
 * @version
 */
public abstract class AbstractStopWatchCommand implements Command {

	protected StopWatch	currentStopWatch	= null;

	/* (non-Javadoc)
	 * @see org.apache.commons.chain.Command#execute(org.apache.commons.chain.Context)
	 */
	@Override
	public boolean execute(Context context) throws Exception {
		StopWatch stopWatch = getStopWatch();
		StopWatch.stopWatchThreadLocal.set(stopWatch);
		stopWatch.start("开始" + getStopWatchId() + "...");
		boolean result = executeWithStopWatch(context);
		stopWatch.print();
		StopWatch.stopWatchThreadLocal.remove();
		return result;
	}

	protected StopWatch getStopWatch() {
		if (currentStopWatch == null) {
			currentStopWatch = new StopWatch(getStopWatchId());
		}
		return currentStopWatch;
	}

	public abstract boolean executeWithStopWatch(Context context)
				throws Exception;

	protected abstract String getStopWatchId();
}
