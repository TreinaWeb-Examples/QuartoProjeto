package com.example.quartoprojeto;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class Tela3 extends Activity {
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tela3);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.activity_tela3, menu);
		return true;
	}
	
	public void save(){
		EditText txtTitulo = (EditText) findViewById(R.id.editTextTitulo);
		EditText txtAutor = (EditText) findViewById(R.id.editTextAutor);
		
		String titulo = txtTitulo.getText().toString();
		String autor = txtAutor.getText().toString();
		
		ContentValues values = new ContentValues();
		
		values.put(BookDbHelper.C_TITULO, titulo);
		values.put(BookDbHelper.C_AUTOR, autor);
		
		ContentResolver provedor = getContentResolver();
		
		provedor.insert(BooksProvider.CONTENT_URI, values);
		
		Intent intent = new Intent(getBaseContext(), Tela1.class);
		startActivity(intent);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.action_save:
				save();
				return true;
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
