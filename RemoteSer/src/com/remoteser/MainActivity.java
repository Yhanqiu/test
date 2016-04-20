package com.remoteser;

import java.io.File;
import java.util.Collection;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
//import androidx.plmgrdemo.PlugListViewAdapter;
//import androidx.plmgrdemo.R;
import androidx.pluginmgr.PluginManager;
import androidx.pluginmgr.environment.PlugInfo;


public class MainActivity extends Activity implements OnClickListener {  


	private ListView plugListView;
	//
	private PluginManager plugMgr;

	private static final String sdcard = Environment
			.getExternalStorageDirectory().getAbsolutePath();
  
    private Button playBtn;  
    private Button stopBtn;  
//    private Button pauseBtn;  
    private Button exitBtn;  
  
    private IMusicControlService musicService;  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
  
        playBtn = (Button) findViewById(R.id.play);  
        stopBtn = (Button) findViewById(R.id.stop);  
//        pauseBtn = (Button) findViewById(R.id.pause);  
        exitBtn = (Button) findViewById(R.id.exit);  

        connection();  
  
        playBtn.setOnClickListener(this);  
        stopBtn.setOnClickListener(this);  
//        pauseBtn.setOnClickListener(this);  
        exitBtn.setOnClickListener(this);  
        
        


		final EditText pluginDirTxt = (EditText) findViewById(R.id.pluginDirTxt);
		Button pluginLoader = (Button) findViewById(R.id.pluginLoader);
		plugListView = (ListView) findViewById(R.id.pluglist);

		plugMgr = PluginManager.getSingleton();

		String pluginSrcDir = sdcard;
		pluginDirTxt.setText(pluginSrcDir);

		plugListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				plugItemClick(position);
			}
		});

		final Context context = this;
		pluginLoader.setOnClickListener(new View.OnClickListener() {
			private volatile boolean plugLoading = false;

			@Override
			public void onClick(View v) {
				final String dirText = pluginDirTxt.getText().toString().trim();
				if (TextUtils.isEmpty(dirText)) {
					Toast.makeText(context, getString(R.string.pl_dir),
							Toast.LENGTH_LONG).show();
					return;
				}
				if (plugLoading) {
					Toast.makeText(context, getString(R.string.loading),
							Toast.LENGTH_LONG).show();
					return;
				}
				String strDialogTitle = getString(R.string.dialod_loading_title);
				String strDialogBody = getString(R.string.dialod_loading_body);
				final ProgressDialog dialogLoading = ProgressDialog.show(
						context, strDialogTitle, strDialogBody, true);
				new Thread(new Runnable() {
					@Override
					
					public void run() {
						plugLoading = true;
						try {
							Collection<PlugInfo> plugs = plugMgr
									.loadPlugin(new File(dirText));
                            PluginManager.getSingleton().dump();
                            setPlugins(plugs);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							dialogLoading.dismiss();
						}
						plugLoading = false;
					}
				}).start();
			}
		});
  
    }  


//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//	}

	private void plugItemClick(int position) {
		PlugInfo plug = (PlugInfo) plugListView.getItemAtPosition(position);
		Toast.makeText(this, "whattt", Toast.LENGTH_SHORT).show();
		plugMgr.startMainActivity(this, plug);
	}

	private void setPlugins(final Collection<PlugInfo> plugs) {
		if (plugs == null || plugs.isEmpty()) {
			return;
		}
		final ListAdapter adapter = new PlugListViewAdapter(this, plugs);
		runOnUiThread(new Runnable() {
			public void run() {
				plugListView.setAdapter(adapter);
				
			}
		});
	}
    

	
  
    private void connection() {  
        Intent intent = new Intent("com.remoteser.IMusicControlService");  
        bindService(intent, sc, Context.BIND_AUTO_CREATE);              // bindService  
    }  
  
    @Override  
    public void onClick(View v) {  
    	int i;
//    	System.out.println(Process.myUid()+"   "+Process.myPid());
  
        try {  
            switch (v.getId()) {  
            case R.id.play:  
//            	Intent in = new Intent(IMusicControlService.class.getName());
//            	bindService(in, sc, BIND_AUTO_CREATE);

            	Log.d("main", Process.myUid()+"   "+Process.myPid()+"   "+getApplication().toString());
            	i=musicService.play();
            	Toast.makeText(getApplicationContext(), Integer.toString(i), Toast.LENGTH_SHORT).show();
            	Log.d("appli", musicService.serviceApplication());
//                  musicService.play();
                break;  
            case R.id.stop:  
                if (musicService != null) {  
//                	Toast.makeText(getApplicationContext(), musicService.stop(), Toast.LENGTH_SHORT).show();
                	i=musicService.stop();
                	Toast.makeText(getApplicationContext(), Integer.toString(i), Toast.LENGTH_SHORT).show();
//                    Integer.toString(musicService.stop());  
                }  
                break;  
//            case R.id.pause:  
//                if (musicService != null) {  
////                	Toast.makeText(getApplicationContext(), musicService.pause(), Toast.LENGTH_SHORT).show();
////                    musicService.pause();  
//                	i=musicService.pause();
//                	Toast.makeText(getApplicationContext(), Integer.toString(i), Toast.LENGTH_SHORT).show();
//                }  
//                break;  
            case R.id.exit:  
                this.finish();  
                break;  
            }  
        } catch (RemoteException e) {  
            e.printStackTrace();  
        }  
    }  
  
    private ServiceConnection sc = new ServiceConnection() {  
        @Override  
        public void onServiceConnected(ComponentName name, IBinder service) {       //connect Service  
            musicService = IMusicControlService.Stub.asInterface(service);  
        }  
  
        @Override  
        public void onServiceDisconnected(ComponentName name) {                 //disconnect Service  
            musicService = null;  
            Log.e("disconnect", "disco");
        }  
  
    };  
      
    @Override  
    public void onDestroy(){  
        super.onDestroy();  
          
        if(sc != null){  
            unbindService(sc);              // unBindService  
        }  
    }  
}  