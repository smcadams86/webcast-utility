package llc.webcast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class StreamProperties {

	private String ffmpegLocation;
	private String ffmpegCommand;
	
	public StreamProperties(String propsFileLocation) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(propsFileLocation));
			ffmpegLocation = prop.getProperty("ffmpeg_location");
			ffmpegCommand = prop.getProperty("ffmpeg_command");
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

	public String getFfmpegLocation() {
		return ffmpegLocation;
	}

	public String getFfmpegCommand() {
		return ffmpegCommand;
	}
	
	public String getFullFfmpegCommand() {
		return ffmpegLocation + " " + ffmpegCommand;
	}

}
