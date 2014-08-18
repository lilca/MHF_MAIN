package jp.skd.lilca.mhf.web;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jp.skd.lilca.mhf.datastruct.BBSItem;
import jp.skd.lilca.mhf.lib.savedata.SavedData;
import jp.skd.lilca.mhf.lib.skill_value_list.StatusText;
import jp.skd.lilca.mhf.main.skill_value_list.SkillInfoIO;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class WebConnection {
	// cLoud関連
	private static final String CONTROLLER_URL		= "https://backdrop-skd.ssl-lolipop.jp/cLoud/webservice/controller.php";
	private static final String SIGNIN_FUNC			= "signin";
	private static final String SIGNOUT_FUNC		= "signout";
	// 装備無問題？関連
	private static final String CONTROLLER_MHF_URL	= "https://backdrop-skd.ssl-lolipop.jp/mhf/webservice/mhf_controller.php";
	private static final String GET_STORAGE_FUNC	= "mhf_getstorage";
	private static final String PUT_STORAGE_FUNC	= "mhf_putstorage";
	private static final String GET_BBS_FUNC		= "mhf_getbbs";
	private static final String PUT_BBS_FUNC		= "mhf_putbbs";
	private static final String DEL_BBS_ITEM_FUNC	= "mhf_deleteitem";
	private static final String CHG_IIKAMO_FUNC		= "mhf_changeiikamo";
	private static final String GET_STATUS_FUNC		= "mhf_getstatus";
	public static final String MODE_GET_BBS_ME		= "me";
	public static final String MODE_GET_BBS_ALL		= "all";
	private CookieStore cookies;
	private SkillInfoIO curSkillInfo;

	private static WebConnection instance = new WebConnection();
	public static WebConnection getInstance(){
		return instance;
	}
	private WebConnection(){
		return;
	}
	public void initialize(SkillInfoIO si){
		this.curSkillInfo	= si;
		return;
	}
	public cLoudResult connect(String id, String pwd) throws Exception{
		cLoudResult res = null;
    	// クライアント設定
    	DefaultHttpClient client = new DefaultHttpClient();
    	HttpPost post = new HttpPost(CONTROLLER_URL);
    	// 送信データ生成
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    	nameValuePair.add(new BasicNameValuePair("func", SIGNIN_FUNC));
    	nameValuePair.add(new BasicNameValuePair("email", id));
    	nameValuePair.add(new BasicNameValuePair("secret", pwd));
   		//　送信データのエンコードと送信
    	UrlEncodedFormEntity a = new UrlEncodedFormEntity(nameValuePair);
   		post.setEntity(a);
   		post.setEntity(new UrlEncodedFormEntity(nameValuePair));
  		HttpResponse response = client.execute(post);
  		// クッキー保存
  		this.cookies = client.getCookieStore();
   		// 応答データの格納
   		ByteArrayOutputStream resStream = new ByteArrayOutputStream();
   		response.getEntity().writeTo(resStream);
   		// 応答の確認
   		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
   			res = new cLoudResult(resStream.toString());
   		}
		return res;
	}
	public cLoudResult disconnect() throws Exception{
		cLoudResult res = null;
    	// クライアント設定
    	DefaultHttpClient client = new DefaultHttpClient();
    	HttpPost post = new HttpPost(CONTROLLER_URL);
    	// 送信データ生成
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    	nameValuePair.add(new BasicNameValuePair("func", SIGNOUT_FUNC));
//   	nameValuePair.add(new BasicNameValuePair("email", id));
//    	nameValuePair.add(new BasicNameValuePair("secret", pwd));
   
   		//　送信データのエンコードと送信
   		post.setEntity(new UrlEncodedFormEntity(nameValuePair));
  		HttpResponse response = client.execute(post);
  		// クッキー保存
  		this.cookies = client.getCookieStore();
   		// 応答データの格納
   		ByteArrayOutputStream resStream = new ByteArrayOutputStream();
   		response.getEntity().writeTo(resStream);
   		// 応答の確認
   		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
   			res = new cLoudResult(resStream.toString());
   		}
		return res;
	}
/*	public cLoudResult registration(String mail, String pwd, String nickname, String comment, String imgPath) throws Exception{
		cLoudResult res = null;
		// 送信データ生成
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    	nameValuePair.add(new BasicNameValuePair("email", mail));
    	nameValuePair.add(new BasicNameValuePair("secret", pwd));
    	nameValuePair.add(new BasicNameValuePair("nickname", nickname));
    	nameValuePair.add(new BasicNameValuePair("comment", comment));
    	// アイコンが指定されている
    	if(imgPath != null){
    		// クライアント設定
    		HttpMultipartClient client = new HttpMultipartClient(
    				REGISTRATION_URL,
    				nameValuePair,
    				"iconpath",
    				imgPath
    				);
    		//　送信
    		String stringResult = client.send();
   			res = new cLoudResult(stringResult);
    	}
    	else{
        	// クライアント設定
        	DefaultHttpClient client = new DefaultHttpClient();
        	HttpPost post = new HttpPost(REGISTRATION_URL);
       		//　送信データのエンコードと送信
       		post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
      		HttpResponse response = client.execute(post);
       		// 応答データの格納
       		ByteArrayOutputStream resStream = new ByteArrayOutputStream();
       		response.getEntity().writeTo(resStream);
       		// 応答の確認
       		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
       			res = new cLoudResult(resStream.toString());
       		}
    	}
		return res;
	}
*//*	public cLoudResult put(SavedData m, String key) throws Exception{
		String[] textList = {"skill01", "skill02", "skill03", "skill04", "skill05", "skill06", "skill07", "skill08", "skill09", "skill10", };
		cLoudResult res = null;
    	// クライアント設定
    	DefaultHttpClient client = new DefaultHttpClient();
    	client.setCookieStore(this.cookies);
    	HttpPost post = new HttpPost(PUT_QUERY_URL);
    	// 送信データ生成
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    	nameValuePair.add(new BasicNameValuePair("soubi", m.getAllElementsToString()));
    	ArrayList<String> ltemp = StatusText.getSkillList(curSkillInfo, m);
    	String[] ltempstr = (String[])ltemp.toArray(new String[0]);
    	nameValuePair.add(new BasicNameValuePair("comment", ""));
    	nameValuePair.add(new BasicNameValuePair("keywords", key));
    	for(int idx=0; idx<ltempstr.length; idx++){
   			nameValuePair.add(new BasicNameValuePair(textList[idx], ltempstr[idx]));
    	}
    	nameValuePair.add(new BasicNameValuePair("totaldef", ""+StatusText.getDefence(curSkillInfo, m)));
    	nameValuePair.add(new BasicNameValuePair("buki", m.getBuki().getName()));
   		//　送信データのエンコードと送信
   		post.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
  		HttpResponse response = client.execute(post);
   		// 応答データの格納
   		ByteArrayOutputStream resStream = new ByteArrayOutputStream();
   		response.getEntity().writeTo(resStream);
   		// 応答の確認
   		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
   			res = new cLoudResult(resStream.toString());
   		}
		return res;
	}
*/
	public cLoudResult putBBS(String key, String quest, String monster, String storategy, String data) throws Exception{
		String[] textList = {"skill01", "skill02", "skill03", "skill04", "skill05", "skill06", "skill07", "skill08", "skill09", "skill10", };
		cLoudResult res = null;
		// SavedData
		SavedData sd = new SavedData();
		sd.setAllElementsToString(data);
    	// クライアント設定
    	DefaultHttpClient client = new DefaultHttpClient();
    	client.setCookieStore(this.cookies);
    	HttpPost post = new HttpPost(CONTROLLER_MHF_URL);
    	// 送信データ生成
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    	nameValuePair.add(new BasicNameValuePair("soubi", data));
    	ArrayList<String> ltemp = StatusText.getSkillList(curSkillInfo.getData(), sd);
    	String[] ltempstr = (String[])ltemp.toArray(new String[0]);
    	// パラメータ
    	nameValuePair.add(new BasicNameValuePair("func", PUT_BBS_FUNC));
    	nameValuePair.add(new BasicNameValuePair("keywords", key));
    	nameValuePair.add(new BasicNameValuePair("quest", quest));
    	nameValuePair.add(new BasicNameValuePair("monster", monster));
    	nameValuePair.add(new BasicNameValuePair("storategy", storategy));
    	nameValuePair.add(new BasicNameValuePair("weapon", sd.getBuki().getType()));
    	for(int idx=0; idx<ltempstr.length; idx++){
   			nameValuePair.add(new BasicNameValuePair(textList[idx], ltempstr[idx]));
    	}
    	nameValuePair.add(new BasicNameValuePair("totaldef", ""+StatusText.getDefence(curSkillInfo.getData(), sd)));
    	nameValuePair.add(new BasicNameValuePair("buki", sd.getBuki().getName()));
   		//　送信データのエンコードと送信
   		post.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
  		HttpResponse response = client.execute(post);
   		// 応答データの格納
   		ByteArrayOutputStream resStream = new ByteArrayOutputStream();
   		response.getEntity().writeTo(resStream);
   		// 応答の確認
   		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
   			res = new cLoudResult(resStream.toString());
   		}
		return res;
	}
/*	public cLoudResult getList() throws Exception{
		cLoudResult res = null;
    	// クライアント設定
    	DefaultHttpClient client = new DefaultHttpClient();
    	client.setCookieStore(this.cookies);
    	HttpPost post = new HttpPost(LIST_QUERY_URL);
    	// 送信データ生成
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    	nameValuePair.add(new BasicNameValuePair("dummy", "DUMMY"));
   		//　送信データのエンコードと送信
   		post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
  		HttpResponse response = client.execute(post);
   		// 応答データの格納
   		ByteArrayOutputStream resStream = new ByteArrayOutputStream();
   		response.getEntity().writeTo(resStream);
   		// 応答の確認
   		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
   			res = new cLoudResult(resStream.toString());
   		}
		return res;
	}
*/	public cLoudResult getBBS(WherePart wp) throws Exception{
		cLoudResult res = null;
    	// クライアント設定
    	DefaultHttpClient client = new DefaultHttpClient();
    	client.setCookieStore(this.cookies);
    	HttpPost post = new HttpPost(CONTROLLER_MHF_URL);
    	// 送信データ生成
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    	// パラメータ
    	nameValuePair.add(new BasicNameValuePair("func", GET_BBS_FUNC));
    	nameValuePair.add(new BasicNameValuePair("mode", wp.getResult()));
   		//　送信データのエンコードと送信
   		post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
  		HttpResponse response = client.execute(post);
   		// 応答データの格納
   		ByteArrayOutputStream resStream = new ByteArrayOutputStream();
   		response.getEntity().writeTo(resStream);
   		// 応答の確認
   		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
   			res = new cLoudResult(resStream.toString());
   		}
		return res;
	}
	public cLoudResult getStorageList() throws Exception{
		cLoudResult res = null;
    	// クライアント設定
    	DefaultHttpClient client = new DefaultHttpClient();
    	client.setCookieStore(this.cookies);
    	HttpPost post = new HttpPost(CONTROLLER_MHF_URL);
    	// 送信データ生成
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    	nameValuePair.add(new BasicNameValuePair("func", GET_STORAGE_FUNC));
   		//　送信データのエンコードと送信
   		post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
  		HttpResponse response = client.execute(post);
   		// 応答データの格納
   		ByteArrayOutputStream resStream = new ByteArrayOutputStream();
   		response.getEntity().writeTo(resStream);
   		// 応答の確認
   		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
   			res = new cLoudResult(resStream.toString());
   		}
		return res;
	}
	public cLoudResult putStorageList(HashMap<Integer, String> lst) throws Exception{
		cLoudResult res = null;
    	// クライアント設定
    	DefaultHttpClient client = new DefaultHttpClient();
    	client.setCookieStore(this.cookies);
    	HttpPost post = new HttpPost(CONTROLLER_MHF_URL);
    	// 送信データ生成
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    	nameValuePair.add(new BasicNameValuePair("func", PUT_STORAGE_FUNC));
    	Set<Integer> keySet = lst.keySet();
    	Iterator<Integer> keyIte = keySet.iterator();
    	while(keyIte.hasNext()){
    		int key		= (Integer)keyIte.next();
    		String val	= (String)lst.get(key);
    		nameValuePair.add(new BasicNameValuePair("idx"+key, val));
    	}
   		//　送信データのエンコードと送信
   		post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
  		HttpResponse response = client.execute(post);
   		// 応答データの格納
   		ByteArrayOutputStream resStream = new ByteArrayOutputStream();
   		response.getEntity().writeTo(resStream);
   		// 応答の確認
   		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
   			res = new cLoudResult(resStream.toString());
   		}
		return res;
	}
	public cLoudResult deleteBBSItem(String itemid) throws Exception{
		cLoudResult res = null;
    	// クライアント設定
    	DefaultHttpClient client = new DefaultHttpClient();
    	client.setCookieStore(this.cookies);
    	HttpPost post = new HttpPost(CONTROLLER_MHF_URL);
    	// 送信データ生成
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    	nameValuePair.add(new BasicNameValuePair("func", DEL_BBS_ITEM_FUNC));
    	nameValuePair.add(new BasicNameValuePair("itemid", itemid));
   		//　送信データのエンコードと送信
   		post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
  		HttpResponse response = client.execute(post);
   		// 応答データの格納
   		ByteArrayOutputStream resStream = new ByteArrayOutputStream();
   		response.getEntity().writeTo(resStream);
   		// 応答の確認
   		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
   			res = new cLoudResult(resStream.toString());
   		}
		return res;
	}
	public cLoudResult changeIikamo(boolean checked, BBSItem item) throws Exception{
		cLoudResult res = null;
    	// クライアント設定
    	DefaultHttpClient client = new DefaultHttpClient();
    	client.setCookieStore(this.cookies);
    	HttpPost post = new HttpPost(CONTROLLER_MHF_URL);
    	// 送信データ生成
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    	// パラメータ
    	nameValuePair.add(new BasicNameValuePair("func", CHG_IIKAMO_FUNC));
    	if( checked )
    		nameValuePair.add(new BasicNameValuePair("checked", "1"));
    	else
    		nameValuePair.add(new BasicNameValuePair("checked", "0"));
    	nameValuePair.add(new BasicNameValuePair("bbsid", ""+item.id));
   		//　送信データのエンコードと送信
   		post.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
  		HttpResponse response = client.execute(post);
   		// 応答データの格納
   		ByteArrayOutputStream resStream = new ByteArrayOutputStream();
   		response.getEntity().writeTo(resStream);
   		// 応答の確認
   		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
   			res = new cLoudResult(resStream.toString());
   		}
		return res;
	}
	public cLoudResult getStatus() throws Exception{
		cLoudResult res = null;
    	// クライアント設定
    	DefaultHttpClient client = new DefaultHttpClient();
    	client.setCookieStore(this.cookies);
    	HttpPost post = new HttpPost(CONTROLLER_MHF_URL);
    	// 送信データ生成
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    	// パラメータ
    	nameValuePair.add(new BasicNameValuePair("func", GET_STATUS_FUNC));
   		//　送信データのエンコードと送信
   		post.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
  		HttpResponse response = client.execute(post);
   		// 応答データの格納
   		ByteArrayOutputStream resStream = new ByteArrayOutputStream();
   		response.getEntity().writeTo(resStream);
   		// 応答の確認
   		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
   			res = new cLoudResult(resStream.toString());
   		}
		return res;
	}
}
