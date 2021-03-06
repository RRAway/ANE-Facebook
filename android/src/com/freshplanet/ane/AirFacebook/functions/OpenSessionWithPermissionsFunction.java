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

package com.freshplanet.ane.AirFacebook.functions;

import android.content.Intent;

import com.adobe.fre.FREArray;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.freshplanet.ane.AirFacebook.AirFacebookExtension;
import com.freshplanet.ane.AirFacebook.LoginActivity;

public class OpenSessionWithPermissionsFunction implements FREFunction
{
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
			AirFacebookExtension.log("ERROR - " + e.getMessage());
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
				AirFacebookExtension.log("ERROR - " + e.getMessage());
				permissions[i] = null;
			}
		}
		
		// Start login activity
		Intent i = new Intent(arg0.getActivity().getApplicationContext(), LoginActivity.class);
		i.putExtra("permissions", permissions);
		arg0.getActivity().startActivity(i);
		
		return null;
	}
}