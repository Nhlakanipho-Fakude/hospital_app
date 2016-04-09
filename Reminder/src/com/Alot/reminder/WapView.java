package com.Alot.reminder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WapView extends Activity {
	private WebView wv1;
	private ProgressBar progress1;
	private int position;
	private TextView wapsite;
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wap_view);
		progress1 = (ProgressBar) findViewById(R.id.progressBar1);
		wv1 = (WebView) findViewById(R.id.wapView);
		Intent i = getIntent();
		position = Integer.parseInt(i.getStringExtra("position"));
		wapsite = (TextView) findViewById(R.id.wapsite);
		back = (ImageView) findViewById(R.id.back);
		runLink(getUrl(position));

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				back.setImageResource(R.drawable.clicked_back);
				startActivity(new Intent(WapView.this, About.class));
				finish();
			}
		});
	}

	private class MyBrowser extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	public String getUrl(int position) {

		String[] links = { "http://hiv360.every1mobile.net/",
				"http://www.hivsa.com/", "http://www.yabonga.com/",
				"http://www.unaids.org/", "http://www.lovelife.org.za/",
				"http://www.aids.org.za/", "http://www.who.int/country/zaf/en",
				"http://www.hivguidelines.org", "http://arv.ashm.org.au/" };
		String[] title = { "HIV360", "HIVSA", "YABONGA", "UNAIDS", "LOVELIFE",
				"AIDS ORGANIZATION", "WORLD HEALTH ORG", "HIV GUIDELINES",
				"ASHM" };
		wapsite.setText(title[position]);

		return links[position];
	}

	@SuppressLint({ "InlinedApi", "NewApi", "SetJavaScriptEnabled" })
	public void runLink(String url) {

		if (CheckNetwork.isInternetAvailable(WapView.this)) {
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.setAcceptCookie(true);

			wv1.setWebChromeClient(new WebChromeClient() {
				public void onProgressChanged(WebView view, int progress) {
					progress1.setProgress(progress);
					if (progress == 100) {
						progress1.setVisibility(View.GONE);

					} else {
						progress1.setVisibility(View.VISIBLE);

					}
				}
			});
			wv1.getSettings().setLoadsImagesAutomatically(true);
			wv1.getSettings().setJavaScriptEnabled(true);
			wv1.getSettings().setBuiltInZoomControls(true);
			wv1.getSettings().setJavaScriptEnabled(true);
			wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			wv1.setWebViewClient(new MyBrowser() {
				@SuppressLint({ "NewApi", "InlinedApi" })
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					String msg = "Oh no! something is not right";
					AlertDialog.Builder builder = new AlertDialog.Builder(
							WapView.this, AlertDialog.THEME_HOLO_LIGHT);
					builder.setTitle("No Internet Connection");
					builder.setMessage(msg);
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									Intent home = new Intent(WapView.this,
											About.class);
									startActivity(home);
									finish();
								}

							});
					builder.show();
				}
			});

			wv1.loadUrl(url);
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(WapView.this,
					AlertDialog.THEME_HOLO_LIGHT);
			builder.setTitle("No Internet Connection");
			builder.setMessage("Enable your Mobile Data,Wifi Connection !");
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent home = new Intent(WapView.this, About.class);
							startActivity(home);
							finish();
						}

					});
			builder.show();
		}
	}
}
