package com.freshplanet.natExt.functions;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.freshplanet.natExt.AirFacebookActivity;
import com.freshplanet.natExt.AirFacebookExtension;

public class GetAccessTokenFunction implements FREFunction
{
	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1)
	{
		try
		{
			return FREObject.newObject(AirFacebookActivity.getInstance().accessToken());
		}
		catch (Exception e)
		{
			Log.d(AirFacebookExtension.TAG, e.getLocalizedMessage());
			return null;
		}
	}

}
