package org.leo.wechat4j.wxmsg.material;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.activation.MimetypesFileTypeMap;

import net.sf.json.JSONObject;
import org.leo.wechat4j.wxmsg.util.Constants;

/**
 * 上传微信素材
 * @author Leo
 * @create 2018-08-03
 */
public class UploadMaterialUtil {
	
	/**
	 * 上传普通素材：分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
	 * @param accessToken
	 * @param fileName
	 * @param fileLength
	 * @param is
	 * @return
	 */
	public static String uploadMaterial(String accessToken,String fileName,long fileLength,InputStream is) {
		return upload(accessToken, fileName, fileLength, is, 0, null, null);
	}
	
	/**
	 * 上传视频素材
	 * @param accessToken
	 * @param fileName
	 * @param fileLength
	 * @param title
	 * @param introduction
	 * @param is
	 * @return
	 */
	public static String uploadVideoMaterial(String accessToken,String fileName,long fileLength, String title, String introduction,InputStream is) {
		return upload(accessToken, fileName, fileLength, is, 1, title,introduction);
	}
	
	
	/**
	 * 上传素材
	 */
	private static String upload(String accessToken,String fileName,long fileLength,InputStream is,
			int isVideo, String title, String introduction) {
		String result = null;
		try {
			String urlString = Constants.WEIXIN_API.ADD_MATERIAL + "access_token=" + accessToken; //请求url
			String type = new MimetypesFileTypeMap().getContentType(fileName); //获取 mime type
			URL url = new URL(urlString);
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false); //post方式不能使用缓存
			
			// 设置请求头信息
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			
			// 设置http boundary
			String BOUNDARY = "----------" + System.currentTimeMillis();
			con.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);
			
			// 请求正文信息
			StringBuilder sb = new StringBuilder();
			
			sb.append("--"); //这两个横杠是http协议要求用来分隔提交的参数
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"type\" \r\n\r\n"); //参数名
			sb.append(type+"\r\n"); //参数的值
			
			if(isVideo == 1){
				//视频相关参数
				JSONObject jo = new JSONObject();
				jo.put("title", title);
				jo.put("introduction", introduction);

				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data;name=\"description\" \r\n\r\n");
				sb.append(jo.toString()+"\r\n");
			}
			
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			
			// media参数相关的信息
			sb.append("Content-Disposition: form-data;name=\"media\";filename=\""
					+ fileName + "\";filelength=\"" + fileLength + "\" \r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			
			byte[] head = sb.toString().getBytes("utf-8");
			OutputStream out = new DataOutputStream(con.getOutputStream()); // 获得输出流
			out.write(head); // 输出表头
			DataInputStream in = new DataInputStream(is);
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//设置最后数据分隔线，必须以-- 结尾
			out.write(foot);
			out.flush();
			out.close();
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			result = buffer.toString();
		} catch (Exception e) {
			System.out.println("********上传素材失败******");
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
	/*	String filePath = "C:/Users/20140316/Desktop/getheadimg.jpg";
		String acc = "VSEckM8N8HiQbCqe5IS5pwRIr5HmnLrzC-pJH7GZf4onylFeD3p8ztNJ6_U282HN_97qo29HjjCit4vt3mehbbgLzWMuzReu_ZCstEnIUO4";
		File file = new File(filePath);
		UploadMaterialUtil.uploadImg(acc,file);*/
	}
}
