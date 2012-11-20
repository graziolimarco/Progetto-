package progetto.pdm;

import java.util.Timer;
import java.util.TimerTask;

import progetto.pdm.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import progetto.pdm.GameView.ICellListener;
import progetto.pdm.GameView.State;


public class GameActivity extends Activity implements MessageReceiver{
	
		enum Stato {
			WAIT_FOR_START, WAIT_FOR_STARTACK
		}
		
		private Stato statocorrente;
	    private Handler mHandler = new Handler(new MyHandlerCallback()); //Gestore Messaggi Ricevuti
	    private GameView mGameView;
	    private Button mButtonNext;
	    private String nomeMio, nomeAvversario, pass;
		ConnectionManager connection;
		TextView et1;
		String tag = "GameActivity";
		String casella;
		
		Timer timer = new Timer();
		TimerTask sendStart = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
					if ( statocorrente == Stato.WAIT_FOR_STARTACK){
					connection.send("START"); 
					}
			}
		};

	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle bundle) {
	        super.onCreate(bundle);
	        setContentView(R.layout.lib_game);

	        mGameView = (GameView) findViewById(R.id.game_view);
	        mButtonNext = (Button) findViewById(R.id.next_turn);

	        mGameView.setFocusable(true);
	        mGameView.setFocusableInTouchMode(true);
	        mGameView.setCellListener(new MyCellListener());

	        mButtonNext.setOnClickListener(new MyButtonListener());
	   
	        et1 = (TextView) findViewById(R.id.text);
			nomeMio = getIntent().getExtras().getString("iltestonelbox");
			pass = getIntent().getExtras().getString("Password");
			nomeAvversario = getIntent().getExtras().getString("iltestonelbox2");
			et1.setText(nomeMio + " VS  " + nomeAvversario);
				 		
			connection = new ConnectionManager(nomeMio, pass, nomeAvversario, this);  //Impostazione dei parametri di connessione       
				 		
			if (nomeAvversario.hashCode()<nomeMio.hashCode()){ 
					//Comincio IO
	                 timer.schedule(sendStart, 500, 10000);
	                 statocorrente = Stato.WAIT_FOR_STARTACK;
				}else{                            
					//Comincia Avversario
					statocorrente = Stato.WAIT_FOR_START;
					}
	   
	    }

	    @Override
	    protected void onResume() {
	        super.onResume();

	        State player = mGameView.getCurrentPlayer();
	        
	        if (player == State.WIN) {
	            setWinState(mGameView.getWinner());
	        }
	    }


	    private State selectTurn(State player) {
	        mGameView.setCurrentPlayer(player);
	        mButtonNext.setEnabled(false);

	        if (player == State.SELEZIONE) {
	        	Log.d(tag, "Turno GIOCATORE:"+nomeMio);
	        	Toast.makeText(getApplicationContext(), "Tocca a te:" +nomeMio, Toast.LENGTH_LONG).show();
	            mGameView.setEnabled(true);
	        } else if (player == State.ATTESA) {
	        	Log.d(tag, "Turno GIOCATORE:"+nomeAvversario);
	        	Toast.makeText(getApplicationContext(), "Tocca al tuo Avversario:" +nomeAvversario, Toast.LENGTH_LONG).show();
	            mGameView.setEnabled(false);
	        }

	        return player;
	    }

	    private class MyCellListener implements ICellListener {
	        public void onCellSelected() {
	            if (mGameView.getCurrentPlayer() == State.SELEZIONE) {
	                int cell = mGameView.getSelection();
	                mButtonNext.setEnabled(cell >= 0);
	            }
	        }
	    }

	    private class MyButtonListener implements OnClickListener {

	        public void onClick(View v) {
	            State player = mGameView.getCurrentPlayer();

	            if (player == State.WIN) {
	                GameActivity.this.finish(); //Il gioco è finito si torna alla schermata iniziale

	            } else if (player == State.SELEZIONE) {
	                int cell = mGameView.getSelection();
	                if (cell >= 0) {
	                    mGameView.stopBlink();
	                    mGameView.setCell(cell, player);
	                    connection.send("CASELLA:"+cell);  //Invio casella selezionata
	                    finishTurn();  //Turno finito, si aspetta la mossa dell'avversario
	                }
	            }
	        }
	    }

	    private class MyHandlerCallback implements Callback {  //Si Gestisce i Messaggi Ricevuti
	        public boolean handleMessage(Message msg) {
	        	
	        	State player = mGameView.getCurrentPlayer();

	        	if (msg.what == 100){ 
	        		player = State.ATTESA;
			        if (!checkGameFinished(player)){
			       		selectTurn(player);
			       	}
	        	}else if (msg.what == 200){
	        		player = State.SELEZIONE;
			        if (!checkGameFinished(player)){
			       		selectTurn(player);
			       	}
	        	}
	        	
	            if (msg.what == 8) {
	            	Log.d(tag, "OCCUPAZIONE CASELLA RICEVUTA");
	                mGameView.setCell(8, mGameView.getCurrentPlayer());
	                finishTurn();
	                return true;
	            }else if (msg.what == 7) {
	            	Log.d(tag, "OCCUPAZIONE CASELLA RICEVUTA");
                    mGameView.setCell(7, mGameView.getCurrentPlayer());
                    finishTurn();
	                return true;
				}else if (msg.what == 6) {
					Log.d(tag, "OCCUPAZIONE CASELLA RICEVUTA");
                    mGameView.setCell(6, mGameView.getCurrentPlayer());
                    finishTurn();
	                return true;
				}else if (msg.what == 5) {
					Log.d(tag, "OCCUPAZIONE CASELLA RICEVUTA");
                    mGameView.setCell(5, mGameView.getCurrentPlayer());
                    finishTurn();
	                return true;
				}else if (msg.what == 4) {
					Log.d(tag, "OCCUPAZIONE CASELLA RICEVUTA");
                    mGameView.setCell(4, mGameView.getCurrentPlayer());
                    finishTurn();
	                return true;
				}else if (msg.what == 3) {
					Log.d(tag, "OCCUPAZIONE CASELLA RICEVUTA");
                    mGameView.setCell(3, mGameView.getCurrentPlayer());
                    finishTurn();
	                return true;
				}else if (msg.what == 2) {
					Log.d(tag, "OCCUPAZIONE CASELLA RICEVUTA");
                    mGameView.setCell(2, mGameView.getCurrentPlayer());
                    finishTurn();
	                return true;
				}else if (msg.what == 1) {
					Log.d(tag, "OCCUPAZIONE CASELLA RICEVUTA");
                    mGameView.setCell(1, mGameView.getCurrentPlayer());
                    finishTurn();
	                return true;
				}else if (msg.what == 0) {
					Log.d(tag, "OCCUPAZIONE CASELLA RICEVUTA");
                    mGameView.setCell(0, mGameView.getCurrentPlayer());
                    finishTurn();
	                return true;
				}
	            return false;
	        }
	    }

	    private State getOtherPlayer(State player) {
	        return player == State.SELEZIONE ? State.ATTESA : State.SELEZIONE;
	    }

	    private void finishTurn() {
	        State player = mGameView.getCurrentPlayer();
	        if (!checkGameFinished(player)) {
	            player = selectTurn(getOtherPlayer(player));
	            Log.d(tag, "Cambio Stato:"+player);
	        }
	    }

	    public boolean checkGameFinished(State player) {
	        State[] data = mGameView.getData();
	        boolean full = true;

	        int col = -1;
	        int row = -1;
	        int diag = -1;

	        // controllo righe
	        for (int j = 0, k = 0; j < 3; j++, k += 3) {
	            if (data[k] != State.EMPTY && data[k] == data[k+1] && data[k] == data[k+2]) {
	                row = j;
	            }
	            if (full && (data[k] == State.EMPTY ||
	                         data[k+1] == State.EMPTY ||
	                         data[k+2] == State.EMPTY)) {
	                full = false;
	            }
	        }

	        // controllo colonne
	        for (int i = 0; i < 3; i++) {
	            if (data[i] != State.EMPTY && data[i] == data[i+3] && data[i] == data[i+6]) {
	                col = i;
	            }
	        }

	        // controllo diagonali
	        if (data[0] != State.EMPTY && data[0] == data[1+3] && data[0] == data[2+6]) {
	            diag = 0;
	        } else  if (data[2] != State.EMPTY && data[2] == data[1+3] && data[2] == data[0+6]) {
	            diag = 1;
	        }

	        if (col != -1 || row != -1 || diag != -1) {
	            setFinished(player, col, row, diag);
	            return true;
	        }

	        // se siamo arrivati ​​qui, non c'è nessun vincitore, ma la scheda è completa.
	        if (full) {
	            setFinished(State.EMPTY, -1, -1, -1);
	            return true;
	        }
	        return false;
	    }

	    private void setFinished(State player, int col, int row, int diagonal) {

	        mGameView.setCurrentPlayer(State.WIN);
	        mGameView.setWinner(player);
	        mGameView.setEnabled(false);
	        mGameView.setFinished(col, row, diagonal);

	        setWinState(player);
	    }

	    private void setWinState(State player) {
	        mButtonNext.setEnabled(true);
	        mButtonNext.setText("Back");

	        if (player == State.EMPTY) {
	        	Log.d(tag, "Gioco Terminato:Nessun Vincitore");
	        	Toast.makeText(getApplicationContext(), "Nessun Giocatore Ha Vinto", Toast.LENGTH_LONG).show();
	        } else if (player == State.SELEZIONE) {
	        	Log.d(tag, "Gioco Terminato:Vincitore "+nomeMio);
	        	Toast.makeText(getApplicationContext(), "Hai Vinto "+nomeMio, Toast.LENGTH_LONG).show();
	        } else {
	        	Log.d(tag, "Gioco Terminato:Vincitore "+nomeAvversario);
	        	Toast.makeText(getApplicationContext(), "Ha Vinto il tuo Avversario:"+nomeAvversario, Toast.LENGTH_LONG).show();	       
	        }
	    }

		@Override
		public void receiveMessage(String msg) {  //Ricevo messaggi
			// TODO Auto-generated method stub
			
			Log.d(tag,msg);
			
			State player = mGameView.getCurrentPlayer();
			
			if(msg.equals("START")){
				Log.d(tag, "Ricevuto START:sei pronto a giocare");
				if(statocorrente == Stato.WAIT_FOR_START){
					connection.send("STARTACK");
					mHandler.sendEmptyMessage(100);
				}
			}else if(msg.equals("STARTACK")){ //mando l'ack indietro
				Log.d(msg, "Ricevuto STARTACK:connessione svolta, il tuo avversario è pronto");
				if(statocorrente == Stato.WAIT_FOR_STARTACK){
				timer.cancel();
				mHandler.sendEmptyMessage(200);
				}
			}
			
			if(msg.startsWith("CASELLA:")){
					casella=msg.split(":")[1];
					Log.d(tag, casella);
					Log.d(tag, "Stato:"+player);
					if(casella.contains("8")){
						mHandler.sendEmptyMessage(8);
					}else if(casella.contains("7")){
						mHandler.sendEmptyMessage(7);
					}else if (casella.contains("6")) {
						mHandler.sendEmptyMessage(6);
					}else if (casella.contains("5")) {
						mHandler.sendEmptyMessage(5);
					}else if (casella.contains("4")) {
						mHandler.sendEmptyMessage(4);
					}else if (casella.contains("3")) {
						mHandler.sendEmptyMessage(3);
					}else if (casella.contains("2")) {
						mHandler.sendEmptyMessage(2);
					}else if (casella.contains("1")) {
						mHandler.sendEmptyMessage(1);
					}else if (casella.contains("0")) {
						mHandler.sendEmptyMessage(0);
						
					}
			}
			
		}

}	
