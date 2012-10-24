package com.freshplanet.natExt.functions;

import android.os.Bundle;
import android.util.Log;

import com.adobe.fre.FREArray;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.freshplanet.natExt.AirFacebookActivity;
import com.freshplanet.natExt.AirFacebookExtension;

/** Post an OpenGraph action. */
public class PostOGActionFunction implements FREFunction
{
	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1)
	{
		// Retrieve the graph path
		String graphPath = null;
		try
		{
			graphPath = arg1[0].getAsString();
		}
		catch (Exception e)
		{
			Log.d(AirFacebookExtension.TAG, e.getLocalizedMessage());
			return null;
		}
		
		// Retrieve the action parameters
		Bundle params = new Bundle();
		FREArray keyArray = (FREArray)arg1[1];
		FREArray valueArray = (FREArray)arg1[2];
		long arrayLength;
		try
		{
			arrayLength = keyArray.getLength();
			String key;
			String value;
			for (int i = 0; i < arrayLength; i++)
			{
				key =  keyArray.getObjectAt((long)i).getAsString();
				value = valueArray.getObjectAt((long)i).getAsString();
				params.putString(key, value);
			}
		}
		catch (Exception e)
		{
			Log.d(AirFacebookExtension.TAG, e.getLocalizedMessage());
		}
		
		// Retrieve the callback name
		String callbackName = null;
		try
		{
			graphPath = arg1[3].getAsString();
		}
		catch (Exception e)
		{
			Log.d(AirFacebookExtension.TAG, e.getLocalizedMessage());
			return null;
		}
		
		// Retrieve the HTTP method
		String httpMethod = null;
		try
		{
			graphPath = arg1[4].getAsString();
		}
		catch (Exception e)
		{
			Log.d(AirFacebookExtension.TAG, e.getLocalizedMessage());
			return null;
		}
		
		// Run Facebook request
		AirFacebookActivity.getInstance().request(graphPath, params, httpMethod, callbackName);
		
		return null;
	}
}