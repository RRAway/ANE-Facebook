//////////////////////////////////////////////////////////////////////////////////////
//
//  Copyright 2012 Freshplanet (http://freshplanet.com | opensource@freshplanet.com)
//  
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//  
//    http://www.apache.org/licenses/LICENSE-2.0
//  
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//  
//////////////////////////////////////////////////////////////////////////////////////

package com.freshplanet.natExt.functions;

import android.os.Bundle;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.freshplanet.natExt.AirFacebookActivity;
import com.freshplanet.natExt.AirFacebookExtension;

public class RequestWithGraphPathFunction implements FREFunction
{
	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1)
	{	
		// Retrieve callback name
		String callbackName = null;
		try
		{
			callbackName = arg1[0].getAsString();
		}
		catch (Exception e)
		{
			Log.d(AirFacebookExtension.TAG, e.getLocalizedMessage());
		}
		
		// Retrieve graph path
		String graphPath = null;
		try
		{
			graphPath = arg1[1].getAsString();
		}
		catch (Exception e)
		{
			Log.d(AirFacebookExtension.TAG, e.getLocalizedMessage());
			return null;
		}
		
		// Retrieve parameters
		Bundle params = null;
		try
		{
			if (arg1.length > 2 && arg1[2] != null)
			{
				params = new Bundle();
				params.putString("fields", arg1[2].getAsString());
			}
		}
		catch (Exception e)
		{
			Log.d(AirFacebookExtension.TAG, e.getLocalizedMessage());
		}
		
		// Run Facebook request
		AirFacebookActivity.getInstance().request(graphPath, params, "GET", callbackName);
		
		return null;
	}
}