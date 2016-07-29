package com.jc.android.template.presentation.wigth;

import com.jc.android.template.data.entity.ContactEntity;
import com.jc.android.template.presentation.model.ContactModel;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<ContactModel> {

	public int compare(ContactModel o1, ContactModel o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}


}
