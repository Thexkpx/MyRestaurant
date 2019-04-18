package appewtc.com.myrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class OrderTABLE {

    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSQLite, readSQLite;

    public static final String TABLE_ORDER = "orderTABLE";
    public static final String COLUMN_ID_ORDER = "_id";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_BANG = "Bang";
    public static final String COLUMN_FOOD = "Food";
    public static final String COLUMN_ITEM = "Item";

    public OrderTABLE(Context context) {

        objMyOpenHelper = new MyOpenHelper(context);
        writeSQLite = objMyOpenHelper.getWritableDatabase();
        readSQLite = objMyOpenHelper.getReadableDatabase();

    }   // Constructor

    public long addValueOrder(String strName, String strBang, String strFood, int intItem) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_NAME, strName);
        objContentValues.put(COLUMN_BANG, strBang);
        objContentValues.put(COLUMN_FOOD, strFood);
        objContentValues.put(COLUMN_ITEM, intItem);

        return writeSQLite.insert(TABLE_ORDER, null, objContentValues);
    }

   public Cursor readAllData() {

      Cursor objCursor = readSQLite.query(TABLE_ORDER, new String[]{COLUMN_ID_ORDER, COLUMN_NAME, COLUMN_BANG, COLUMN_FOOD}, null, null, null, null, null);
      if (objCursor != null) {
        objCursor.moveToFirst();
    }

       return objCursor;
   }


}
