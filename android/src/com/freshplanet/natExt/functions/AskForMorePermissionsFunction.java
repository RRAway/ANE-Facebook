package com.freshplanet.natExt.functions;

import java.util.Arrays;

import android.util.Log;

import com.adobe.fre.FREArray;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.freshplanet.natExt.AirFacebookActivity;
import com.freshplanet.natExt.AirFacebookExtension;

public class AskForMorePermissionsFunction implements FREFunction
{
	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1)
	{
		// Retrieve permissions
		FREArray permissionsArray = (FREArray)arg1[0];
		long arrayLength = 0;
		try
		{
			arrayLength = permissionsArray.getLength();
		}
		catch (Exception e)
		{
			Log.d(AirFacebookExtension.TAG, e.getLocalizedMessage());
		}
		String[] permissions = new String[(int)arrayLength];
		for (int i = 0; i < arrayLength; i++)
		{
			try
			{
				permissions[i] =  permissionsArray.getObjectAt((long) i).getAsString();
			}
			catch (Exception e)
			{
				Log.d(AirFacebookExtension.TAG, e.getLocalizedMessage());
				permissions[i] = null;
			}
		}
		
		// Retrieve permissions type (false if read, true if publish)
		Boolean publish = false;
		try
		{
			publish = arg1[1].getAsBool();
		}
		catch (Exception e)
		{
			Log.d(AirFacebookExtension.TAG, e.getLocalizedMessage());
		}
		
		// Reauthorize Facebook session
		AirFacebookActivity.getInstance().reauthorize(Arrays.asList(permissions), publish);
		
		return null;	
	}
}