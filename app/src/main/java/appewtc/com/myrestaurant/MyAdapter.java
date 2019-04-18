package appewtc.com.myrestaurant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import appewtc.masterung.myrestaurant.R;

public class MyAdapter extends BaseAdapter{

    //Explicit
    private Context objContext;
    private String[] strNameFood, strGiaFood;
    private int[] intMyTarget;

    public MyAdapter(Context context, String[] strName, String[] strGia, int[] targetID) {

        this.objContext = context;
        this.strNameFood = strName;
        this.strGiaFood = strGia;
        this.intMyTarget = targetID;

    }   // Constructor

    @Override
    public int getCount() {
        return strNameFood.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater objLayoutInflater = (LayoutInflater) objContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = objLayoutInflater.inflate(R.layout.list_view_row, parent, false);

        //Setup Text Food
        TextView listViewFood = (TextView) view.findViewById(R.id.txtShowFood);
        listViewFood.setText(strNameFood[position]);

        //Setup Text Gia
        TextView listViewGia = (TextView) view.findViewById(R.id.txtShowGia);
        listViewGia.setText(strGiaFood[position]);

        //Setup Image
        ImageView listImageFood = (ImageView) view.findViewById(R.id.imvFood);
        listImageFood.setBackgroundResource(intMyTarget[position]);


        return view;
    }   // getView

}   // Main Class
