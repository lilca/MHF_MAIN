package jp.skd.lilca.mhf.main.skill_value_list;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import jp.skd.lilca.mhf.lib.skill_value_list.SkillValueList;
import jp.skd.lilca.mhf.lib.skill_value_list.SkillInfoData;
import jp.skd.lilca.mhf.lib.tools.CsvStringToList;
import jp.skd.lilca.mhf.main.R;

import android.content.res.Resources;
import android.util.Log;

public class SkillInfoIO {

	// 内容クラス
	private SkillInfoData contents;

	public SkillInfoIO(Resources r){
		ArrayList<String> list = new ArrayList<String>();
		HashMap<String, String> boot2skill	= new HashMap<String, String>();
		HashMap<String, Integer> boot2value	= new HashMap<String, Integer>();
		HashMap<String, Boolean> boot2rank	= new HashMap<String, Boolean>();
		ArrayList<String> doubleList = new ArrayList<String>();
		HashMap<String, String> skill2boots	= new HashMap<String, String>();

    	String line = "";
    	try{
        	// ストリームを開く
        	InputStream input = r.openRawResource(R.raw.skillinfo);
        	BufferedReader br = new BufferedReader(new InputStreamReader(input, "Shift_JIS"));
        	
        	String preName = "";
        	int preVal = 0;
        	for(line=br.readLine(); line!=null; line=br.readLine()){
        		list.add(line);
        		String[] data = CsvStringToList.split(line, ",");
        		String name		= data[0];
        		String valStr	= data[1];
        		String skill	= data[2];
        		// 空行をスキップ
        		if( line.replaceAll(" ", "").equals("") )
        			continue;
        		// 2重のとき
        		if( valStr.equals("2重") ){
        			doubleList.add(line);
        		}
        		else{
        			boot2skill.put(skill, name);
        			boot2value.put(skill, Integer.parseInt(valStr));
        			// スキルの最上位以外
        			if(preName.equals(name))
        				boot2rank.put(skill, SkillValueList.isRankUpable(name));
        			// スキルの最上位
        			else{
        				boot2rank.put(skill, false);
        				// １つ前の値が負だったら
        				if( preVal < 0 )
            				boot2rank.put(preName, false);
        			}
           			// 発動リスト
           			if( skill2boots.get(name) == null ){
           				skill2boots.put(name, skill);
           			}
           			else{
           				skill2boots.put(name, skill2boots.get(name)+":"+skill);
           			}
            		preName = name;
            		preVal	= Integer.parseInt(valStr);
        		}
        	}
        	// 最後のスキルもfalse
			boot2rank.put(preName, false);        	
    	}catch(Exception e){
    		Log.d("mise", line);
    		e.printStackTrace();
    	}
    	// 内容クラス生成
		contents = new SkillInfoData(list, boot2skill, boot2value, boot2rank, doubleList, skill2boots);
    	
		return;
	}
	public SkillInfoData getData(){
		return this.contents.getData();
	}
	public ArrayList<String> getSkillList(){
		return this.contents.getSkillList();
	}
	public String getSkillName(String bootSkillName){
		return this.contents.getSkillName(bootSkillName);
	}
	public Integer getSkillValue(String bootSkillName){
		return this.contents.getSkillValue(bootSkillName);
	}
	public Boolean getSkillRank(String bootSkillName){
		return this.contents.getSkillRank(bootSkillName);
	}
}
