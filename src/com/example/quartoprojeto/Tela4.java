package com.example.quartoprojeto;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class Tela4 extends Activity {
	private static final int LOADER_ID = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tela4);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		if(intent.hasExtra("id")){
			LoaderManager ln = getLoaderManager();
			ln.initLoader(LOADER_ID, intent.getExtras(), loaderManager);
		}else{
			finish();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.activity_tela4, menu);
		return true;
	}
	
	private LoaderManager.LoaderCallbacks<Cursor> loaderManager = new LoaderManager.LoaderCallbacks<Cursor>() {
		
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args){
			if(id == LOADER_ID){
				String idLivro = "";
				idLivro = args.getString("id");
				Uri uri = Uri.withAppendedPath(BooksProvider.CONTENT_URI, "id/" + idLivro);
				return new CursorLoader(getApplicationContext(), uri, null, null, null, null);
			}else{
				return null;
			}
		}
		
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
			if(loader.getId() == LOADER_ID){
				String titulo = "";
				String autor = "";
				String id = "";
				
				if(cursor.moveToNext()){
					id = cursor.getString(0);
					titulo = cursor.getString(1);
					autor = cursor.getString(2);
				}
				
				EditText txtId = (EditText) findViewById(R.id.editTextId);
				EditText txtTitulo = (EditText) findViewById(R.id.editTextTitulo);
				EditText txtAutor = (EditText) findViewById(R.id.editTextAutor);
				
				txtId.setText(id);
				txtTitulo.setText(titulo);
				txtAutor.setText(autor);
			}
		}
		
		@Override
		public void onLoaderReset(Loader<Cursor> loader){
			
		}
	};
	
	public void alter(){
		EditText txtId = (EditText) findViewById(R.id.editTextId);
		EditText txtTitulo = (EditText) findViewById(R.id.editTextTitulo);
		EditText txtAutor = (EditText) findViewById(R.id.editTextAutor);
		
		String id = txtId.getText().toString();
		String titulo = txtTitulo.getText().toString();
		String autor = txtAutor.getText().toString();
		
		ContentValues values = new ContentValues();
		
		values.put(BookDbHelper.C_TITULO, titulo);
		values.put(BookDbHelper.C_AUTOR, autor);
		
		ContentResolver provedor = getContentResolver();
		String selection = BookDbHelper.C_ID + "=?";
		String[] selectionArgs = {id};
		
		provedor.update(BooksProvider.CONTENT_URI, values, selection, selectionArgs);
		
		Intent intent = new Intent(getBaseContext(), Tela1.class);
		startActivity(intent);
	}
	
	public void delete(){
		final Tela4 _this = this;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_menu_delete).setTitle("Confirmação")
							.setMessage("Gostaria de excluir este livro?")
							.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int id) {
									// TODO Auto-generated method stub
									EditText txtId = (EditText) _this.findViewById(R.id.editTextId);
									String idLivro = txtId.getText().toString();
									
									ContentResolver provedor = getContentResolver();
									
									String selection = BookDbHelper.C_ID + "=?";
									String[] selectionArgs = {idLivro};
									
									provedor.delete(BooksProvider.CONTENT_URI, selection, selectionArgs);
									
									Toast.makeText(getApplicationContext(), "Livro excluído", Toast.LENGTH_SHORT).show();
									
									Intent intent = new Intent(getBaseContext(), Tela1.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);
								}
							}).setNegativeButton("Não", null);
		builder.show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.action_alter:
			alter();
			return true;
		case R.id.action_delete:
			delete();
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
