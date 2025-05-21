package com.uro.serialport;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.urovo.serialport.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getResources().getString(R.string.app_name) + " v" + getAppInfo());
        findViewById(R.id.button_pc).setOnClickListener(this);
        findViewById(R.id.button_pos).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }    private void DisplayError(String resourceId) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Aviso de Erro");
        b.setMessage(resourceId);
        b.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        b.show();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.button_pc) {
            intent = new Intent(MainActivity.this, DowloadPCActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.button_pos) {
            intent = new Intent(MainActivity.this, ImportActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 获取版本信息
     *
     * @return
     */
    private String getAppInfo() {
        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(pkName, 0).versionName;
            return versionName;
        } catch (Exception e) {
            // Handle exception appropriately, e.g., logging
            return null;
        }
    }
}