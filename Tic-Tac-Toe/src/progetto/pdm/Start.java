package progetto.pdm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Start extends Activity {
	
		EditText text1;
		EditText text2;
		EditText text3;
		String tag = "StartActivity";
		
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        text1=(EditText)findViewById(R.id.editText1);//Inserisci Nome Giocatore 1			
        text2=(EditText)findViewById(R.id.editText2);//Inserisci Nome Giocatore 2 (Avversario)
        text3=(EditText)findViewById(R.id.editText3);//Inserisci Password
        
        Button btn=(Button)findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {  //Azioni svolte dal click sul bottone
				// TODO Auto-generated method stub
				Log.d(tag, "Start Game");
				Intent intent=new Intent(Start.this,GameActivity.class);
				String iltesto=text1.getText().toString();
				String iltesto2=text2.getText().toString();
				String iltesto3=text3.getText().toString();
				intent.putExtra("iltestonelbox", iltesto);
				intent.putExtra("iltestonelbox2", iltesto2);
				intent.putExtra("Password", iltesto3);
				startActivity(intent);
			}
		});
        
    }
}
