package com.uro.serialport.view.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.urovo.serialport.R;


/**
 * Activity
 * @author baxol
 *
 */
public abstract class BaseActivity extends AppCompatActivity{

    private ScrollView scrollView;
    private TextView tvOutput;
    private Button button_clearPage;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initView(){

        scrollView = (ScrollView)findViewById(R.id.scrollView);
        tvOutput = (TextView)findViewById(R.id.tvOutput);

        button_clearPage = (Button)findViewById(R.id.btn_clearpage);
        button_clearPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvOutput.setText("");
            }
        });
    }

    protected void outputText(String text) {
        outputColorText(TextColor.BLACK, text);
    }

    protected void outputNotWrapText(final String appendText) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                tvOutput.append(getColorText(TextColor.BLACK, appendText));

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 50);
            }
        });
    }

    protected void outputColorText(final TextColor color, final String text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String appendText = "\n" + text;
                tvOutput.append(getColorText(color, appendText));

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 50);
            }
        });
    }

    protected void clearText() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                tvOutput.setText("");

            }
        });
    }

    protected static SpannableStringBuilder getColorText(TextColor color, String text) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        ForegroundColorSpan colorSpan = null;
        switch (color) {
            case RED:
                colorSpan = new ForegroundColorSpan(Color.RED);
                break;
            case GREEN:
                colorSpan = new ForegroundColorSpan(Color.GREEN);
                break;
            case BLUE:
                colorSpan = new ForegroundColorSpan(Color.BLUE);
                break;
            case DKGRAY:
                colorSpan = new ForegroundColorSpan(Color.DKGRAY);
                break;
            case YELLOW:
                colorSpan = new ForegroundColorSpan(Color.YELLOW);
                break;
            case MAGENTA:
                colorSpan = new ForegroundColorSpan(Color.MAGENTA);
                break;
            case CYAN:
                colorSpan = new ForegroundColorSpan(Color.CYAN);
                break;
            case BLACK:
            default:
                colorSpan = new ForegroundColorSpan(Color.BLACK);
                break;
        }
        ssb.setSpan(colorSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    public enum TextColor {
        BLACK,
        RED,
        GREEN,
        BLUE,
        DKGRAY,
        YELLOW,
        MAGENTA,
        CYAN
    }

    protected void showMessage(String message){

        Toast.makeText(BaseActivity.this, ""+message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void showDialogError(String resourceId) {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("错误提示");
        b.setMessage(resourceId);
        b.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
            }
        });
        b.show();
    }

}
