package com.example.quartoprojeto;

import java.io.ByteArrayOutputStream;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Tela3 extends Activity {
	
	private static final int SELECT_PICTURE = 1;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tela3);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Button btn = (Button) findViewById(R.id.buttonImage);
		btn.setOnClickListener(mOnClickListener);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.activity_tela3, menu);
		return true;
	}
	
	public void save(){
		EditText txtTitulo = (EditText) findViewById(R.id.editTextTitulo);
		EditText txtAutor = (EditText) findViewById(R.id.editTextAutor);
		ImageView imagemCapa = (ImageView) findViewById(R.id.imageCapa);
		
		String titulo = txtTitulo.getText().toString();
		String autor = txtAutor.getText().toString();
		
		Bitmap bitmap = ((BitmapDrawable)imagemCapa.getDrawable()).getBitmap();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, outputStream);
		byte[] img = outputStream.toByteArray();
		
		ContentValues values = new ContentValues();
		
		values.put(BookDbHelper.C_TITULO, titulo);
		values.put(BookDbHelper.C_AUTOR, autor);
		values.put(BookDbHelper.C_CAPA, img);
		
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
	
	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Selecione a capa do Livro"), SELECT_PICTURE);
			
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			if(requestCode == SELECT_PICTURE){
				Uri selectedImage = data.getData();
				ImageView imagemCapa = (ImageView) findViewById(R.id.imageCapa);
				imagemCapa.setImageURI(selectedImage);
			}
		}
	}

}
