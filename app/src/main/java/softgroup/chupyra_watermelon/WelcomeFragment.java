package softgroup.chupyra_watermelon;


import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WelcomeFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerview;
    private List<WatermelonModel> mWatermelonModel;
    private RVAdapter adapter;
    private FloatingActionButton faBtnAdd;
    private MyApplication myApp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome,container,false);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
        Context context = view.getContext().getApplicationContext();
        myApp = (MyApplication) context;

        recyclerview.addOnItemTouchListener(new RecyclerViewTouchListener(context, recyclerview, new RecyclerViewClickListener() {

            @Override
            public void onClick(View view, int position) {
                myApp.setSelectedRVPosition(position);
                showChangeVarietyDialog(view);
            }

            @Override
            public void onLongClick(View view, int position) {
                myApp.setSelectedRVPosition(position);
                showDeleteWatermelonDialog(view);
            }
        }));
        faBtnAdd = (FloatingActionButton) view.findViewById(R.id.faBtn);
        faBtnAdd.setOnClickListener(add_FAB_Listener);
        return view;
    }

    public void showChangeVarietyDialog(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = LayoutInflater.from(view.getContext());
        final View dialogView = inflater.inflate(R.layout.new_variety_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.editTextVariety);

        dialogBuilder.setTitle(getResources().getString(R.string.change_variety_dialog_title));
        dialogBuilder.setIcon(R.mipmap.ic_change);
//        dialogBuilder.setMessage("");
        dialogBuilder.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mWatermelonModel.get(myApp.getSelectedRVPosition()).setVariety(edt.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });
        dialogBuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void showDeleteWatermelonDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(getResources().getString(R.string.delete_variety))
//                        .setMessage("")
                .setIcon(R.mipmap.ic_delete)
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mWatermelonModel.remove(myApp.getSelectedRVPosition());
                                adapter.notifyItemRemoved(myApp.getSelectedRVPosition());
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int whichButton) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private final View.OnClickListener add_FAB_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
            LayoutInflater inflater = LayoutInflater.from(view.getContext());
            final View dialogView = inflater.inflate(R.layout.new_variety_dialog, null);
            dialogBuilder.setView(dialogView);
            final EditText edt = (EditText) dialogView.findViewById(R.id.editTextVariety);
            dialogBuilder.setTitle(getResources().getString(R.string.new_variety_add_dialog_title));
            dialogBuilder.setIcon(R.mipmap.ic_add);
            dialogBuilder.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    mWatermelonModel.add(new WatermelonModel(edt.getText().toString(), R.drawable.watermelon));
                    int index = mWatermelonModel.size()-1;
//                    myApp.setWatermelonData(mWatermelonModel.get(index).getVariety());
                    myApp.setWatermelonData(mWatermelonModel);
                    adapter.notifyDataSetChanged();
                    recyclerview.scrollToPosition(adapter.getItemCount()-1);
                }
            });
            dialogBuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        }
    };

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        try {
            initializeData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new RVAdapter(mWatermelonModel);
        recyclerview.setAdapter(adapter);
    }

    private void initializeData() throws JSONException {
        mWatermelonModel = new ArrayList<>();
        String jsonStr = myApp.getWatermelonData_JSON_str();
        if (jsonStr.isEmpty()) {
            String[] varieties = getResources().getStringArray(R.array.varieties);
            TypedArray watermelons_photoes = getResources().obtainTypedArray(R.array.watermelons_photoes);
            for (int i = 0; i < varieties.length; i++) {
                mWatermelonModel.add(new WatermelonModel(varieties[i], watermelons_photoes.getResourceId(i, -1)));
            }
        }
        else {
            JSONArray jsonArray = new JSONArray(jsonStr);
            String variety;
            int photoId;
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = jsonArray.getJSONObject(i);
                    variety = jsonObject.getString(getResources().getString(R.string.variety_field));
                    photoId = jsonObject.getInt(getResources().getString(R.string.photoId_field));
                    mWatermelonModel.add(new WatermelonModel(variety, photoId));
                    jsonObject = null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        adapter.setFilter(mWatermelonModel);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }
                });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<WatermelonModel> filteredModelList = filter(mWatermelonModel, newText);

        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<WatermelonModel> filter(List<WatermelonModel> models, String query) {
        query = query.toLowerCase();final List<WatermelonModel> filteredModelList = new ArrayList<>();
        for (WatermelonModel model : models) {
            final String text = model.getVariety().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

//    private void showToast(String message, View v) {
//        Toast toast = Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER,0,0);
//        toast.show();
//    }

}
