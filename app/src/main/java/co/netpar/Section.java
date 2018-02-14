package co.netpar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import co.netpar.Widget.Slidinguppanel.SlidingUpPanelLayout;
import co.netpar.Widget.Slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import co.netpar.Widget.Slidinguppanel.SlidingUpPanelLayout.PanelState;
import java.util.Arrays;

/*Not Used*/
public class Section extends AppCompatActivity {
    private static final String TAG = "SECTION";
    private SlidingUpPanelLayout mLayout;
    private RecyclerView section_lay;

    class C02711 implements OnItemClickListener {
        C02711() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Toast.makeText(Section.this, "onItemClick",Toast.LENGTH_LONG).show();
        }
    }

    class C02723 implements OnClickListener {
        C02723() {
        }

        public void onClick(View view) {
            Section.this.mLayout.setPanelState(PanelState.COLLAPSED);
        }
    }

    class C02734 implements OnClickListener {
        C02734() {
        }

        public void onClick(View v) {
            Intent i = new Intent("android.intent.action.VIEW");
            i.setData(Uri.parse("http://www.twitter.com/umanoapp"));
            Section.this.startActivity(i);
        }
    }

    class C04722 implements PanelSlideListener {
        C04722() {
        }

        public void onPanelSlide(View panel, float slideOffset) {
            Log.i(Section.TAG, "onPanelSlide, offset " + slideOffset);
        }

        public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
            Log.i(Section.TAG, "onPanelStateChanged " + newState);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_section);
        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new C02711());
        lv.setAdapter(new ArrayAdapter(this, R.layout.drop_item, Arrays.asList(new String[]{"This", "Is", "An", "Example", "ListView", "That", "You", "Can", "Scroll", ".", "It", "Shows", "How", "Any", "Scrollable", "View", "Can", "Be", "Included", "As", "A", "Child", "Of", "SlidingUpPanelLayout"})));
        this.mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        this.mLayout.addPanelSlideListener(new C04722());
        this.mLayout.setFadeOnClickListener(new C02723());
        ((TextView) findViewById(R.id.name)).setText(Html.fromHtml("Hello"));
        Button f = (Button) findViewById(R.id.follow);
        f.setText(Html.fromHtml("Follow"));
        f.setMovementMethod(LinkMovementMethod.getInstance());
        f.setOnClickListener(new C02734());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.demo, menu);
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (this.mLayout != null) {
            if (this.mLayout.getPanelState() == PanelState.HIDDEN) {
                item.setTitle("Show");
            } else {
                item.setTitle("Hide");
            }
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle:
                if (this.mLayout == null) {
                    return true;
                }
                if (this.mLayout.getPanelState() != PanelState.HIDDEN) {
                    this.mLayout.setPanelState(PanelState.HIDDEN);
                    item.setTitle("Action Show");
                    return true;
                }
                this.mLayout.setPanelState(PanelState.COLLAPSED);
                item.setTitle("Action Hide");
                return true;
            case R.id.action_anchor:
                if (this.mLayout == null) {
                    return true;
                }
                if (this.mLayout.getAnchorPoint() == 1.0f) {
                    this.mLayout.setAnchorPoint(0.7f);
                    this.mLayout.setPanelState(PanelState.ANCHORED);
                    item.setTitle("action_anchor_disable");
                    return true;
                }
                this.mLayout.setAnchorPoint(1.0f);
                this.mLayout.setPanelState(PanelState.COLLAPSED);
                item.setTitle("action_anchor_enable");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        if (this.mLayout == null || !(this.mLayout.getPanelState() == PanelState.EXPANDED || this.mLayout.getPanelState() == PanelState.ANCHORED)) {
            super.onBackPressed();
        } else {
            this.mLayout.setPanelState(PanelState.COLLAPSED);
        }
    }
}
