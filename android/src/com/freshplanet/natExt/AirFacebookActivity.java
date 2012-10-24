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

package com.freshplanet.natExt;

import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookActivity;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.Facebook;

public class AirFacebookActivity extends FacebookActivity
{
	private static AirFacebookActivity _instance;
	
	public static AirFacebookActivity getInstance()
	{
		return _instance;
	}
	
	/**
	 * Facebook object. Used for everything not related to session management (for now).
	 */
	public Facebook facebook;
	
	/**
	 * Facebook application ID.
	 */
	public String appID;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Save app ID
		Bundle values = this.getIntent().getExtras();
		appID = values.getString("appID");
		
		// Create Facebook object
		facebook = new Facebook(appID);
		
		// Save activity instance
		_instance = this;
	}
	
	/**
	 * Start Facebook authentication flow. The session can only be opened
	 * with read permissions. Use reauthorize to ask for publish permissions.
	 * 
	 * @param permissions the permissions list, cannot be null. Must contain
	 * only read permissions.
	 */
	public void login(List<String> permissions)
	{
		openSessionForRead(appID, permissions);
	}
	
	/**
	 * Reauthorize the current session with additional permissions.
	 * 
	 *  @param permissions the permissions list, cannot be null. Must contain
	 *  only read or publish permissions depending on the second parameter.
	 *  @param publish true if publish permissions are provided, false if
	 *  read permissions are provided.
	 */
	public void reauthorize(List<String> permissions, Boolean publish)
	{
		Session session = getSession();
		
		if (session != null)
		{
			Session.ReauthorizeRequest request = new Session.ReauthorizeRequest(this, permissions);
			if (publish) session.reauthorizeForPublish(request);
			else session.reauthorizeForRead(request);
		}
		else
		{
			Log.d(AirFacebookExtension.TAG, "Can't reauthorize: no Facebook session!");
		}
	}
	
	/**
	 * Close Facebook session.
	 */
	public void logout()
	{
		closeSessionAndClearTokenInformation();
	}
	
	/**
	 * Returns true if a Facebook session is open, false otherwise.
	 */
	public Boolean isSessionValid()
	{
		return isSessionOpen();
	}
	
	/**
	 * Returns the current session's access token.
	 */
	public String accessToken()
	{
		return getAccessToken();
	}
	
	/**
	 * Returns the current session's expiration date.
	 */
	public Date expirationDate()
	{
		return getExpirationDate();
	}
	
	/**
	 * Run a Facebook request.
	 * 
	 * @param graphPath the graph path to retrieve, create or delete
	 * @param parameters additional parameters to pass along with the Graph API request
	 * @param httpMethod the HTTP method to use for the request (GET, POST or DELETE), or null
	 * for default (GET)
	 * @param callbackName the callback name that will be sent back to the Actionscript API
	 */
	public void request(String graphPath, Bundle parameters, String httpMethod, String callbackName)
	{
		// HTTP Method
		HttpMethod method = HttpMethod.GET;
		if (httpMethod == "POST") method = HttpMethod.POST;
		else if (httpMethod == "DELETE") method = HttpMethod.DELETE;
		
		// Callback
		final String actionScriptCallback = callbackName;
		Request.Callback callback = new Request.Callback()
		{	
			@Override
			public void onCompleted(Response arg0)
			{
				String result;
				if (arg0.getError() != null) result = arg0.getError().getMessage();
				else result = arg0.getGraphObjectList().getInnerJSONArray().toString();
				if (actionScriptCallback != null)
				{
					AirFacebookExtension.context.dispatchStatusEventAsync(actionScriptCallback, result);
				}
			}
		};
		
		// Run request
		Request request = new Request(getSession(), graphPath, parameters, method, callback);
		request.executeAsync();
	}
	
	@Override
	protected void onSessionStateChange(SessionState state, Exception exception)
	{
		String eventName = null;
		
		if (state.equals(SessionState.OPENED))
		{
			eventName = "USER_LOGGED_IN";
		}
		else if (state.equals(SessionState.CLOSED))
		{
			eventName = "USER_LOGGED_OUT";
		}
		else if (state.equals(SessionState.CLOSED_LOGIN_FAILED))
		{
			eventName = "USER_LOG_IN_ERROR";
		}
		
		if (eventName != null)
		{
			AirFacebookExtension.context.dispatchStatusEventAsync(eventName, "OK");
		}
		
		if (exception != null)
		{
			Log.d(AirFacebookExtension.TAG, exception.getLocalizedMessage());
		}
	}
}