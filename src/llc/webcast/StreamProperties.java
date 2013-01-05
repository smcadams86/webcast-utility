package llc.webcast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class StreamProperties {

	private String ffmpegLocation;
	private String ffmpegCommand;
	
	// meta data
	private String ministerName;
	private String bibleVerses;
	private String serviceTime;

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

	public String getMinisterName() {
		return ministerName;
	}

	public void setMinisterName(String ministerName) {
		this.ministerName = ministerName;
	}

	public String getBibleVerses() {
		return bibleVerses;
	}

	public void setBibleVerses(String bibleVerses) {
		this.bibleVerses = bibleVerses;
	}

	public String getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
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
