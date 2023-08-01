package com.example.tomatoalarmclock;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import com.example.tomatoalarmclock.bean.message;

import org.litepal.LitePal;
import org.litepal.exceptions.DataSupportException;
import org.litepal.tablemanager.Connector;

public class clock extends AppCompatActivity {

    private TextView countdownText;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);


        float time = getIntent().getFloatExtra("Time",0);
        int position =getIntent().getIntExtra("Position",0);

        countdownText = findViewById(R.id.countdown_text);
        countDownTimer = new CountDownTimer((long) (time *60* 60*1000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long i =millisUntilFinished;
                long hours = millisUntilFinished / (60 * 60 * 1000);
                long minutes = (millisUntilFinished % (60 * 60 * 1000)) / (60 * 1000);
                long seconds = (millisUntilFinished % (60 * 1000)) / 1000;
                countdownText.setText(String.format("%02d:%02d:%02d", hours, minutes,seconds));
            }

            @Override
            public void onFinish() {

                countdownText.setText("倒计时完成");
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                // 检查设备是否处于静音模式
                boolean isSilentMode = audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;

                // 判断是否支持震动
                boolean hasVibrator = vibrator != null && vibrator.hasVibrator();

                if (!isSilentMode) {
                    // 播放响铃声音
                    Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
                    if (ringtone != null && !ringtone.isPlaying()) {
                        ringtone.play();
                        long ringDuration = 1000; //

                        // 延迟停止响铃
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ringtone.stop();
                            }
                        }, ringDuration);
                    }
                }

                if (hasVibrator) {
                    // 执行震动
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        VibrationEffect vibrationEffect = VibrationEffect.createOneShot(
                                1000, VibrationEffect.DEFAULT_AMPLITUDE);
                        vibrator.vibrate(vibrationEffect);
                    } else {
                        vibrator.vibrate(1000);
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(clock.this);
                builder.setTitle("提示");
                builder.setMessage("您现在可以进入休息状态了，别忘了打开飞行模式哦！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(clock.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.show();


            }
        };
        countDownTimer.start();
    }

}