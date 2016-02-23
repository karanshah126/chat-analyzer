package com.finalyear.eventful;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class RadioButtonAlert extends DialogFragment {

	static CharSequence[] list  = {"Low(<=5)","Medium(6-15)","High(16+)"};
	int selection = 0;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle("Select the approximate no. of active users:").setSingleChoiceItems(list, 0, new OnClickListener() {
			public void onClick(DialogInterface dialog, int arg) {
				switch (arg) {
				case 0:
					selection = 0;
					break;
				case 1:
					selection = 1;
					break;
				case 2:
					selection = 2;
					break;
				}
			}
		}).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		return adb.create();
	}

		public int getSelection(){
			return selection;
		}
}
