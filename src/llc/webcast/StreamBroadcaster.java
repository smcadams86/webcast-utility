package llc.webcast;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;

public class StreamBroadcaster {
	
	private static final int MAX_STREAM_TIME = 4 * 60 * 60 * 1000; // 4 hours

	private ExecuteWatchdog watchdog;
	private CommandLine cmdLine;
	private Executor executor;
	private StreamResultHandler resultHandler;
	private StreamProperties streamProperties;
	
	public StreamBroadcaster(StreamProperties streamProperties) {
		this.streamProperties = streamProperties;
		cmdLine = CommandLine.parse(streamProperties.getFullFfmpegCommand());
		
		watchdog = new ExecuteWatchdog(MAX_STREAM_TIME);
		resultHandler = new StreamResultHandler(watchdog);
		executor = new DefaultExecutor();
		executor.setExitValue(1);
		executor.setWatchdog(watchdog);
	}

	public void start() throws ExecuteException, IOException, InterruptedException {
		
		
		
		resultHandler.setRunning(true);
		executor.execute(cmdLine, resultHandler);
	}

	public void stop() {
		watchdog.destroyProcess();
	}

	public boolean isRunning() {
		return resultHandler.isRunning();
	}

}
