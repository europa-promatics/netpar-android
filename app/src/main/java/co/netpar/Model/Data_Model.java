package co.netpar.Model;

import java.util.List;

/**
 * Created by promatics on 11/27/2017.
 */

public class Data_Model
{
    String state_name_regional,state_name_english;
    List <District_Data_Model> districts;

    public Data_Model(String state_name_regional, String state_name_english, List<District_Data_Model> districts) {
        this.state_name_regional = state_name_regional;
        this.state_name_english = state_name_english;
        this.districts = districts;
    }

    public String getState_name_regional() {
        return state_name_regional;
    }

    public String getState_name_english() {
        return state_name_english;
    }

    public List<District_Data_Model> getDistricts() {
        return districts;
    }
}
