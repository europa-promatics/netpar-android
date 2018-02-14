package co.netpar.Adapter;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import co.netpar.Home;
import co.netpar.Model.CategoryModel;
import co.netpar.Model.SectionModel;
import co.netpar.Model.SubCategoryModel;
import co.netpar.R;
import co.netpar.Syncronization.NetparDataBase.DatabaseHelper;
import co.netpar.Widget.NonScrollListView;

public class NVSectionExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<SectionModel> _listDataHeader;
    private HashMap<String, List<CategoryModel>> _listDataChild;
    private DatabaseHelper helper;
    private OnExpendItemClickListener onExpendItemClickListener;
    private ExpandableListView sectionExpandableListView;

    public NVSectionExpandableListAdapter(Context context, ExpandableListView sectionExpandableListView,List<SectionModel> listDataHeader, HashMap<String, List<CategoryModel>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.sectionExpandableListView=sectionExpandableListView;
        helper=((Home)context).helper;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return _listDataChild.get(_listDataHeader.get(groupPosition).getSectionName()).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    /*----------- Set child and sub child data -----------------*/
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        final CategoryModel childText = (CategoryModel) getChild(groupPosition, childPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.nv_section_list_item, null);
        }
        final TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        final NonScrollListView inner_list=(NonScrollListView)convertView.findViewById(R.id.inner_list);
        txtListChild.setText(childText.getCategoryName());
        inner_list.setVisibility(View.GONE);
        final List<SubCategoryModel> subCategoryModels=helper.getAllSubCategory(childText.getCategory_sectionId(),childText.getCategory_id());
        txtListChild.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(subCategoryModels.size()>0)
                {
                    if(inner_list.isShown())
                    {
                        inner_list.setVisibility(View.GONE);
                    }
                    else
                    {
                        NVSubCatAdapter threeHorizontalTextViewsAdapter = new NVSubCatAdapter(_context, R.layout.nav_sub_cat, subCategoryModels);
                        inner_list.setAdapter(threeHorizontalTextViewsAdapter);
                        inner_list.setVisibility(View.VISIBLE);
                        inner_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                onExpendItemClickListener.onSubCategoryItemClick(subCategoryModels.get(position),childText);
                            }
                        });
                    }
                }
                else
                {
                    inner_list.setVisibility(View.GONE);
                    onExpendItemClickListener.onCategoryItemClick(childText);
                }
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return _listDataChild.get(_listDataHeader.get(groupPosition).getSectionName()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _listDataHeader.get(groupPosition).getSectionName();
    }

    @Override
    public int getGroupCount() {
        return _listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.nv_section_list_header, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.NORMAL);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class NVSubCatAdapter extends ArrayAdapter<SubCategoryModel> {

        private int layoutResource;

        public NVSubCatAdapter(Context context, int layoutResource, List<SubCategoryModel> threeStringsList) {
            super(context, layoutResource, threeStringsList);
            this.layoutResource = layoutResource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                view = layoutInflater.inflate(layoutResource, null);
            }

            SubCategoryModel threeStrings = getItem(position);

            if (threeStrings != null)
            {
                TextView lblListItem = (TextView) view.findViewById(R.id.lblListItem);
                if (lblListItem != null) {
                    lblListItem.setText(threeStrings.getSub_category_Name());
                }
            }
            return view;
        }
    }

    public void setOnExpendItemClickListener(OnExpendItemClickListener onExpendItemClickListener)
    {
        this.onExpendItemClickListener=onExpendItemClickListener;
    }

    public interface OnExpendItemClickListener
    {
        public void onSectionItemClick(SectionModel data);
        public void onCategoryItemClick(CategoryModel data);
        public void onSubCategoryItemClick(SubCategoryModel data,CategoryModel data1);
    }
}
