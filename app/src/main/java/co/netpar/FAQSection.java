package co.netpar;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.netpar.Adapter.FAQExpandableListAdapter;

public class FAQSection extends AppCompatActivity {

    private ExpandableListView faq_list;
    private List<String> questionDataHeader;
    private HashMap<String, List<String>> answer_DataChild;
    private ImageView back_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqsection);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.top_bar_bg_color));
        }
        faq_list=(ExpandableListView)findViewById(R.id.faq_list);
        prepareListData();
        back_icon=(ImageView)findViewById(R.id.back_icon);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            faq_list.setIndicatorBounds(width - GetDipsFromPixel(50), width - GetDipsFromPixel(10));

        } else {
            faq_list.setIndicatorBoundsRelative(width - GetDipsFromPixel(50), width - GetDipsFromPixel(10));
        }

        FAQExpandableListAdapter listAdapter = new FAQExpandableListAdapter(this, questionDataHeader, answer_DataChild);
        faq_list.setAdapter(listAdapter);
        faq_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View clickedView, int groupPosition, long rowId) {
                ImageView groupIndicator = (ImageView) clickedView.findViewById(R.id.help_group_indicator);
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                    groupIndicator.setImageResource(R.drawable.expend_down);
                } else {
                    parent.expandGroup(groupPosition);
                    groupIndicator.setImageResource(R.drawable.expend_up);
                }
                return true;
            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void prepareListData()
    {
        questionDataHeader = new ArrayList<String>();
        answer_DataChild = new HashMap<String, List<String>>();

        // Adding child data
        questionDataHeader.add("- नेटपर काय आहे?");
        questionDataHeader.add("- ही एक विना - नफा संस्था आहे का?");
        questionDataHeader.add("- नेटपरचा अॅप डाउनलोड कसा करायचा आणि ही सेवा मोफत आहे का?");
        questionDataHeader.add("- मी नेटपर वर लिहू शकतो का?");
        questionDataHeader.add("- मी हे लेख फेसबुक/ ट्विटर/व्हाट्सएप वर शेअर कसे करू शकतो?");
        questionDataHeader.add("- मी नेटपर च्या वाचकांशी कसंकाय कनेक्ट होऊन संवाद करू शकतो?");
        questionDataHeader.add("- मी वेबसाईट वर फीडबॅक कसा देऊ शकतो?");


        // Adding child dat
        List<String> ans1 = new ArrayList<String>();
        ans1.add("नेटपर एक मीडिया वेबसाईट आहे. नेटपरचं मुख्य उद्देश आहे कि ग्रामीण आणि अजून विकसीत होत असलेल्या शहरात त्यांच्या मायबोलीत माहिती पोचवणे. हे एक अॅपलीकेशन आहे ग्रामीण भारताला बाहेरच्या जगाशी जोडून देण्याकरीता.");

        List<String> ans2= new ArrayList<String>();
        ans2.add("नाही. नेटपर एक विना - नफा संस्था नाही आहे. ही एक खासगी संस्था आहे, भारतात नोंदणीकृत केलेली.");

        List<String> ans3 = new ArrayList<String>();
        ans3.add("नेटपर हा अॅप गुगल प्लेस्टोरवर उपलब्ध आहे. आणि हो! ही सेवा अगदी मोफत आहे.");

        List<String> ans4 = new ArrayList<String>();
        ans4.add("हो! तुम्ही देखील नेटपर साठी लिहू शकता आणि तुमचं लेखक बनण्याचं स्वप्न पूर्ण करू शकता.");

        List<String> ans5 = new ArrayList<String>();
        ans5.add("तुम्ही या लेखांच्या खाली दिलेल्या शेअर बटन्स वर क्लिक करून फेसबुक/ ट्विटर/व्हाट्सएप वर शेअर करू शकता.");

        List<String> ans6 = new ArrayList<String>();
        ans6.add("तुम्ही लेखांच्या खाली नेमलेल्या कमेंट सेकशन वर जाऊन वाचकांशी संवाद सादू शकता, किंवा नेटपर मराठी नावाच्या फेसबुक पेज वर जाऊन प्रेक्षकांशी कनेक्ट करू शकता.");

        List<String> ans7= new ArrayList<String>();
        ans7.add("तुम्ही आम्हाला या (info@netpar.co) ई-मेल आयडी वर तुमचे काही प्रश्न असतील तर ते विचारू शकता,किंवा काही सूचना असतील तर त्याही देऊ शकता.");

        answer_DataChild.put(questionDataHeader.get(0), ans1);
        answer_DataChild.put(questionDataHeader.get(1), ans2);
        answer_DataChild.put(questionDataHeader.get(2), ans3);
        answer_DataChild.put(questionDataHeader.get(3), ans4);
        answer_DataChild.put(questionDataHeader.get(4), ans5);
        answer_DataChild.put(questionDataHeader.get(5), ans6);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //Convert pixel to dip
    public int GetDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
}
