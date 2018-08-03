package org.leo.wechat4j.wxmsg.util;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

import java.io.File;

public class ChangeAudioFormat {
	public static void main(String[] args) throws Exception {
		String path1 = "E:/1435654405419.amr";
		String path2 = "E:/111111.mp3";
		changeToMp3(path1, path2);
	}

	public static void changeToMp3(String sourcePath, String targetPath) {
		File source = new File(sourcePath);
		File target = new File(targetPath);
		
		AudioAttributes audio = new AudioAttributes();
		Encoder encoder = new Encoder();

		audio.setCodec("libmp3lame");
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);

		try {
			encoder.encode(source, target, attrs);
		} catch (IllegalArgumentException e) {
			//e.printStackTrace();
		} catch (InputFormatException e) {
			//e.printStackTrace();
		} catch (EncoderException e) {
			//e.printStackTrace();
		}
	}
}