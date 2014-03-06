package com.jenuine.linkedin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.EnumSet;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Person;
import com.google.code.linkedinapi.schema.VisibilityType;
import com.jenuine.linkedin.LinkedinDialog.OnVerifyListener;

/**
 * @author Mukesh Kumar Yadav
 */
public class LinkedInSampleActivity extends Activity {
	Button login;
	Button share;
	EditText et;
	TextView name;
	ImageView photo;
	protected VisibilityType visibility = VisibilityType.CONNECTIONS_ONLY;
	public static final String OAUTH_CALLBACK_HOST = "litestcalback";

	final LinkedInOAuthService oAuthService = LinkedInOAuthServiceFactory
            .getInstance().createLinkedInOAuthService(
                    Config.LINKEDIN_CONSUMER_KEY,Config.LINKEDIN_CONSUMER_SECRET);
	final LinkedInApiClientFactory factory = LinkedInApiClientFactory
			.newInstance(Config.LINKEDIN_CONSUMER_KEY,
					Config.LINKEDIN_CONSUMER_SECRET);
	LinkedInRequestToken liToken;
	LinkedInApiClient client;
	LinkedInAccessToken accessToken = null;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		if( Build.VERSION.SDK_INT >= 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy); 
		}
		share = (Button) findViewById(R.id.share);
		name = (TextView) findViewById(R.id.name);
		et = (EditText) findViewById(R.id.et_share);
		login = (Button) findViewById(R.id.login);
		photo = (ImageView) findViewById(R.id.photo);

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				linkedInLogin();
			}
		});

		// share on linkedin
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				client.postShare(
						"commentTEXT",
						"title",
						"description",
						"http://androidnews.co.in/wp-content/uploads/2013/11/Android-KitKat-4.4.jpg",
						"https://lh6.googleusercontent.com/-TET-Db529fE/AAAAAAAAAAI/AAAAAAAAATY/DWZ278Gh4Z0/photo.jpg",
						visibility);
				// currentStatus.setText("");
						
				/*String share = et.getText().toString();
				if (null != share && !share.equalsIgnoreCase("")) {
					OAuthConsumer consumer = new CommonsHttpOAuthConsumer(Config.LINKEDIN_CONSUMER_KEY, Config.LINKEDIN_CONSUMER_SECRET);
				    consumer.setTokenWithSecret(accessToken.getToken(), accessToken.getTokenSecret());
					DefaultHttpClient httpclient = new DefaultHttpClient();
					HttpPost post = new HttpPost("https://api.linkedin.com/v1/people/~/shares");
					try {
						consumer.sign(post);
					} catch (OAuthMessageSignerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OAuthExpectationFailedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OAuthCommunicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // here need the consumer for sign in for post the share
					post.setHeader("content-type", "text/XML");
					String myEntity = "<share><comment>"+ share +"</comment><visibility><code>anyone</code></visibility></share>";
					try {
						post.setEntity(new StringEntity(myEntity));
						org.apache.http.HttpResponse response = httpclient.execute(post);
						Toast.makeText(LinkedInSampleActivity.this,
								"Shared sucessfully", Toast.LENGTH_SHORT).show();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					Toast.makeText(LinkedInSampleActivity.this,
							"Please enter the text to share",
							Toast.LENGTH_SHORT).show();
				}
				
				String share = et.getText().toString();
				if (null != share && !share.equalsIgnoreCase("")) {
					client = factory.createLinkedInApiClient(accessToken);
					client.postNetworkUpdate(share);
					et.setText("");
					Toast.makeText(LinkedInSampleActivity.this,
							"Shared sucessfully", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(LinkedInSampleActivity.this,
							"Please enter the text to share",
							Toast.LENGTH_SHORT).show();
				}
				*/
				
			}
		});
	}

	private void linkedInLogin() {
		ProgressDialog progressDialog = new ProgressDialog(
				LinkedInSampleActivity.this);

		final LinkedinDialog d = new LinkedinDialog(LinkedInSampleActivity.this,
				progressDialog);
		d.show();

		// set call back listener to get oauth_verifier value
		d.setVerifierListener(new OnVerifyListener() {
			@Override
			public void onVerify(String verifier) {
				try {
					Log.i("LinkedinSample", "verifier: " + verifier);

					accessToken = LinkedinDialog.oAuthService
							.getOAuthAccessToken(LinkedinDialog.liToken,
									verifier);
					
					
					
					LinkedinDialog.factory.createLinkedInApiClient(accessToken);
					client = factory.createLinkedInApiClient(accessToken);
					
					
					client = factory.createLinkedInApiClient(accessToken);
					client.postNetworkUpdate("LinkedIn Android app test");
					// Person profile = client.getProfileForCurrentUser();
					Person profile = null;
					try {
						profile = client.getProfileForCurrentUser(EnumSet.of(
								ProfileField.ID, ProfileField.FIRST_NAME,
								ProfileField.EMAIL_ADDRESS, ProfileField.LAST_NAME,
								ProfileField.HEADLINE, ProfileField.INDUSTRY,
								ProfileField.PICTURE_URL, ProfileField.DATE_OF_BIRTH,
								ProfileField.LOCATION_NAME, ProfileField.MAIN_ADDRESS,
								ProfileField.LOCATION_COUNTRY));
						Log.e("create access token secret", client.getAccessToken()
								.getTokenSecret());
					} catch (NullPointerException e) {
						// TODO: handle exception
					}
					
					
					
					// client.postNetworkUpdate("Testing by Mukesh!!! LinkedIn wall post from Android app");
					Log.i("LinkedinSample",
							"ln_access_token: " + accessToken.getToken());
					Log.i("LinkedinSample",
							"ln_access_token: " + accessToken.getTokenSecret());
					Person p = client.getProfileForCurrentUser();
					name.setText("Welcome " + p.getFirstName() + " "
							+ p.getLastName());
					Toast.makeText(getApplicationContext(),profile.getEmailAddress(), Toast.LENGTH_SHORT).show();
					name.setVisibility(0);
					login.setVisibility(4);
					share.setVisibility(0);
					et.setVisibility(0);
					d.dismiss();

				} catch (Exception e) {
					Log.i("LinkedinSample", "error to get verifier");
					e.printStackTrace();
				}
			}
		});

		// set progress dialog
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(true);
		progressDialog.show();
	}
}

