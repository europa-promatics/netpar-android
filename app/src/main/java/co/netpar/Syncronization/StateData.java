package co.netpar.Syncronization;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import co.netpar.Comman.Alert;
import co.netpar.Model.Blocks_Data_Model;
import co.netpar.Model.Data_Model;
import co.netpar.Model.District_Data_Model;

/**
 * Created by promatics on 11/27/2017.
 */

public class StateData
{
    /*---------GET State data List-------------*/
    public static List<Data_Model> getAllState(Context context)
    {
        List<Data_Model> st=new ArrayList<>();
        try
        {
            String data = loadJSONFromAsset(context);
            JSONObject object = new JSONObject(data);
            if(object.has("name"))
            {
                List<District_Data_Model> dst=new ArrayList<>();
                if(object.has("dist"))
                {
                   JSONArray disti=object.getJSONArray("dist");
                    Alert.showLog("OYE HOYE","disti.length()- "+disti.length());

                    for (int dt=0;dt<disti.length();dt++)
                   {
                       Alert.showLog("OYE HOYE","disti.length() come- "+dt);

                       JSONObject dist_dat=disti.getJSONObject(dt);
                       List<Blocks_Data_Model> bdt=new ArrayList<>();

                       Alert.showLog("OYE dist_dat","dist_dat.length() "+dist_dat.length());

                       if(dist_dat.has("block"))
                       {
                           JSONArray blockss=dist_dat.getJSONArray("block");
                           for (int blt=0;blt<blockss.length();blt++)
                           {
                               JSONObject blobk_dat=blockss.getJSONObject(blt);
                               bdt.add(new Blocks_Data_Model(blobk_dat.getString("hindiName"),blobk_dat.getString("name")));
                           }
                       }
                       dst.add(new District_Data_Model(dist_dat.getString("hindiName"),dist_dat.getString("name"),bdt));
                   }
                }
                st.add(new Data_Model(object.getString("hindiName"),object.getString("name"),dst));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Alert.showLog("Exception",e.toString()+" MESSAE=  "+e.getMessage());
        }
        return st;
    }

    /*---------Load State data in to our app-------------*/
    private static String loadJSONFromAsset(Context context)
    {
        String json = null;
        try
        {
            InputStream is = context.getAssets().open("state_district_blocks_maharashtra.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
