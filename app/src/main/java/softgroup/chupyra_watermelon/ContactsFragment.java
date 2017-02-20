package softgroup.chupyra_watermelon;


import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ContactsFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerview;
    private List<PhonebookContactModel> mPhonebookContact;
    private RVAdapter adapter;
    private FloatingActionButton faBtnAdd;
    private MyApplication myApp;
    private ProgressBar mProgressBar;
    TextView tvInfo;
    MyAsyncTask asyncTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tvInfo = (TextView) view.findViewById(R.id.tvInfo);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
        Context context = view.getContext().getApplicationContext();
        myApp = (MyApplication) context;

        recyclerview.addOnItemTouchListener(new RecyclerViewTouchListener(context, recyclerview, new RecyclerViewClickListener() {

            @Override
            public void onClick(View view, int position) {
                myApp.setSelectedRVPosition(position);
                PhonebookContactModel phonebookContact = mPhonebookContact.get(position);
                if (phonebookContact.getReadonlyStatus()) showAlert(view, "This contact is readonly");
                else showChangeContactDialog(view);
            }

            @Override
            public void onLongClick(View view, int position) {
                myApp.setSelectedRVPosition(position);
                PhonebookContactModel phonebookContact = mPhonebookContact.get(position);
                if (phonebookContact.getReadonlyStatus()) showAlert(view, "This contact is readonly");
                else showDeleteContactDialog(view);
            }
        }));
        faBtnAdd = (FloatingActionButton) view.findViewById(R.id.faBtn);
        faBtnAdd.setOnClickListener(add_FAB_Listener);
        return view;
    }

    public void showAlert(View view, String alertMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(alertMessage)
                .setIcon(R.mipmap.ic_delete)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void showChangeContactDialog(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = LayoutInflater.from(view.getContext());
        final View dialogView = inflater.inflate(R.layout.new_contact_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edtName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText edtPhone = (EditText) dialogView.findViewById(R.id.editTextPhone);

        dialogBuilder.setTitle(getResources().getString(R.string.change_contact_dialog_title));
        dialogBuilder.setIcon(R.mipmap.ic_change);
        dialogBuilder.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int position = myApp.getSelectedRVPosition();
                PhonebookContactModel phonebookContact = mPhonebookContact.get(position);
                phonebookContact.setName(edtName.getText().toString());
                phonebookContact.setPhone(edtPhone.getText().toString());
                myApp.updatePhonebookContactInDB(phonebookContact);
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

    public void showDeleteContactDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(getResources().getString(R.string.delete_contact))
                .setIcon(R.mipmap.ic_delete)
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                int position = myApp.getSelectedRVPosition();
                                PhonebookContactModel phonebookContact = mPhonebookContact.get(position);
                                mPhonebookContact.remove(position);
                                adapter.notifyItemRemoved(position);
                                myApp.removePhonebookContactFromDB(phonebookContact.getId());
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
            final View dialogView = inflater.inflate(R.layout.new_contact_dialog, null);
            dialogBuilder.setView(dialogView);
            final EditText edtName = (EditText) dialogView.findViewById(R.id.editTextName);
            final EditText edtPhone = (EditText) dialogView.findViewById(R.id.editTextPhone);
            dialogBuilder.setTitle(getResources().getString(R.string.new_contact_add_dialog_title));
            dialogBuilder.setIcon(R.mipmap.ic_add);
            dialogBuilder.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    PhonebookContactModel newmPhonebookContact = new PhonebookContactModel(edtName.getText().toString(), edtPhone.getText().toString(), false);
                    Log.d(TAG, String.valueOf(newmPhonebookContact.getId()));
                    myApp.savePhonebookContactToDB(newmPhonebookContact);
                    Log.d(TAG, String.valueOf(newmPhonebookContact.getId()));
                    mPhonebookContact.add(newmPhonebookContact);
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
        mPhonebookContact = new ArrayList<>();
        adapter = new RVAdapter(mPhonebookContact);
        recyclerview.setAdapter(adapter);

        asyncTask = new MyAsyncTask(mProgressBar, view);
        asyncTask.execute();
    }





    public class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        private ProgressBar mProgressBar;
        private View mView;

        public MyAsyncTask(ProgressBar target, View view) {
            mProgressBar = target;
            mView = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText(getResources().getString(R.string.start_contacts_reading));
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            tvInfo.setText(getResources().getString(R.string.contacts_were_read));
            adapter.notifyDataSetChanged();
        }

        private void initializeDataFromDB() {
            List<PhonebookContactDBRec> allPhonebookContactDBRec = myApp.getAllPhonebookContactsDBRec();
            String name;
            String phone;
            boolean readonlyContact;
            long id;
            for (int i = 0; i < allPhonebookContactDBRec.size(); i++) {
                name = allPhonebookContactDBRec.get(i).name;
                phone = allPhonebookContactDBRec.get(i).phone;
                id = allPhonebookContactDBRec.get(i).getId();
                readonlyContact = allPhonebookContactDBRec.get(i).readonly;
                mPhonebookContact.add(new PhonebookContactModel(name, phone, readonlyContact, id));
            }
        }
        public void getContacts(View view) {

            String name = "";
            String phone = "";
            boolean readonlyContact = true;
            long id;
            float i = 0;
            int i_max = 0;
            float k;


            //Связываемся с контактными данными и берем с них значения id контакта, имени контакта и его номера:
            Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
            String _ID = ContactsContract.Contacts._ID;
            String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

            Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
            String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;



            ContentResolver contentResolver = view.getContext().getContentResolver();
            Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

            //Запускаем цикл обработчик для каждого контакта:
            if (cursor.getCount() > 0) {
                i_max = cursor.getCount();
                //Если значение имени и номера контакта больше 0 (то есть они существуют) выбираем
                //их значения в приложение привязываем с соответствующие поля "Имя" и "Номер":
                while (cursor.moveToNext()) {
                    String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                    id = cursor.getColumnIndex(_ID);
                    name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                    //Получаем имя:
                    if (hasPhoneNumber > 0) {
                        Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,
                                Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                        //и соответствующий ему номер:
                        while (phoneCursor.moveToNext()) {
                            phone = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        }
                    }
                    mPhonebookContact.add(new PhonebookContactModel(name, phone, readonlyContact, id));
                    i++;
                    k = 100*(i/i_max);
                    publishProgress((int)k);
                }
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            initializeDataFromDB();
            getContacts(mView);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
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
                        adapter.setFilter(mPhonebookContact);
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
        final List<PhonebookContactModel> filteredModelList = filter(mPhonebookContact, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<PhonebookContactModel> filter(List<PhonebookContactModel> models, String query) {
        query = query.toLowerCase();final List<PhonebookContactModel> filteredModelList = new ArrayList<>();
        for (PhonebookContactModel model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
