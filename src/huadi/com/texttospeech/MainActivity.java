package huadi.com.texttospeech;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class MainActivity extends Activity implements OnInitListener
{
	private static final int MY_DATA_CHECK_CODE = 0;
	
	private TextToSpeech tts;
	private Button btnSpeak;
	private EditText txtText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 檢查tts是否已安裝且可用
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
		
		//tts = new TextToSpeech(this, this);
		
		btnSpeak = (Button) findViewById(R.id.btnSpeak);
		txtText = (EditText) findViewById(R.id.editText1);
		
		btnSpeak.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				speakOut();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		if (requestCode == MY_DATA_CHECK_CODE)
		{
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) // 如果TTS Engine有成功找到的話
			{
				tts = new TextToSpeech(this, this);
				// 宣告一個 TextToSpeech instance, 註冊android.speech.tts.TextToSpeech.OnInitListener
				// 當TTS Engine 初始完後會呼叫 onInit(int status)
				Log.d("onActivityResult", "onInit");
			}
			else // 如果 TTS 沒有安裝的話 , 要求安裝			
			{
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	@Override
	public void onInit(int status)
	{
		if (status == TextToSpeech.SUCCESS)
		{
			tts.setPitch(1.0f); // 音調
			tts.setSpeechRate(1); // 速度
			int result = tts.setLanguage(Locale.US); // 語言
			
			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
			{
				Log.e("TTS", "This Language is not supported");
			}
			else
			{
				btnSpeak.setEnabled(true);
				speakOut();
			}
		}
		else
		{
			Log.e("TTS", "Initilization Failed!");
		}
	}

	private void speakOut()
	{
		String text = txtText.getText().toString();
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null); //TextToSpeech.QUEUE_ADD 為目前的念完才念
	}

	@Override
	public void onDestroy()
	{
		if (tts != null)
		{
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}
}
