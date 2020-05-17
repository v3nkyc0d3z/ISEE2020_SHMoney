package com.example.strawhats;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;



public class settings_fragment extends Fragment {
    private ListView xmlListSettings;
    public View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
//        findXmlElements();
//        configureOnClickListeners();
//        setAdapters();
        return view;
    }
//
//    protected void findXmlElements() {
//        xmlListSettings = view.findViewById(R.id.SettingsList);
//    }
//
//    protected void configureOnClickListeners() {
//        xmlListSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        Intent i = new Intent(getActivity(), security_setting.class);
//                        startActivity(i);
//                        break;
//                    case 1:
//                        Intent j = new Intent(getActivity(), currency.class);
//                        startActivity(j);
//
//                        break;
//                    case 2:
//                        Intent k = new Intent(getActivity(), Neon.class);
//                        startActivity(k);
//
//                        break;
//                    case 3:
//
//                        break;
//                    case 4:
//
//                        break;
//                }
//            }
//        });
//    }
//
//
//    private void setAdapters() {
//        String[] items = new String[]{"Security settings", "Change currency", "Change date format", "Manage categories", "Clear all entries"};
//        ArrayAdapter<String> adapterItems = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
//        xmlListSettings.setAdapter(adapterItems);
//    }


}
