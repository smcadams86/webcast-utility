package llc.webcast;

import java.util.logging.Logger;

import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;

public class StreamResultHandler extends DefaultExecuteResultHandler {
	private ExecuteWatchdog watchdog;
	private boolean isRunning = false;
	
	private final static Logger log = Logger.getLogger(StreamResultHandler.class.getName()); 

	public StreamResultHandler(ExecuteWatchdog watchdog) {
		this.watchdog = watchdog;
	}

	public StreamResultHandler(int exitValue) {
		super.onProcessComplete(exitValue);
	}

	public void onProcessComplete(int exitValue) {
		super.onProcessComplete(exitValue);
		setRunning(false);
		log.info("[resultHandler] The stream was successfully completed...");
	}

	public void onProcessFailed(ExecuteException e) {
		super.onProcessFailed(e);
		setRunning(false);
		if (watchdog != null && watchdog.killedProcess()) {
			log.info("[resultHandler] The stream process timed out");
		} else {
			log.info("[resultHandler] The stream process failed to do : " + e.getMessage());
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
