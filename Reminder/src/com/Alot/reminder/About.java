package com.Alot.reminder;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class About extends Activity {

	private List<ListHandler> wapsites = new ArrayList<ListHandler>();
	private int Position = 0;
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		back = (ImageView) findViewById(R.id.back);
		populateListView();
		addWapsites();
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				back.setImageResource(R.drawable.clicked_back);
				startActivity(new Intent(About.this, HomeScreen.class));
				finish();
			}
		});
	}

	public void populateListView() {

		ArrayAdapter<ListHandler> adapter = new MyListAdapter();
		ListView List = (ListView) findViewById(R.id.about_listview);
		List.setAdapter(adapter);
		List.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Position = position;
				Intent i = new Intent(About.this, WapView.class);
				i.putExtra("position", Integer.toString(Position));
				startActivity(i);
				finish();

			}
		});

	}

	private class MyListAdapter extends ArrayAdapter<ListHandler> {
		public MyListAdapter() {
			super(About.this, R.layout.about_layout, wapsites);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;

			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.about_layout,
						parent, false);
			}

			ListHandler handler = wapsites.get(position);
			ImageView icon = (ImageView) itemView.findViewById(R.id.back);
			icon.setImageResource(handler.getIconId());
			TextView title = (TextView) itemView.findViewById(R.id.TITLE);
			title.setText(handler.getTitle());
			TextView high = (TextView) itemView.findViewById(R.id.HIGH);
			high.setText(handler.getHigh());

			return itemView;

		}
	}

	public void addWapsites() {
		wapsites.add(new ListHandler("HIV360",
				"learn about HIV and other STIs", R.drawable.health_icon));
		wapsites.add(new ListHandler("HIVSA",
				"HIVSA is non-profit organisation", R.drawable.hivsa));
		wapsites.add(new ListHandler("Yabonga", "Living positively with HIV",
				R.drawable.yabonga));
		wapsites.add(new ListHandler("UNAIDS", "UNAIDS", R.drawable.unaids));
		wapsites.add(new ListHandler("LoveLife", "Powering the Future",
				R.drawable.lovelife));
		wapsites.add(new ListHandler("AIDS Foundation Of SA",
				"Developing Partnerships", R.drawable.aids));
		wapsites.add(new ListHandler("World Health Organization", "WHO",
				R.drawable.who));
		wapsites.add(new ListHandler("HIV", "HIV Clinical Resource",
				R.drawable.hivclinic));
		wapsites.add(new ListHandler("ashm", "US DHHS GuideLines ",
				R.drawable.ashm));
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(getApplicationContext(),
				" Us the exit button to log out", Toast.LENGTH_SHORT).show();

	}
}
