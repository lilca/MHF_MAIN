package jp.skd.lilca.mhf.main.listadapters.c_loud_bbs;

import java.util.Comparator;

public class BBSItemLayoutComparator implements Comparator<Object> {

	@Override
	public int compare(Object obj1, Object obj2) {
		int num1;
		int num2;

		BBSItemListLayout lay1 = (BBSItemListLayout) obj1;
		BBSItemListLayout lay2 = (BBSItemListLayout) obj2;

		num1 = Integer.parseInt((String) lay1.getTextRight());
		num2 = Integer.parseInt((String) lay2.getTextRight());
		return num2 - num1;
	}

}