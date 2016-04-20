package com.remoteser;

import java.io.File;
import java.io.IOException;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;


public class RemoteMusicService extends Service {  
  
//    private MediaPlayer mediaPlayer;  
	private int i = 0;
    File file = new File("storage/emulated/0/bigbang-if you.mp3");
  
    @Override  
    public IBinder onBind(Intent intent) {  
        return sbinder;  
    }  
  
    private final IMusicControlService.Stub sbinder = new IMusicControlService.Stub() {  
  
        @Override  
        public int play() throws RemoteException {  
        	System.out.println(getCallingPid()+"   "+Process.myUid()+"   "+Process.myPid()+"   "+getApplication().toString());
//            if (mediaPlayer == null) {  
////                mediaPlayer = MediaPlayer.create(RemoteMusicService.this, R.raw.tmp);  
//            	mediaPlayer = MediaPlayer.create(RemoteMusicService.this, Uri.fromFile(file));
//                mediaPlayer.setLooping(false);  
//            }  
//            if (!mediaPlayer.isPlaying()) {  
//                mediaPlayer.start();  
//            }  
//        	Toast.makeText(getApplicationContext(), "play", Toast.LENGTH_SHORT).show();
        	return 0;
        }  
  
        @Override  
        public int pause() throws RemoteException {  
//            if (mediaPlayer != null && mediaPlayer.isPlaying()) {  
//                mediaPlayer.pause();  
//            }             
//        	Toast.makeText(getApplicationContext(), "pause", Toast.LENGTH_SHORT).show(); 
        	return 1;
        }  
  
        @Override  
        public int stop() throws RemoteException {  
//            if (mediaPlayer != null) {  
//                mediaPlayer.stop();  
//                try {  
//                    mediaPlayer.prepare();      // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数  
//                } catch (IOException ex) {  
//                    ex.printStackTrace();  
//                }  
//            }  
//        	Toast.makeText(getApplicationContext(), "stop", Toast.LENGTH_SHORT).show();
        	return 2;
        }  
        public String serviceApplication() {
        	Log.d("context", getApplicationContext().toString());
			return getApplication().toString();
		}
    };  
      
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
          
//        if(mediaPlayer != null){  
//            mediaPlayer.stop();  
//            mediaPlayer.release();  
//        }  
//    	Toast.makeText(getApplicationContext(), "stop", Toast.LENGTH_SHORT).show();
    }  
    
    
}  
