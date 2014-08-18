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
		// ���[�h
		if(this.mode.equals("me"))
			res += "WHERE s.memberid=m.memberid AND s.memberid=\"%s\"";
		else
		if(this.mode.equals("all"))
			res += "WHERE s.memberid=m.memberid";
		// �N�G�X�g���x��
		if(!this.level.equals(""))
			res += " AND quest=\""+this.level+"\"";
		// �����X�^�[���x��
		if(!this.monster.equals(""))
			res += " AND monster=\""+this.monster+"\"";
		// �헪
		if(!this.storategy.equals(""))
			res += " AND storategy=\""+this.storategy+"\"";
		// ����
		if(this.weapon.equals("���m"))
			res += " AND NOT (weapon=\"\" OR weapon=\"���C�g�{�E�K��\" OR weapon=\"�w�r�B�{�E�K��\" OR weapon=\"�|\")";
		else
		if(this.weapon.equals("�K���i�["))
			res += " AND (weapon=\"���C�g�{�E�K��\" OR weapon=\"�w�r�B�{�E�K��\" OR weapon=\"�|\")";
		else
		if(!this.weapon.equals(""))
			res += " AND weapon=\""+this.weapon+"\"";
		// �L�[���[�h
		if(!this.keyword.equals(""))
			res += " AND keywords LIKE \"%%"+this.keyword+"%%\"";
		return res;
	}
}
