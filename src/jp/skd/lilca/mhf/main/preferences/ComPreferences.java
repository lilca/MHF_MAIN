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
		res[0] = getNotNullString(pre, "list_skill_preference0101", "‰ñ•œ‘¬“x"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0102", "‘Ì—Í"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0103", "‚Í‚ç‚Ö‚è"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0104", "‰ñ•œ"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0105", "ƒXƒ^ƒ~ƒi"	,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0106", "H–"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0107", "H‚¢‚µ‚ñ–V"	,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0108", "‚Æ‚ñ‚¸‚ç"	,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0109", "‹C—Í‰ñ•œ"	,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills02(SharedPreferences pre, String mode){
		String[] res = new String[23];
		res[0] = getNotNullString(pre, "list_skill_preference0201", "UŒ‚"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0202", "“ÁêUŒ‚"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0203", "”š’e‹­‰»"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0204", "–Cpt"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0205", "’ê—Í"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0206", "’Bl"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0207", "‘Ìp"		,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0208", "ãJ"			,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0209", "‰Î‘®«UŒ‚"	,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0210", "…‘®«UŒ‚"	,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0211", "—‹‘®«UŒ‚"	,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0212", "•X‘®«UŒ‚"	,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0213", "—´‘®«UŒ‚"	,mode);
		res[13] = getNotNullString(pre, "list_skill_preference0214", "’fH"		,mode);
		res[14] = getNotNullString(pre, "list_skill_preference0215", "—­‚ß’Zk"	,mode);
		res[15] = getNotNullString(pre, "list_skill_preference0216", "•ŠíJ‚«"	,mode);
		res[16] = getNotNullString(pre, "list_skill_preference0217", "‘®«UŒ‚"	,mode);
		res[17] = getNotNullString(pre, "list_skill_preference0218", "„Œ‚"		,mode);
		res[18] = getNotNullString(pre, "list_skill_preference0219", "ˆê‘M"		,mode);
		res[19] = getNotNullString(pre, "list_skill_preference0220", "’ÉŒ‚"		,mode);
		res[20] = getNotNullString(pre, "list_skill_preference0221", "‹t‹«"		,mode);
		res[21] = getNotNullString(pre, "list_skill_preference0222", "ˆê•C˜T"		,mode);	// add 2013/6/27
		res[22] = getNotNullString(pre, "list_skill_preference0223", "“{"		,mode);	// add 2013/6/27
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills03(SharedPreferences pre, String mode){
		String[] res = new String[5];
		res[0] = getNotNullString(pre, "list_skill_preference0301", "–hŒä"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0302", "ƒK[ƒh«”\"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0303", "©“®–hŒä"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0304", "¶–½—Í"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0305", "”½Ë"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills04(SharedPreferences pre, String mode){
		String[] res = new String[13];
		res[0] = getNotNullString(pre, "list_skill_preference0401", "a‚ê–¡"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0402", " "			,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0403", "Œ¤‚¬t"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0404", "”šŒ‚Œ•"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0405", "–Ò“ÅŒ•"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0406", "–ƒáƒŒ•"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0407", "‡–°Œ•"		,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0408", "‰Î‰ŠŒ•"		,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0409", "…ŒƒŒ•"		,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0410", "—‹_Œ•"		,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0411", "•XŒ‹Œ•"		,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0412", "—´‰¤Œ•"		,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0413", "Œ•p"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills05(SharedPreferences pre, String mode){
		String[] res = new String[19];
		res[0] = getNotNullString(pre, "list_skill_preference0501", "’Êí’e‹­‰»"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0502", "ŠÑ’Ê’e‹­‰»"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0503", "U’e‹­‰»"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0504", "’Êí’e’Ç‰Á"	,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0505", "ŠÑ’Ê’e’Ç‰Á"	,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0506", "U’e’Ç‰Á"	,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0507", "—­’e’Ç‰Á"	,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0508", "ŠgU’e’Ç‰Á"	,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0509", "“Å•r’Ç‰Á"	,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0510", "–ƒáƒ•r’Ç‰Á"	,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0511", "‡–°•r’Ç‰Á"	,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0512", "‘•“U”"		,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0513", "˜AË"		,mode);
		res[13] = getNotNullString(pre, "list_skill_preference0514", "‘•“U"		,mode);
		res[14] = getNotNullString(pre, "list_skill_preference0515", "”½“®"		,mode);
		res[15] = getNotNullString(pre, "list_skill_preference0516", "¸–§ËŒ‚"	,mode);
		res[16] = getNotNullString(pre, "list_skill_preference0517", "’e’²‡"		,mode);
		res[17] = getNotNullString(pre, "list_skill_preference0518", "‘¬Ë"		,mode);
		res[18] = getNotNullString(pre, "list_skill_preference0519", "‘•’…"		,mode);	// add 2013/6/27
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills06(SharedPreferences pre, String mode){
		String[] res = new String[6];
		res[0] = getNotNullString(pre, "list_skill_preference0601", "‘S‘Ï«UP"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0602", "‰Î‘Ï«"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0603", "…‘Ï«"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0604", "•X‘Ï«"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0605", "—‹‘Ï«"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0606", "—´‘Ï«"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills07(SharedPreferences pre, String mode){
		String[] res = new String[10];
		res[0] = getNotNullString(pre, "list_skill_preference0701", "“Å"			,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0702", "–ƒáƒ"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0703", "‡–°"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0704", "‹Câ"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0705", "’EL"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0706", "‘Ïá"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0707", "º‘Ñ"		,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0708", "‘Î–hŒäDOWN"	,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0709", "‘Ïó‘ÔˆÙí"	,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0710", "‘ÏŒ"		,mode);	// add 2013/6/27
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills08(SharedPreferences pre, String mode){
		String[] res = new String[13];
		res[0] = getNotNullString(pre, "list_skill_preference0801", "’®Šo•ÛŒì"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0802", "•—ˆ³"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0803", "‘Ïk"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0804", "‰ñ”ğ«”\"	,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0805", "‘Ï‹"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0806", "‘ÏŠ¦"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0807", "“‚İ–³Œø"	,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0808", "’nŒ`"		,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0809", "R”»"		,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0810", "‚Ğ‚ç‚ß‚«"	,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0811", "ó‚¯g"		,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0812", "ª«"		,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0813", "Œx‰ú"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills09(SharedPreferences pre, String mode){
		String[] res = new String[16];
		res[0] = getNotNullString(pre, "list_skill_preference0901", "Lˆæ"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0902", "Œø‰Ê‘±"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0903", "‹C‚Ü‚®‚ê"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0904", "“Š±"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0905", "“÷Ä‚«"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0906", "’Ş‚è"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0907", "’²‡¬Œ÷—¦"	,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0908", "˜B‹àp"		,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0909", "‹­Œ¨"		,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0910", "“J‚«–¼l"	,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0911", "‚‘¬İ’u"	,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0912", "ƒiƒCƒtg‚¢"	,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0913", "’²‡t"		,mode);
		res[13] = getNotNullString(pre, "list_skill_preference0914", "ël"		,mode);
		res[14] = getNotNullString(pre, "list_skill_preference0915", "ŒÛ•‘"		,mode);
		res[15] = getNotNullString(pre, "list_skill_preference0916", "“S˜r"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills10(SharedPreferences pre, String mode){
		String[] res = new String[3];
		res[0] = getNotNullString(pre, "list_skill_preference1001", "ç—¢Šá"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference1002", "’n}"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference1003", "‹C”z"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills11(SharedPreferences pre, String mode){
		String[] res = new String[4];
		res[0] = getNotNullString(pre, "list_skill_preference1101", "‚‘¬ûW"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference1102", "”‚¬æ‚è"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference1103", "‰^”À"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference1104", "Ìæ"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills12(SharedPreferences pre, String mode){
		String[] res = new String[7];
		res[0] = getNotNullString(pre, "list_skill_preference1201", "‰^‹C"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference1202", "ƒ‚ƒ“ƒXƒ^["	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference1203", "ˆ³—Í"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference1204", "•ßŠlãè"	,mode);
		res[4] = getNotNullString(pre, "list_skill_preference1205", "‚¢‚½‚í‚è"	,mode);
		res[5] = getNotNullString(pre, "list_skill_preference1206", "ƒuƒŠ[ƒ_["	,mode);
		res[6] = getNotNullString(pre, "list_skill_preference1207", "‹~‰‡"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills13(SharedPreferences pre, String mode){
		String[] res = new String[11];
		res[0] = getNotNullString(pre, "list_skill_preference1301", "•ĞèŒ•‹Z"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference1302", "‘oŒ•‹Z"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference1303", "‘åŒ•‹Z"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference1304", "‘¾“‹Z"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference1305", "’È‹Z"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference1306", "ë—Â“J‹Z"	,mode);
		res[6] = getNotNullString(pre, "list_skill_preference1307", "‘„‹Z"		,mode);
		res[7] = getNotNullString(pre, "list_skill_preference1308", "e‘„‹Z"		,mode);
		res[8] = getNotNullString(pre, "list_skill_preference1309", "Œye‹Z"		,mode);
		res[9] = getNotNullString(pre, "list_skill_preference1310", "de‹Z"		,mode);
		res[10] = getNotNullString(pre, "list_skill_preference1311", "‹|‹Z"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
}
