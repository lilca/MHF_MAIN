package jp.skd.lilca.mhf.datastruct;

import jp.skd.lilca.mhf.lib.tools.CsvStringToList;

public class BBSItem {
	public String id;
	public String soubi;
	public String nickname;
	public String reply_post_id;
	public String keywords;
	public String quest;
	public String monster;
	public String storategy;
	public String weapon;
	public String skill01;
	public String skill02;
	public String skill03;
	public String skill04;
	public String skill05;
	public String skill06;
	public String skill07;
	public String skill08;
	public String skill09;
	public String skill10;
	public String totaldef;
	public String buki;
	public String created;
	public String delflag;		// add 2013/1/13
	public String iikamo_cnt;	// add 2013/4/3
	public String iikamo_me;	// add 2013/4/3
	
	public BBSItem(String sepdata){
		String[] temp = CsvStringToList.split(sepdata, ";sep;");
		this.id				= temp[0];
		this.soubi			= temp[1];
		this.nickname		= temp[2];
		this.reply_post_id	= temp[3];
		this.keywords		= temp[4];
		this.quest			= temp[5];
		this.monster		= temp[6];
		this.storategy		= temp[7];
		this.weapon			= temp[8];
		this.skill01		= temp[9];
		this.skill02		= temp[10];
		this.skill03		= temp[11];
		this.skill04		= temp[12];
		this.skill05		= temp[13];
		this.skill06		= temp[14];
		this.skill07		= temp[15];
		this.skill08		= temp[16];
		this.skill09		= temp[17];
		this.skill10		= temp[18];
		this.totaldef		= temp[19];
		this.buki			= temp[20];
		this.created		= temp[21];
		// 削除フラグ 2013/1/13
		if( temp.length <= 22 )
			return;
		this.delflag	= temp[22];
		// 削除フラグ 2013/4/3
		if( temp.length <= 24 )
			return;
		this.iikamo_cnt	= temp[23];
		this.iikamo_me	= temp[24];
		return;
	}
	public String getSkillsNameList(){
		return
			this.skill01+", "+
			this.skill02+", "+
			this.skill03+", "+
			this.skill04+", "+
			this.skill05+", "+
			this.skill06+", "+
			this.skill07+", "+
			this.skill08+", "+
			this.skill09+", "+
			this.skill10;
	}
}
