package jp.skd.lilca.mhf.web;

public class WherePart {
	private String mode;		// mode
	private String keyword;		// keywords
	private String level;		// quest
	private String monster;		// monster
	private String storategy;	// storategy
	private String weapon;		// weapon
	public WherePart(String m, String k, String lvl, String mon, String st, String w){
		this.mode		= m;
		this.keyword	= k;
		this.level		= lvl;
		this.monster	= mon;
		this.storategy	= st;
		this.weapon		= w;
		return;
	}
	public String getResult(){
		String res = "";
		// モード
		if(this.mode.equals("me"))
			res += "WHERE s.memberid=m.memberid AND s.memberid=\"%s\"";
		else
		if(this.mode.equals("all"))
			res += "WHERE s.memberid=m.memberid";
		// クエストレベル
		if(!this.level.equals(""))
			res += " AND quest=\""+this.level+"\"";
		// モンスターレベル
		if(!this.monster.equals(""))
			res += " AND monster=\""+this.monster+"\"";
		// 戦略
		if(!this.storategy.equals(""))
			res += " AND storategy=\""+this.storategy+"\"";
		// 武器
		if(this.weapon.equals("剣士"))
			res += " AND NOT (weapon=\"\" OR weapon=\"ライトボウガン\" OR weapon=\"ヘビィボウガン\" OR weapon=\"弓\")";
		else
		if(this.weapon.equals("ガンナー"))
			res += " AND (weapon=\"ライトボウガン\" OR weapon=\"ヘビィボウガン\" OR weapon=\"弓\")";
		else
		if(!this.weapon.equals(""))
			res += " AND weapon=\""+this.weapon+"\"";
		// キーワード
		if(!this.keyword.equals(""))
			res += " AND keywords LIKE \"%%"+this.keyword+"%%\"";
		return res;
	}
}
