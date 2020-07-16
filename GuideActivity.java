package com.cropprediction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuideActivity extends AppCompatActivity {

    ListView listCrop;
    ArrayList<Crop> arrayList;
    CropShow cropShow;
    MySharedPreference mySharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mySharedPreference = new MySharedPreference(this);

        listCrop = (ListView) findViewById(R.id.listCrop);


        listCrop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Crop crop = arrayList.get(position);

                Log.d("pdf", Config.PDF_URL + "" + crop.getName() + ".pdf");

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.PDF_URL + "" + crop.getName() + ".pdf"));
                startActivity(browserIntent);

            }
        });

        requestData();
    }


    public class CropShow extends BaseAdapter {
        Context context;
        private LayoutInflater mInflater;
        private ArrayList<Crop> arrayList = new ArrayList<Crop>();

        public CropShow(Context context, ArrayList<Crop> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolder holder;
            final Crop crop = arrayList.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.layout_crop, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tvName);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvName.setText(crop.name);


            return convertView;
        }
    }

    class ViewHolder {
        TextView tvName, tvdate;
    }


    class Crop {
        public Crop() {
        }

        public String id = "";
        public String name = "";

        public Crop(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    private void requestData() {

        final ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", "Please Wait..", true);
        progressDialog.setCancelable(true);

        arrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.JSON_URL + "showUserCrop.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("list", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Crop crop = new Crop();
                                crop.setId(jsonObject.getString("id"));
                                crop.setName(jsonObject.getString("name"));
                                arrayList.add(crop);
                            }
                            if (jsonArray.length() == 0) {
                                Toast.makeText(getApplicationContext(), "No data Available", Toast.LENGTH_LONG).show();
                            }
                            cropShow = new CropShow(getApplicationContext(), arrayList);
                            listCrop.setAdapter(cropShow);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("list", "" + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("list", error + "error");
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", mySharedPreference.getPref("user_id"));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}
