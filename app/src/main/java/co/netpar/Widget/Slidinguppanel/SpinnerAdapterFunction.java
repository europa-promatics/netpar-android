package co.netpar.Widget.Slidinguppanel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import co.netpar.R;

/**
 * Created by traniee on 25/7/16.
 */
public class SpinnerAdapterFunction {
    private static Context ctxt;

    public static ArrayAdapter<String> SpinnerAdapter(Context ctx)
    {
        ctxt=ctx;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_dropdown_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == 0) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(position)); //"Hint to be displayed"
                    ((TextView)v.findViewById(android.R.id.text1)).setHintTextColor(ctxt.getResources().getColor(R.color.dark_logo)); //"Hint to be displayed"
                }
                else
                {
                    ((TextView)v.findViewById(android.R.id.text1)).setTextColor(ctxt.getResources().getColor(R.color.black));
                }
                return v;
            }
            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
