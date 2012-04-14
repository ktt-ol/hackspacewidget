package de.ring0.hackspace;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public class HackspaceListFragment extends SherlockListFragment implements OnItemClickListener {

	List<String> spaceList = new ArrayList<String>();
	private SpaceSelectedListener listener = null;
	private ArrayAdapter<String> spaceAdapter;
	
	public HackspaceListFragment() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spaceAdapter = new ArrayAdapter<String>(getActivity(), R.layout.hackspace_list, spaceList);
	}
	
	@Override
	public void onStart() {
		super.onStart();
        setListAdapter(spaceAdapter);
        getListView().setOnItemClickListener(this);
        
        //END
        spaceAdapter.notifyDataSetChanged();
	}
	
	public void setOnSpaceSelectedListener(SpaceSelectedListener listener) {
		this.listener = listener;
	}
	
	public interface SpaceSelectedListener {
		public void onSpaceSelected(int position);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View vuew, int position, long id) {
		if (null != listener) {
			listener.onSpaceSelected(position);
        }		
	}
	
	public void setSelectable(boolean selectable) {
        if (selectable) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
        else {
            getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
        }
    }
}
