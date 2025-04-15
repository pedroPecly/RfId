package com.uro.serialport;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uro.utils.ConfigurationUtils;
import com.urovo.serialport.R;


/**
* @author 作者 :wrc
* @version 创建时间：2019年11月5日 上午10:18:35
* 类说明
*/
public class SerialPortSettingActivity extends AppCompatActivity {
	
	private EditText edittextSave ;
	private LinearLayout linearLayout;
	private Button btnSave ;
	private TextView tvUseSetting;
	private Spinner spinnerSerial,spinnerBaudrates;
	private String [] arraySerial =null;
	private String [] arrayBaudrates =null;
	private int  arraySerialLength = 0;
	private int arrayBaudratesLength = 0;
	private int type = 0 ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serialport_setting);
		
		Resources res =getResources();
		arraySerial = res.getStringArray(R.array.serial_value);
		arrayBaudrates = res.getStringArray(R.array.baudrates_value);
		arraySerialLength = arraySerial.length-1;
		arrayBaudratesLength = arrayBaudrates.length -1;
		
		edittextSave = (EditText) findViewById(R.id.edittext_serial);
		linearLayout = (LinearLayout) findViewById(R.id.ll_input);
		tvUseSetting = (TextView) findViewById(R.id.tv_use_setting);
		btnSave = (Button)findViewById(R.id.btn_save);
		type = getIntent().getIntExtra("type",0);
		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String edStr  = edittextSave.getText().toString().trim();
				if (TextUtils.isEmpty(edStr)) {
					showToas("请先输入串口号");
					return;
				}
				
				ConfigurationUtils.setSerialPort(type,arraySerialLength, edStr);
				updateUseSetting();
			}
		});
		updateUseSetting();
		
		spinnerSerial = (Spinner) findViewById(R.id.spinner_serial);
		spinnerBaudrates = (Spinner) findViewById(R.id.spinner_baudrates);
		
//		spinnerSerial.setAdapter(new Sim);
		spinnerSerial.setSelection(ConfigurationUtils.getSerialPort_Index(type));
		spinnerSerial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	               if (position == 0) {
	            	   linearLayout.setVisibility(View.GONE);
					ConfigurationUtils.setSerialPort(type,0, "");
				}else if (position >0 && position<arraySerialLength) {
					linearLayout.setVisibility(View.GONE);
					ConfigurationUtils.setSerialPort(type,position, arraySerial[position]);
				}else {
					linearLayout.setVisibility(View.VISIBLE);
//					ConfigurationUtils.setSerialPort(position, "");
				}
	               updateUseSetting();
	            }

	            @Override
	            public void onNothingSelected(AdapterView<?> parent) {
	            
	            	
	            	
	            }
	        });
		
		
		
		spinnerBaudrates.setSelection(ConfigurationUtils.getBaudrates_Index(type));
		spinnerBaudrates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ConfigurationUtils.setBaudrates(type,position, Integer.valueOf(arrayBaudrates[position]));
               updateUseSetting();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            
            	
            	
            }
        });
	}
	
	private void updateUseSetting(){
		String serial = ConfigurationUtils.getSerialPort_Value(type);
		int baudrates = ConfigurationUtils.getBaudrates_Value(type);
		tvUseSetting.setText("当前使用  串口号："+(TextUtils.isEmpty(serial)?"自动匹配":serial)+"   波特率： "+baudrates);
		
	}
	
	private void showToas(String msg){
		Toast.makeText(SerialPortSettingActivity.this, msg, Toast.LENGTH_LONG).show();
	}
}
