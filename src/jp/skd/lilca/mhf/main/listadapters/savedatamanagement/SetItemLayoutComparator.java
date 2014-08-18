package jp.skd.lilca.mhf.main.listadapters.savedatamanagement;

import java.util.Comparator;

public class SetItemLayoutComparator implements Comparator<Object> {

	@Override
	public int compare(Object obj1, Object obj2) {
		int num1;
		int num2;

		SetItemListLayout lay1 = (SetItemListLayout) obj1;
		SetItemListLayout lay2 = (SetItemListLayout) obj2;

		num1 = Integer.parseInt((String) lay1.getTextRight());
		num2 = Integer.parseInt((String) lay2.getTextRight());
		return num2 - num1;
	}

}