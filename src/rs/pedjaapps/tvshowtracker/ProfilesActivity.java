package rs.pedjaapps.tvshowtracker;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.support.v4.app.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import rs.pedjaapps.tvshowtracker.adapter.*;
import rs.pedjaapps.tvshowtracker.model.*;
import rs.pedjaapps.tvshowtracker.utils.*;

public class ProfilesActivity extends BaseActivity {
	ProfilesAdapter adapter;
	SharedPreferences prefs;
	EditText name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profiles);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		ListView list = (ListView)findViewById(R.id.list);
		adapter = new ProfilesAdapter(this, R.layout.profiles_row);
		updateList();
		list.setAdapter(adapter);
		final Button add = (Button)findViewById(R.id.add);
		name = (EditText)findViewById(R.id.name);
		name.addTextChangedListener(new TextWatcher(){

		@Override
		public void afterTextChanged(Editable arg0) {
			if(name.getText().toString().length() <1){
				name.setError("Profile name cannot be empty!");
				add.setEnabled(false);
			}
			else{
				add.setEnabled(true);
			}
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}});
		add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//new DatabaseHandler(ProfilesActivity.this).addProfile(name.getText().toString());
				updateList();
				switchProfile(name.getText().toString());
			}
		});
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switchProfile(adapter.getItem(arg2).getName());
			}
		});
	}

	private void updateList(){
		/*adapter.clear();
		for(String s: db.getAllProfiles()){
			boolean active = (prefs.getString("profile", "Default").equals(s));
			adapter.add(new Profile(s, active));
		}*/
	}
	
	private void switchProfile(final String name){
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	
	builder.setTitle("Switch Profile");
	builder.setMessage("Switch to Profile: "+name+"?");
	builder.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("profile", name);
				editor.apply();
				updateList();
				ProfilesActivity.this.name.setText("");
				Tools.setRefresh(true);
			}
		});
	builder.setNegativeButton(getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			ProfilesActivity.this.name.setText("");
		}
	});
	AlertDialog alert = builder.create();

	alert.show();
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
