package appewtc.com.myrestaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import appewtc.masterung.myrestaurant.R;


public class OrderActivity extends ActionBarActivity {

    //Explicit
    private FoodTABLE objFoodTABLE;
    private String[] strListFood, strListGia;
    private TextView txtShowName;
    private EditText edtBang;
    private String strMyName, strMyBang, strMyFood, strItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //Bind Widget
        bindWidget();

        objFoodTABLE = new FoodTABLE(this);
        strMyBang = edtBang.getText().toString().trim();
        //setup Text Show Name
        setUpTxtShowName();


        //Synchronize JSON to SQLite
        synchronizeJSONtoFood();

        //setup All Array
        setupAllArray();

        //Create ListView
        createListView();



    }   // onCreate

    private void setUpTxtShowName() {
        strMyName = getIntent().getExtras().getString("Name");
        txtShowName.setText(strMyName);
    }

    private void bindWidget() {
        txtShowName = (TextView) findViewById(R.id.txtShowName);
        edtBang = (EditText) findViewById(R.id.edtBang);
    }   // bindWidget

    private void createListView() {

        int[] myTarget = {R.drawable.food1, R.drawable.food2, R.drawable.food3, R.drawable.food4, R.drawable.food5,
                R.drawable.food6, R.drawable.food7, R.drawable.food8, R.drawable.food9, R.drawable.food10,
                R.drawable.food11, R.drawable.food12, R.drawable.food13, R.drawable.food14, R.drawable.food15,
                R.drawable.food16, R.drawable.food17, R.drawable.food18, R.drawable.food19, R.drawable.food20};

        MyAdapter objMyadapter = new MyAdapter(getApplicationContext(), strListFood, strListGia, myTarget);
        ListView objListView = (ListView) findViewById(R.id.foodlistView);
        objListView.setAdapter(objMyadapter);

        //Active Click
        objListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Check Zero
                strMyBang = edtBang.getText().toString().trim();
                if (strMyBang.equals("")) {
                    MyAlertDialog objMyAlertDialog = new MyAlertDialog();
                    objMyAlertDialog.errorDialog(OrderActivity.this, "Chọn Bảng ?", "Xin vui lòng chọn bảng");
                } else {
                    strMyFood = strListFood[position];


                    //Show Choose iTem
                    showChooseItem();

                }   // if

            }   // event
        });

    }   // createListView

    private void showChooseItem() {

        CharSequence[] charItem = {"1", "2", "3", "4", "5",};

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.icon_question);
        objBuilder.setTitle("Choose Item ?");
        objBuilder.setCancelable(false);
        objBuilder.setSingleChoiceItems(charItem, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        strItem = "1";
                        break;
                    case 1:
                        strItem = "2";
                        break;
                    case 2:
                        strItem = "3";
                        break;
                    case 3:
                        strItem = "4";
                        break;
                    case 4:
                        strItem = "5";
                        break;
                }   // switch

                dialog.dismiss();

                checkLog();

                //up New Order to mySQL
                upNewOrderToMySQL();

            }
        });
        AlertDialog obAlertDialog = objBuilder.create();
        obAlertDialog.show();

    }   // shwoChooseItem

    private void upNewOrderToMySQL() {

        //SetUP Policy
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(myPolicy);
        }

        //Up Value
        try {

            ArrayList<NameValuePair> objNameValuePairs = new ArrayList<NameValuePair>();
           // objNameValuePairs.add( new BasicNameValuePair("isAdd", "true"));
            objNameValuePairs.add(new BasicNameValuePair("Name", strMyName));
            objNameValuePairs.add(new BasicNameValuePair("Bang", strMyBang));
            objNameValuePairs.add(new BasicNameValuePair("Food", strMyFood));
            objNameValuePairs.add(new BasicNameValuePair("Item", strItem));

                HttpClient objHttpClient = new DefaultHttpClient();
                HttpPost objHttpPost = new HttpPost("http://192.168.86.1/connn/get_order.php");
                objHttpPost.setEntity(new UrlEncodedFormEntity(objNameValuePairs, "UTF-8"));
                objHttpClient.execute(objHttpPost);

            Toast.makeText(OrderActivity.this, "Order Thành công " + strMyFood, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.d("Order", "Updata mySQL ===> " + e.toString());
        }

    }   // upNewOrder

    private void checkLog() {
        Log.d("order", "Name ==> " + strMyName);
        Log.d("order", "Bang ==> " + strMyBang);
        Log.d("order", "Food ==> " + strMyFood);
        Log.d("order", "Item ==> " + strItem);
    }   // checkLog

    private void setupAllArray() {

        strListFood = objFoodTABLE.listFood();
        strListGia = objFoodTABLE.listGia();



    }   // setupAllArray

    private void synchronizeJSONtoFood() {

        //Change Policy
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(myPolicy);
        }   // if

        InputStream objInputStream = null;
        String strJSON = "";

        //Create InputStream
        try {

            HttpClient objHttpClient = new DefaultHttpClient();
            HttpPost objHttpPost = new HttpPost("https://hoctiengviet.net/json.php");
            HttpResponse objHttpRestponse = objHttpClient.execute(objHttpPost);
            HttpEntity objHttpEntity = objHttpRestponse.getEntity();
            objInputStream = objHttpEntity.getContent();

        } catch (Exception e) {
            Log.d("Restaurant", "InputStream ==> " + e.toString());
        }

        //Create strJSON
        try {

            BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
            StringBuilder objStringBuilder = new StringBuilder();
            String strLine = null;
            while ((strLine = objBufferedReader.readLine()) != null) {
                objStringBuilder.append(strLine);
            }   // while
            objInputStream.close();
            strJSON = objStringBuilder.toString();

        } catch (Exception e) {
            Log.d("Restaurant", "strJSON ==> " + e.toString());
        }

        //Update Value to SQLite
        try {

            final JSONArray objJSONArray = new JSONArray(strJSON);
            for (int i = 0; i < objJSONArray.length(); i++) {
                JSONObject objJSONObject = objJSONArray.getJSONObject(i);
                String strFood = objJSONObject.getString("Food");
                String strGia = objJSONObject.getString("Gia");
                FoodTABLE objFoodTABLE = new FoodTABLE(this);
                long valueFood = objFoodTABLE.addValueToFood(strFood, strGia);
            }   // for

        } catch (Exception e) {
            Log.d("Restaurant", "Update SQLite ==> " + e.toString());
        }


    }   //synJSONtoFood


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}   // Main Class
