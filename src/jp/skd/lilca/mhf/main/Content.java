package jp.skd.lilca.mhf.main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import jp.skd.lilca.mhf.lib.savedata.SavedData;

import android.app.Activity;

/**
 * 
 * @author mise
 *
 */
public class Content {
	// セーブデータファイル名リスト
	public final static String[] SAVE_FILE_LIST = {
		"save.txt",
		"save2.txt",
		"save3.txt",
		"save4.txt",
		"save5.txt",
		"save6.txt",
		"save7.txt",
		"save8.txt",
		"save9.txt",
		"save10.txt",
		"save11.txt",
		"save12.txt",
		"save13.txt",
		"save14.txt",
		"save15.txt",
		"save16.txt",
		"save17.txt",
		"save18.txt",
		"save19.txt",
		"save20.txt",
	};
	/**
	 * 
	 */
	private Activity owner;
	/**
	 * 
	 */
	private String conentName;
	/**
	 * 
	 */
	private SavedData cur;

	/**
	 * 
	 * @param own
	 * @param name
	 * @throws Exception 
	 */
	public Content(Activity own, String name){
		this.owner = own;
		this.conentName = name;
		try{
			this.cur = new SavedData();
		}catch(Exception e){
			e.printStackTrace();
		}
		this.check();
		return;
	}
	/**
	 * 
	 */
	private void check(){
		try{
			this.getReadBuffer();
		}catch(Exception e){
			this.save();
			return;
		}
		return;
	}
	/**
	 * 
	 * @return
	 */
	public boolean read(){
		try{
			// オープン
			BufferedReader br = this.getReadBuffer();
			// 読み取り
			for(int idx=0; idx<SavedData.DATA_SIZE; idx++){
				String line;
				try{
					line = br.readLine();
				}catch(Exception e){
					line = "";
				}
				// 全角英字を半角へ
				if( idx == SavedData.ID_REQ_SKILLS )
					line = line.replaceAll("ＵＰ", "UP").replaceAll("ＤＯＷＮ", "DOWN");
				// 格納
				this.cur.setElement(idx, line);
			}
			// クローズ
			br.close();
			// メモリへの変換
			this.cur.toMemory();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public BufferedReader getReadBuffer() throws Exception{
		InputStream input;
		input = this.owner.openFileInput(this.conentName);
		BufferedReader br = new BufferedReader(new InputStreamReader(input,"UTF-8"));
		return br;
	}
	/**
	 * 
	 * @return
	 */
	public boolean save(){
		// Csvへの変換
		this.cur.toCsv();
		try{
			// オープン
			PrintWriter bw = getWriteBuffer();
			// 書き込み
			for(int idx=0; idx<SavedData.DATA_SIZE; idx++){
				bw.println(this.cur.getElement(idx));
			}
			// クローズ
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private PrintWriter getWriteBuffer() throws Exception{
		OutputStream output;
		output = this.owner.openFileOutput(this.conentName, Activity.MODE_WORLD_WRITEABLE);
		PrintWriter bw = new PrintWriter(new OutputStreamWriter(output,"UTF-8"));
		return bw;
	}
	/**
	 * 
	 * @return
	 */
	public SavedData getSavedData(){
		return this.cur;
	}
	/**
	 * 
	 * @param in
	 */
	public void setSavedData(SavedData in){
		this.cur = in;
		return;
	}
}
