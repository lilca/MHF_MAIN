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
		res[0] = getNotNullString(pre, "list_skill_preference0101", "�񕜑��x"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0102", "�̗�"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0103", "�͂�ւ�"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0104", "��"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0105", "�X�^�~�i"	,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0106", "�H��"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0107", "�H������V"	,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0108", "�Ƃ񂸂�"	,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0109", "�C�͉�"	,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills02(SharedPreferences pre, String mode){
		String[] res = new String[23];
		res[0] = getNotNullString(pre, "list_skill_preference0201", "�U��"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0202", "����U��"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0203", "���e����"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0204", "�C�p�t"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0205", "���"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0206", "�B�l"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0207", "�̏p"		,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0208", "�J"			,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0209", "�Α����U��"	,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0210", "�������U��"	,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0211", "�������U��"	,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0212", "�X�����U��"	,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0213", "�������U��"	,mode);
		res[13] = getNotNullString(pre, "list_skill_preference0214", "�f�H"		,mode);
		res[14] = getNotNullString(pre, "list_skill_preference0215", "���ߒZ�k"	,mode);
		res[15] = getNotNullString(pre, "list_skill_preference0216", "����J��"	,mode);
		res[16] = getNotNullString(pre, "list_skill_preference0217", "�����U��"	,mode);
		res[17] = getNotNullString(pre, "list_skill_preference0218", "����"		,mode);
		res[18] = getNotNullString(pre, "list_skill_preference0219", "��M"		,mode);
		res[19] = getNotNullString(pre, "list_skill_preference0220", "�Ɍ�"		,mode);
		res[20] = getNotNullString(pre, "list_skill_preference0221", "�t��"		,mode);
		res[21] = getNotNullString(pre, "list_skill_preference0222", "��C�T"		,mode);	// add 2013/6/27
		res[22] = getNotNullString(pre, "list_skill_preference0223", "�{"		,mode);	// add 2013/6/27
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills03(SharedPreferences pre, String mode){
		String[] res = new String[5];
		res[0] = getNotNullString(pre, "list_skill_preference0301", "�h��"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0302", "�K�[�h���\"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0303", "�����h��"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0304", "������"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0305", "����"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills04(SharedPreferences pre, String mode){
		String[] res = new String[13];
		res[0] = getNotNullString(pre, "list_skill_preference0401", "�a�ꖡ"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0402", "��"			,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0403", "�����t"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0404", "������"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0405", "�ғŌ�"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0406", "��჌�"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0407", "������"		,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0408", "�Ή���"		,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0409", "������"		,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0410", "���_��"		,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0411", "�X����"		,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0412", "������"		,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0413", "���p"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills05(SharedPreferences pre, String mode){
		String[] res = new String[19];
		res[0] = getNotNullString(pre, "list_skill_preference0501", "�ʏ�e����"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0502", "�ђʒe����"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0503", "�U�e����"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0504", "�ʏ�e�ǉ�"	,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0505", "�ђʒe�ǉ�"	,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0506", "�U�e�ǉ�"	,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0507", "���e�ǉ�"	,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0508", "�g�U�e�ǉ�"	,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0509", "�ŕr�ǉ�"	,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0510", "��ვr�ǉ�"	,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0511", "�����r�ǉ�"	,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0512", "���U��"		,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0513", "�A��"		,mode);
		res[13] = getNotNullString(pre, "list_skill_preference0514", "���U"		,mode);
		res[14] = getNotNullString(pre, "list_skill_preference0515", "����"		,mode);
		res[15] = getNotNullString(pre, "list_skill_preference0516", "�����ˌ�"	,mode);
		res[16] = getNotNullString(pre, "list_skill_preference0517", "�e����"		,mode);
		res[17] = getNotNullString(pre, "list_skill_preference0518", "����"		,mode);
		res[18] = getNotNullString(pre, "list_skill_preference0519", "����"		,mode);	// add 2013/6/27
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills06(SharedPreferences pre, String mode){
		String[] res = new String[6];
		res[0] = getNotNullString(pre, "list_skill_preference0601", "�S�ϐ�UP"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0602", "�Αϐ�"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0603", "���ϐ�"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0604", "�X�ϐ�"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0605", "���ϐ�"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0606", "���ϐ�"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills07(SharedPreferences pre, String mode){
		String[] res = new String[10];
		res[0] = getNotNullString(pre, "list_skill_preference0701", "��"			,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0702", "���"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0703", "����"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0704", "�C��"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0705", "�E�L"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0706", "�ϐ�"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0707", "����"		,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0708", "�Ζh��DOWN"	,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0709", "�Ϗ�Ԉُ�"	,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0710", "�ϐ�"		,mode);	// add 2013/6/27
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills08(SharedPreferences pre, String mode){
		String[] res = new String[13];
		res[0] = getNotNullString(pre, "list_skill_preference0801", "���o�ی�"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0802", "����"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0803", "�ϐk"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0804", "��𐫔\"	,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0805", "�Ϗ�"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0806", "�ϊ�"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0807", "���ݖ���"	,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0808", "�n�`"		,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0809", "�R��"		,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0810", "�Ђ�߂�"	,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0811", "�󂯐g"		,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0812", "����"		,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0813", "�x��"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills09(SharedPreferences pre, String mode){
		String[] res = new String[16];
		res[0] = getNotNullString(pre, "list_skill_preference0901", "�L��"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference0902", "���ʎ���"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference0903", "�C�܂���"	,mode);
		res[3] = getNotNullString(pre, "list_skill_preference0904", "����"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference0905", "���Ă�"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference0906", "�ނ�"		,mode);
		res[6] = getNotNullString(pre, "list_skill_preference0907", "����������"	,mode);
		res[7] = getNotNullString(pre, "list_skill_preference0908", "�B���p"		,mode);
		res[8] = getNotNullString(pre, "list_skill_preference0909", "����"		,mode);
		res[9] = getNotNullString(pre, "list_skill_preference0910", "�J�������l"	,mode);
		res[10] = getNotNullString(pre, "list_skill_preference0911", "�����ݒu"	,mode);
		res[11] = getNotNullString(pre, "list_skill_preference0912", "�i�C�t�g��"	,mode);
		res[12] = getNotNullString(pre, "list_skill_preference0913", "�����t"		,mode);
		res[13] = getNotNullString(pre, "list_skill_preference0914", "��l"		,mode);
		res[14] = getNotNullString(pre, "list_skill_preference0915", "�ە�"		,mode);
		res[15] = getNotNullString(pre, "list_skill_preference0916", "�S�r"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills10(SharedPreferences pre, String mode){
		String[] res = new String[3];
		res[0] = getNotNullString(pre, "list_skill_preference1001", "�痢��"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference1002", "�n�}"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference1003", "�C�z"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills11(SharedPreferences pre, String mode){
		String[] res = new String[4];
		res[0] = getNotNullString(pre, "list_skill_preference1101", "�������W"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference1102", "�������"	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference1103", "�^��"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference1104", "�̎�"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills12(SharedPreferences pre, String mode){
		String[] res = new String[7];
		res[0] = getNotNullString(pre, "list_skill_preference1201", "�^�C"		,mode);
		res[1] = getNotNullString(pre, "list_skill_preference1202", "�����X�^�["	,mode);
		res[2] = getNotNullString(pre, "list_skill_preference1203", "����"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference1204", "�ߊl���"	,mode);
		res[4] = getNotNullString(pre, "list_skill_preference1205", "�������"	,mode);
		res[5] = getNotNullString(pre, "list_skill_preference1206", "�u���[�_�["	,mode);
		res[6] = getNotNullString(pre, "list_skill_preference1207", "�~��"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
	public static String getSkills13(SharedPreferences pre, String mode){
		String[] res = new String[11];
		res[0] = getNotNullString(pre, "list_skill_preference1301", "�Ў茕�Z"	,mode);
		res[1] = getNotNullString(pre, "list_skill_preference1302", "�o���Z"		,mode);
		res[2] = getNotNullString(pre, "list_skill_preference1303", "�匕�Z"		,mode);
		res[3] = getNotNullString(pre, "list_skill_preference1304", "�����Z"		,mode);
		res[4] = getNotNullString(pre, "list_skill_preference1305", "�ȋZ"		,mode);
		res[5] = getNotNullString(pre, "list_skill_preference1306", "��J�Z"	,mode);
		res[6] = getNotNullString(pre, "list_skill_preference1307", "���Z"		,mode);
		res[7] = getNotNullString(pre, "list_skill_preference1308", "�e���Z"		,mode);
		res[8] = getNotNullString(pre, "list_skill_preference1309", "�y�e�Z"		,mode);
		res[9] = getNotNullString(pre, "list_skill_preference1310", "�d�e�Z"		,mode);
		res[10] = getNotNullString(pre, "list_skill_preference1311", "�|�Z"		,mode);
		return CsvStringToList.scatNullSkip(res, ",");
	}
}
