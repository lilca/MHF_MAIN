package jp.skd.lilca.mhf.web;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.util.List;

import org.apache.http.NameValuePair;

public class HttpMultipartClient {

	private static final String BOUNDARY = "----------V2ymHFg03ehbqgZCaKO6jy";
	private String targetUrl;
	private List<NameValuePair> textPostData;
	private String imageName;
	private String imagePath;
	
	public HttpMultipartClient(String url, List<NameValuePair> postData, String imgName, String imgPath){
		this.targetUrl = url;
		this.textPostData = postData;
		this.imageName = imgName;
		this.imagePath = imgPath;
		return;
	}
	public String send() throws Exception{
		String res = null;
		URLConnection con = null;
		try{
			// コネクション設定
			con = new URL(this.targetUrl).openConnection();
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			((HttpURLConnection)con).setRequestMethod("POST");
			con.setDoOutput(true);
			// HTTP接続
			con.connect();
			// データ送信
			File file = new File(this.imagePath);
			OutputStream os = con.getOutputStream();
			os.write(createBoundaryMessage(file.getName()).getBytes());
			os.write(getImageBytes(file));
			String endBoundary = "\r\n--" + BOUNDARY + "--\r\n";
			os.write(endBoundary.getBytes());
			os.close();
			// 結果（レスポンス）取得
			InputStream is = con.getInputStream();
			res = convertToString(is);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if( con != null )
				((HttpURLConnection)con).disconnect();
		}
		return res;
	}
	private String createBoundaryMessage(String fileName){
		StringBuffer res = new StringBuffer("--").append(BOUNDARY).append("\r\n");
		// テキストデータ部分を構築
		for( NameValuePair pd : this.textPostData ){
			res.append("Content-Disposition: form-data; name=\"").append(pd.getName()).append("\"\r\n")
				.append("\r\n").append(pd.getValue()).append("\r\n")
				.append("--").append(BOUNDARY).append("\r\n");
		}
		// 拡張子からファイルタイプ取得
		String[] fileChunks = fileName.split("\\.");
		String fileType = "image/" + fileChunks[fileChunks.length - 1];
		// ファイル名とファイルタイプを構築
		res.append("Content-Disposition: form-data; name=\"").append(this.imageName)
			.append("\"; filename=\"").append(fileName).append("\"\r\n")
			.append("Content-Type: ").append(fileType).append("\r\n\r\n");
		return res.toString();
	}
	private byte[] getImageBytes(File file){
		byte[] buf = new byte[10];
		FileInputStream fis = null;
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		// ファイルの中身を出力ストリームへ
		try{
			fis = new FileInputStream(file);
			while( fis.read(buf) > 0 ){
				bo.write(buf);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				bo.close();
			}catch(Exception e){}
			if( fis != null)
				try{
					fis.close();
				}catch(Exception e){}
				
		}
		return bo.toByteArray();
	}
	private String convertToString(InputStream stream){
		InputStreamReader streamReader = null;
		BufferedReader bufferReader = null;
		try{
			streamReader = new InputStreamReader(stream, "UTF-8");
			bufferReader = new BufferedReader(streamReader);
			StringBuilder builder = new StringBuilder();
			for( String line = null; (line = bufferReader.readLine()) != null;){
				builder.append(line).append("\n");
			}
			return builder.toString();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				stream.close();
				if(bufferReader != null)
					bufferReader.close();
			}catch(Exception e){}
		}
		return null;
	}
}
