package co.netpar.Model;

import java.util.List;

/**
 * Created by promatics on 11/27/2017.
 */

public class Blocks_Data_Model
{
    String block_name_regional, block_name_english;
    public String getBlock_name_regional() {
        return block_name_regional;
    }

    public String getBlock_name_english() {
        return block_name_english;
    }

    public Blocks_Data_Model(String block_name_regional, String block_name_english) {
        this.block_name_regional = block_name_regional;
        this.block_name_english = block_name_english;
    }
}
