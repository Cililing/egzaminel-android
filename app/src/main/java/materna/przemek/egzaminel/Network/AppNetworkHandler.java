package materna.przemek.egzaminel.Network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;

import materna.przemek.egzaminel.Interfaces.OnResponseListener;

public class AppNetworkHandler {

    private static String TAG = AppNetworkHandler.class.getName();

    public static StringRequest getStringAnswer(final String tag, String url,  final HashMap<String, String> params, final OnResponseListener listener) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String fixed = StringEscapeUtils.unescapeJava(response);
                        listener.onResponse(tag, fixed);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onResponseError(tag, error);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

        };

        AppNetworkController.getInstance().addToRequestQueue(request, tag);
        return request;
    }

}
