package jp.skd.lilca.mhf.main.preferences;

import jp.skd.lilca.mhf.lib.tools.CsvStringToList;
import android.content.SharedPreferences;

public class ComPreferences {

	public static final String CPF_MODE_MAIN	= "";
	public static final String CPF_MODE_BUKI	= "_buki";
	public static final String CPF_MODE_SOUBI	= "_soubi";
	public static final String CPF_MODE_SKILL	= "_skill";
	public static final String CPF_MODE_KAFUS	= "_kafus";
	private static String getNotNullString(SharedPreferences pre, String key, String header, String mode){
		String temp;
		if( mode.equals(CPF_MODE_MAIN) ){
			temp = pre.getString(key+CPF_MODE_MAIN, "");
			if( temp == null )
				return "0";
			return temp;
		}
		else
		if( mode.equals(CPF_MODE_BUKI) || mode.equals(CPF_MODE_SOUBI) || mode.equals(CPF_MODE_SKILL) || mode.equals(CPF_MODE_KAFUS) ){
			temp = pre.getString(key+mode, "0");
			if( temp.equals("") || temp.equals("0"))
				return "";
			return header+temp;
		}
		return "";
	}
	public static String getSkills01(SharedPreferences pre, String mode){
		String[] res = new String[9];
		res[0] = getNotNullString(pre, "list_skill_preference0101", "回復速度"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0102", "体力"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0103", "はらへり"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0104", "回復"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0105", "スタミナ"	,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0106", "食事"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0107", "食いしん坊"	,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0108", "とんずら"	,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0109", "気力回復"	,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills02(SharedPreferences pre, String mode){
		String[] res = new String[23];
		res[0] = getNotNullString(pre, "list_skill_preference0201", "攻撃"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0202", "特殊攻撃"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0203", "爆弾強化"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0204", "砲術師"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0205", "底力"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0206", "達人"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0207", "体術"		,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0208", "絆"			,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0209", "火属性攻撃"	,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0210", "水属性攻撃"	,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0211", "雷属性攻撃"	,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0212", "氷属性攻撃"	,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0213", "龍属性攻撃"	,mode);
		res[13] = getNotNullString(pre, "list_skill_preference0214", "断食"		,mode);
		res[14] = getNotNullString(pre, "list_skill_preference0215", "溜め短縮"	,mode);
		res[15] = getNotNullString(pre, "list_skill_preference0216", "武器捌き"	,mode);
		res[16] = getNotNullString(pre, "list_skill_preference0217", "属性攻撃"	,mode);
		res[17] = getNotNullString(pre, "list_skill_preference0218", "剛撃"		,mode);
		res[18] = getNotNullString(pre, "list_skill_preference0219", "一閃"		,mode);
		res[19] = getNotNullString(pre, "list_skill_preference0220", "痛撃"		,mode);
		res[20] = getNotNullString(pre, "list_skill_preference0221", "逆境"		,mode);
		res[21] = getNotNullString(pre, "list_skill_preference0222", "一匹狼"		,mode);	// add 2013/6/27
		res[22] = getNotNullString(pre, "list_skill_preference0223", "怒"		,mode);	// add 2013/6/27
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills03(SharedPreferences pre, String mode){
		String[] res = new String[5];
		res[0] = getNotNullString(pre, "list_skill_preference0301", "防御"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0302", "ガード性能"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0303", "自動防御"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0304", "生命力"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0305", "反射"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills04(SharedPreferences pre, String mode){
		String[] res = new String[13];
		res[0] = getNotNullString(pre, "list_skill_preference0401", "斬れ味"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0402", "匠"			,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0403", "研ぎ師"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0404", "爆撃剣"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0405", "猛毒剣"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0406", "麻痺剣"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0407", "睡眠剣"		,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0408", "火炎剣"		,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0409", "水激剣"		,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0410", "雷神剣"		,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0411", "氷結剣"		,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0412", "龍王剣"		,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0413", "剣術"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills05(SharedPreferences pre, String mode){
		String[] res = new String[19];
		res[0] = getNotNullString(pre, "list_skill_preference0501", "通常弾強化"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0502", "貫通弾強化"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0503", "散弾強化"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0504", "通常弾追加"	,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0505", "貫通弾追加"	,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0506", "散弾追加"	,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0507", "溜弾追加"	,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0508", "拡散弾追加"	,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0509", "毒瓶追加"	,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0510", "麻痺瓶追加"	,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0511", "睡眠瓶追加"	,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0512", "装填数"		,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0513", "連射"		,mode);
		res[13] = getNotNullString(pre, "list_skill_preference0514", "装填"		,mode);
		res[14] = getNotNullString(pre, "list_skill_preference0515", "反動"		,mode);
		res[15] = getNotNullString(pre, "list_skill_preference0516", "精密射撃"	,mode);
		res[16] = getNotNullString(pre, "list_skill_preference0517", "弾調合"		,mode);
		res[17] = getNotNullString(pre, "list_skill_preference0518", "速射"		,mode);
		res[18] = getNotNullString(pre, "list_skill_preference0519", "装着"		,mode);	// add 2013/6/27
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills06(SharedPreferences pre, String mode){
		String[] res = new String[6];
		res[0] = getNotNullString(pre, "list_skill_preference0601", "全耐性UP"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0602", "火耐性"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0603", "水耐性"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0604", "氷耐性"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0605", "雷耐性"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0606", "龍耐性"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills07(SharedPreferences pre, String mode){
		String[] res = new String[10];
		res[0] = getNotNullString(pre, "list_skill_preference0701", "毒"			,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0702", "麻痺"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0703", "睡眠"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0704", "気絶"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0705", "脱臭"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0706", "耐雪"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0707", "声帯"		,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0708", "対防御DOWN"	,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0709", "耐状態異常"	,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0710", "耐酔"		,mode);	// add 2013/6/27
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills08(SharedPreferences pre, String mode){
		String[] res = new String[13];
		res[0] = getNotNullString(pre, "list_skill_preference0801", "聴覚保護"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0802", "風圧"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0803", "耐震"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0804", "回避性能"	,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0805", "耐暑"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0806", "耐寒"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0807", "盗み無効"	,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0808", "地形"		,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0809", "審判"		,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0810", "ひらめき"	,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0811", "受け身"		,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0812", "根性"		,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0813", "警戒"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills09(SharedPreferences pre, String mode){
		String[] res = new String[16];
		res[0] = getNotNullString(pre, "list_skill_preference0901", "広域"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0902", "効果持続"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0903", "気まぐれ"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0904", "投擲"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0905", "肉焼き"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0906", "釣り"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0907", "調合成功率"	,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0908", "錬金術"		,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0909", "強肩"		,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0910", "笛吹き名人"	,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0911", "高速設置"	,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0912", "ナイフ使い"	,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0913", "調合師"		,mode);
		res[13] = getNotNullString(pre, "list_skill_preference0914", "狩人"		,mode);
		res[14] = getNotNullString(pre, "list_skill_preference0915", "鼓舞"		,mode);
		res[15] = getNotNullString(pre, "list_skill_preference0916", "鉄腕"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills10(SharedPreferences pre, String mode){
		String[] res = new String[3];
		res[0] = getNotNullString(pre, "list_skill_preference1001", "千里眼"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference1002", "地図"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference1003", "気配"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills11(SharedPreferences pre, String mode){
		String[] res = new String[4];
		res[0] = getNotNullString(pre, "list_skill_preference1101", "高速収集"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference1102", "剥ぎ取り"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference1103", "運搬"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference1104", "採取"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills12(SharedPreferences pre, String mode){
		String[] res = new String[7];
		res[0] = getNotNullString(pre, "list_skill_preference1201", "運気"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference1202", "モンスター"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference1203", "圧力"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference1204", "捕獲上手"	,mode);
		res[4] = getNotNullString(pre, "list_skill_preference1205", "いたわり"	,mode);
		res[5] = getNotNullString(pre, "list_skill_preference1206", "ブリーダー"	,mode);
		res[6] = getNotNullString(pre, "list_skill_preference1207", "救援"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills13(SharedPreferences pre, String mode){
		String[] res = new String[11];
		res[0] = getNotNullString(pre, "list_skill_preference1301", "片手剣技"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference1302", "双剣技"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference1303", "大剣技"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference1304", "太刀技"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference1305", "鎚技"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference1306", "狩猟笛技"	,mode);
		res[6] = getNotNullString(pre, "list_skill_preference1307", "槍技"		,mode);
		res[7] = getNotNullString(pre, "list_skill_preference1308", "銃槍技"		,mode);
		res[8] = getNotNullString(pre, "list_skill_preference1309", "軽銃技"		,mode);
		res[9] = getNotNullString(pre, "list_skill_preference1310", "重銃技"		,mode);
		res[10] = getNotNullString(pre, "list_skill_preference1311", "弓技"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
}
