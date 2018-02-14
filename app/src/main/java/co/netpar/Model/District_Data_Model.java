package co.netpar.Model;

import java.util.List;

/**
 * Created by promatics on 11/27/2017.
 */

public class District_Data_Model{
    String district_regional,district_name_english;
    List<Blocks_Data_Model> blocks;

    public District_Data_Model(String district_regional, String district_name_english, List<Blocks_Data_Model> blocks) {
        this.district_regional = district_regional;
        this.district_name_english = district_name_english;
        this.blocks = blocks;
    }

    public String getDistrict_regional() {
        return district_regional;
    }

    public String getDistrict_name_english() {
        return district_name_english;
    }

    public List<Blocks_Data_Model> getBlocks() {
        return blocks;
    }
}
