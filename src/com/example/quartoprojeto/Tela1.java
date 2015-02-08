package com.example.quartoprojeto;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Tela1 extends Activity {
	private static final int LOADER_ID = 1;
	private SimpleCursorAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tela1);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.activity_tela1, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(mQueryTextListener);
		return true;
	}
	
	private LoaderCallbacks<Cursor> loaderManager = new 
			LoaderManager.LoaderCallbacks<Cursor>(){
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args){
			if(args!=null){
				if(args.containsKey("filter")){
					String filter = args.getString("filter");
					
					String selection = BookDbHelper.C_TITULO + " like ? or " + BookDbHelper.C_AUTOR + " like ?";
					String[] selectionArgs = {"%" + filter + "%", "%" + filter + "%"};
					return new CursorLoader(getApplicationContext(), BooksProvider.CONTENT_URI, null, selection, selectionArgs, null);
				}
			}
			return new CursorLoader(getApplicationContext(), BooksProvider.CONTENT_URI, null, null, null, null);
		}
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
			if(loader.getId() == LOADER_ID){
				mAdapter.swapCursor(cursor);
			}
		}
		@Override
		public void onLoaderReset(Loader<Cursor> loader){
			mAdapter.swapCursor(null);
		}
	};
	@Override
	protected void onStart(){
		super.onStart();
		String[] uiBindFrom = { BookDbHelper.C_ID, BookDbHelper.C_TITULO, 
				BookDbHelper.C_AUTOR };
		int[] uiBindTo = { R.id.textViewId, R.id.textViewTitulo, R.id.textViewAutor };
		
		mAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.activity_tela2, 
				null, uiBindFrom, uiBindTo, 0);
		
		ListView list = (ListView) findViewById(R.id.listViewLivros);
		list.setAdapter(mAdapter);
		list.setOnItemClickListener(mClickItemList);
		
		LoaderManager ln = getLoaderManager();
		ln.initLoader(LOADER_ID, null, loaderManager);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.action_add:
			Intent intent = new Intent(getBaseContext(), Tela3.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private OnItemClickListener mClickItemList = new OnItemClickListener(){
		
		@Override
		public void onItemClick(AdapterView<?> adapter, View view,int position,
				long id){
			Intent intent = new Intent(getBaseContext(), Tela4.class);
			intent.putExtra("id", String.valueOf(id));
			
			startActivity(intent);
		}
	};
	
	private SearchView.OnQueryTextListener mQueryTextListener = new SearchView.OnQueryTextListener() {
		
		@Override
		public boolean onQueryTextSubmit(String query) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean onQueryTextChange(String newText) {
			// TODO Auto-generated method stub
			Bundle params = new Bundle();
			
			params.putString("filter", newText);
			
			LoaderManager mLoader = getLoaderManager();
			mLoader.restartLoader(LOADER_ID, params, loaderManager);
			return true;
		}
	};
}
