package com.dlvct.utils.db;

import com.dlvct.barcode.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper{

    //用来保存UserID、Access Token、Access Secret的表名

	public static final String TB_USER="user";
	public static final String TB_TYPE="type";
	public static final String TB_ATTRIBUTE="attribute";
	public static final String TB_COLLECT="collect";
    public static final String TB_PREFERENCES="preferences";
    public static final String VIEW = "collect_view";
    
    private String[] types ;
    
    public SqliteHelper(Context context, String name, CursorFactory factory, int version) {

        super(context, name, factory, version);
        types = context.getResources().getStringArray(R.array.type);
    }

    //创建表

    @Override

    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS "+
				TB_USER+"("+
                "ID"+" integer  primary key,"+
                "USERNAME"+" varchar,"+
                "PASSWORD"+" varchar"+
                ")"
                );
        db.execSQL("CREATE TABLE IF NOT EXISTS "+
				TB_PREFERENCES+"("+
                "ID"+" integer  primary key,"+
                "USERID"+" varchar,"+
                "AUTO_LOGIN"+" varchar"+ 		//0:正常  1:自动登陆
                ")"
                );
        db.execSQL("CREATE TABLE IF NOT EXISTS "+
				TB_TYPE+"("+
                "ID"+" integer  primary key,"+
                "TYPE"+" varchar"+
                ")"
                );
        db.execSQL("CREATE TABLE IF NOT EXISTS "+
				TB_ATTRIBUTE+"("+
                "ID"+" integer  primary key,"+
                "TYPEID"+" varchar,"+
                "NAME"+" varchar,"+
                "ICON"+" varchar"+
                ")"
                );
        db.execSQL("CREATE TABLE IF NOT EXISTS "+
				TB_COLLECT+"("+
                "ID"+" integer  primary key,"+
                "TYPEID"+" varchar,"+
                "ATTRIBUTE_ID"+" varchar,"+
                "VALUE"+" varchar,"+
                "TIME"+" varchar"+
                ")"
                );
        db.execSQL("CREATE VIEW IF NOT EXISTS "+
				VIEW+" AS SELECT C.TYPEID,C.ATTRIBUTE_ID,A.TYPE,B.NAME AS ATTRIBUTE,C.VALUE,B.ICON,C.TIME FROM "+
        		TB_TYPE+" A,"+TB_ATTRIBUTE+" B,"+TB_COLLECT+" C WHERE A.ID=B.TYPEID AND C.TYPEID=A.ID AND C.ATTRIBUTE_ID=B.ID ORDER BY C.TIME"
                );
        for(int i=0;i<types.length;i++){
        	db.execSQL("INSERT INTO "+TB_TYPE+" (TYPE) VALUES ('"+types[i]+"')");
        }
//        CREATE VIEW tt as select a.type,b.name as attribute,c.value,
//        c.time from type a,attribute b,collect c where a.id=b.typeid and c.typeid=a.id and c.attribute_id=b.id order by c.time;
        Log.i("Database","onCreate");

    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
      db.execSQL("DROP TABLE IF EXISTS " + TB_USER);
      onCreate(db);
      Log.e("Database","onUpgrade");
	}

}
