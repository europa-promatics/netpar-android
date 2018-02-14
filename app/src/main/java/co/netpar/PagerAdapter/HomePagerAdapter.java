package co.netpar.PagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import co.netpar.Fragment.CategoryFragment;
import co.netpar.Fragment.ContactListFragment;
import co.netpar.Fragment.ContentListingAfterFilter;
import co.netpar.Fragment.Contribution;
import co.netpar.Fragment.Gallery;
import co.netpar.Fragment.HomeFragment;
import co.netpar.Fragment.MyAccountFragment;
import co.netpar.Fragment.Notifications;
import co.netpar.Fragment.SavedItems;
import co.netpar.Fragment.SubCategoryFragment;

/**
 * Created by promatics on 10/8/2017.
 */

public class HomePagerAdapter extends FragmentStatePagerAdapter
{
    private static int NUM_ITEMS;

    /*------ Fragments Position ---------*/
    public static final int homeFragment=0;
    public static final int contribution=1;
    public static final int myAccountFragment=2;
    public static final int categoryFragment=3;
    public static final int subCategoryFragment=4;
    public static final int ContactListFragment=5;
    public static final int contentListingAfterFilter=6;
    public static final int savedItems=7;
    public static final int gallery=8;
    public static final int notification=9;

    public HomePagerAdapter(FragmentManager fm, int NUM_ITEMS) {
        super(fm);
        this.NUM_ITEMS=NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case homeFragment:
                HomeFragment tab1 = new HomeFragment();
                return tab1;
            case contribution:
                Contribution tab2 = new Contribution();
                return tab2;
            case myAccountFragment:
                MyAccountFragment tab3 = new MyAccountFragment();
                return tab3;
            case categoryFragment:
                CategoryFragment tab4 = new CategoryFragment();
                return tab4;
            case subCategoryFragment:
                SubCategoryFragment tab5 = new SubCategoryFragment();
                return tab5;
            case ContactListFragment:
                ContactListFragment tab6 = new ContactListFragment();
                return tab6;
            case contentListingAfterFilter:
                ContentListingAfterFilter tab7 = new ContentListingAfterFilter();
                return tab7;
            case savedItems:
                SavedItems tab8 = new SavedItems();
                return tab8;
            case gallery:
                Gallery tab9 = new Gallery();
                return tab9;
            case notification:
                Notifications tab10 = new Notifications();
                return tab10;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
