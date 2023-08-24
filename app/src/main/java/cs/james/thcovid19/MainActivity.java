package cs.james.thcovid19;

import cs.james.thcovid19.R;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class MainActivity extends AppCompatActivity {
    private String url = "https://covid19.ddc.moph.go.th/api/Cases/today-cases-all";
    private RequestQueue queue;
    private TextView tvYear, tvWeek, tvNewCase, tvTotalCase, tvUpdateDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        matchView();
        getData();
    }

    private void matchView() {
        tvYear = findViewById(R.id.tv_year);
        tvWeek = findViewById(R.id.tv_week);
        tvNewCase = findViewById(R.id.tv_new);
        tvTotalCase = findViewById(R.id.tv_total);
        tvUpdateDate = findViewById(R.id.tv_update_date);
    }

    private void getData() {
        JsonArrayRequest request = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            DecimalFormat df = new DecimalFormat("###,###");
//                            Log.d("Response", response.toString());
//                            Log.d("JSONObject", response.get(0).toString());
                            JSONObject object = (JSONObject) response.get(0);
//                            Log.d("Year", object.getInt("year")+"");
//                            Log.d("Update Date", object.getString("update_date"));
                            int year = object.getInt("year");
                            tvYear.setText("รายงานประจำปี พ.ศ."+(year+543));

                            int week = object.getInt("weeknum");
                            tvWeek.setText("สัปดาห์ที่ " + week + "/52");

                            int newCase = object.getInt("new_case");
                            tvNewCase.setText("จำนวน " + df.format(newCase) + " คน");

                            int totalCase = object.getInt("total_case");
                            tvTotalCase.setText("จำนวน " + df.format(totalCase) + " คน");

                            String updateDate = object.getString("update_date");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = sdf.parse(updateDate);
                            sdf = new SimpleDateFormat("MMMM dd, yyyy");
                            tvUpdateDate.setText(sdf.format(date));

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(request);

    }
}