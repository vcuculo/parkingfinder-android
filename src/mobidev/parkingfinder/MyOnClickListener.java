package mobidev.parkingfinder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class MyOnClickListener implements OnClickListener {

	int id;
	Context c;
	
	public MyOnClickListener(Context c,int id){
		this.id=id;
		this.c=c;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		new asyncTaskOccupyPark(c, id).execute();
		Activity a=(Activity)c;
		a.finish();
	}

}
