package io.train.modules.oss.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.VideoAttributes;

public class VideoTools {

	private static final int GET_FRAMES_LENGTH = 5;

	public static String getScreenshot(String filePath) {
		return getScreenshot(filePath, null);
	}

	public static String getScreenshot(String filePath, String saveImagePath) {
		String result = null;
		FFmpegFrameGrabber grabber;
		try {
			grabber = FFmpegFrameGrabber.createDefault(filePath);
			System.out.println(grabber.getVideoCodec());
			System.out.println(grabber.getVideoCodecName());
			// 第一帧图片存储位置(也是视频路径)
			System.out.println(filePath);
			String targerFilePath = filePath.substring(0, filePath.lastIndexOf(File.separator));
			// 视频文件名
			String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
			// 图片名称
			String targetFileName = fileName.substring(0, fileName.lastIndexOf("."));
			grabber.start();
			// 视频总帧数
			int videoLength = grabber.getLengthInFrames();

			Frame frame = null;
			int i = 0;
			while (i < videoLength) {
				// 过滤前5帧,避免出现全黑的图片,依自己情况而定(每循环一次取一帧)
				frame = grabber.grabFrame();
				if ((i > GET_FRAMES_LENGTH) && (frame.image != null)) {
					break;
				}
				i++;
			}

			// 视频旋转度
			String rotate = grabber.getVideoMetadata("rotate");
			Java2DFrameConverter converter = new Java2DFrameConverter();
			// 绘制图片
			BufferedImage bi = converter.getBufferedImage(frame);
			if (rotate != null) {
				// 旋转图片
				bi = rotate(bi, Integer.parseInt(rotate));
			}
			// 图片的类型
			String imageMat = "jpg";
			// 图片的完整路径
			String imagePath = targerFilePath + File.separator + targetFileName + "." + imageMat;
			if (null != saveImagePath && !"".equals(saveImagePath)) {
				// 指定路径
				imagePath = saveImagePath + "." + imageMat;
			}

			// 创建文件
			File output = new File(imagePath);
			ImageIO.write(bi, imageMat, output);

			// 拼接Map信息
			result = output.getPath();	// 视频总帧数
			grabber.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}



	public static void changeByteTypes2Mp4(String path,String pathsss){
		File source = new File(path);
	    File target = new File(pathsss);
	    AudioAttributes audio = new AudioAttributes();
	    audio.setCodec("libmp3lame"); //音频编码格式
		audio.setBitRate(new Integer(800000));
		audio.setChannels(new Integer(1));
		VideoAttributes video = new VideoAttributes();
		video.setCodec("libx264");//视频编码格式
		video.setBitRate(new Integer(3200000));
		video.setFrameRate(new Integer(15));//数字设置小了，视频会卡顿
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp4");
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);
		Encoder encoder = new Encoder();
		MultimediaObject multimediaObject = new MultimediaObject(source);
		try {
			encoder.encode(multimediaObject, target, attrs);
			source.deleteOnExit();
		} catch (IllegalArgumentException | EncoderException e) {
			e.printStackTrace();
		}
	}

	public static void changeByteTypes2Mp4(File source,File target){
	    AudioAttributes audio = new AudioAttributes();
	    audio.setCodec("libmp3lame"); //音频编码格式
		audio.setBitRate(new Integer(800000));
		audio.setChannels(new Integer(1));
		VideoAttributes video = new VideoAttributes();
		video.setCodec("libx264");//视频编码格式
		video.setBitRate(new Integer(3200000));
		video.setFrameRate(new Integer(15));//数字设置小了，视频会卡顿
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp4");
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);
		Encoder encoder = new Encoder();
		MultimediaObject multimediaObject = new MultimediaObject(source);
		try {
			encoder.encode(multimediaObject, target, attrs);
			source.deleteOnExit();
		} catch (IllegalArgumentException | EncoderException e) {
			e.printStackTrace();
		}
	}


	private static BufferedImage rotate(BufferedImage src, int angel) {
		int src_width = src.getWidth(null);
		int src_height = src.getHeight(null);
		int type = src.getColorModel().getTransparency();
		Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
		BufferedImage bi = new BufferedImage(rect_des.width, rect_des.height, type);
		Graphics2D g2 = bi.createGraphics();
		g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
		g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
		g2.drawImage(src, 0, 0, null);
		g2.dispose();
		return bi;
	}

	private static Rectangle calcRotatedSize(Rectangle src, int angel) {
		if (angel >= 90) {
			if (angel / 90 % 2 == 1) {
				int temp = src.height;
				src.height = src.width;
				src.width = temp;
			}
			angel = angel % 90;
		}
		double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
		double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
		double angel_dalta_width = Math.atan((double) src.height / src.width);
		double angel_dalta_height = Math.atan((double) src.width / src.height);
		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
		int des_width = src.width + len_dalta_width * 2;
		int des_height = src.height + len_dalta_height * 2;
		return new java.awt.Rectangle(new Dimension(des_width, des_height));
	}
}
