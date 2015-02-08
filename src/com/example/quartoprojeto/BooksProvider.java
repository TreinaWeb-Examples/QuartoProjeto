package com.example.quartoprojeto;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class BooksProvider extends ContentProvider {
	private BookDbHelper dbHelper;
	
	//authority e caminhos
	public static final String AUTHORITY = "com.example.quartoprojeto.livros";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	public static final String ID_PATH = "id/*";
	public static final String TITULO_PATH = "titulo/*";
	public static final String AUTOR_PATH = "autor/*";
	
	//UriMatcher
	public static final int BOOKS = 1;
	public static final int BY_ID = 2;
	public static final int BY_TITULO = 3;
	public static final int BY_AUTOR = 4;
	
	static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	static{
		matcher.addURI(AUTHORITY, null, BOOKS);
		matcher.addURI(AUTHORITY, ID_PATH, BY_ID);
		matcher.addURI(AUTHORITY, TITULO_PATH, BY_TITULO);
		matcher.addURI(AUTHORITY, AUTOR_PATH, BY_AUTOR);
		matcher.addURI(AUTHORITY, "#", BOOKS);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int match = matcher.match(uri);
		
		if(match == 1){
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			return db.delete(BookDbHelper.TABLE, selection, selectionArgs);
		}else
			return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		int match = matcher.match(uri);
		
		if(match == 1){
			return "vnd.android.cursor.dir/vnd.quartoprojeto.livros";
		}else
			return "vnd.android.cursor.item/vnd.quartoprojeto.livros";
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		int match = matcher.match(uri);
		
		long newID = 0;
		if(match != 1)
			throw new IllegalArgumentException("Wrong Uri " + uri.toString());
		if(values != null){
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			newID = db.insert(BookDbHelper.TABLE, null, values);
			return Uri.withAppendedPath(uri, String.valueOf(newID));
		}else	
			return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dbHelper = new BookDbHelper(getContext());
		if(dbHelper == null)
			return false;
		else
			return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs,	String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		String order = null;
		Cursor result = null;
		
		if(sortOrder != null)
			order = sortOrder;
		
		int match = matcher.match(uri);
		
		try{
			switch(match){
			case BOOKS:
				//content://com.example.quartoprojeto.livros
				result = db.query(BookDbHelper.TABLE, projection, selection, selectionArgs, null, null, order);
				break;
			case BY_ID:
				//content://com.example.quartoprojeto.livros/id/?
				result = db.query(BookDbHelper.TABLE, projection, BookDbHelper.C_ID + "=?",
						new String[] {uri.getLastPathSegment()}, null, null, order);
				break;
			case BY_TITULO:
				//content://com.example.quartoprojeto.livros/titulo/?
				result = db.query(BookDbHelper.TABLE, projection, BookDbHelper.C_TITULO + "=?",
						new String[] {uri.getLastPathSegment()}, null, null, order);
				break;
			case BY_AUTOR:
				//content://com.example.quartoprojeto.livros/autor/?
				result = db.query(BookDbHelper.TABLE, projection, BookDbHelper.C_AUTOR + "=?",
						new String[] {uri.getLastPathSegment()}, null, null, order);
				break;
			}
		}catch(Exception e){
			Log.e("Erro Provider", e.getMessage());
		}
		
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int match = matcher.match(uri);
		
		int rows = 0;
		
		if(match != 1)
			throw new IllegalArgumentException("Wrong Uri " + uri.toString());
		if(values != null){
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			rows = db.update(BookDbHelper.TABLE, values, selection, selectionArgs);
		}
		
		return rows;
	}

}
