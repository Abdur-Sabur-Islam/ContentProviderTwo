package com.me.save.com.contentprovidertwo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by acer on 8/14/2017.
 */

public class CountryEdit extends AppCompatActivity implements View.OnClickListener{

    private Spinner continentList;
    private Button save , delete ;
    private String mode;
    private EditText code, name;

    private String id;

    @Override
    public void onCreate(Bundle saveInstaceState){
        super.onCreate(saveInstaceState);
        setContentView(R.layout.detail_page);

        // get the values passed to the activity from the calling activity

        // determine the node  - add, update or delete ;

        if(this.getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            mode = bundle.getString("mode");
        }


        // get reference to the button and add listeners

        save = (Button) findViewById(R.id.save);
        delete = (Button) findViewById(R.id.delete);

        // get reference from the editText

        code = (EditText) findViewById(R.id.code);
        name = (EditText) findViewById(R.id.name);

        save.setOnClickListener(this);
        delete.setOnClickListener(this);

        // create a droup down for select varios country;

        continentList = (Spinner) findViewById(R.id.continentList);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.continent_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        continentList.setAdapter(adapter);

        // if in add mode dissabe the delete options
        if(mode.trim().equalsIgnoreCase("add")){
            delete.setEnabled(false);
        }
        // get the rowid for the specific country
        else{
            Bundle bundle = this.getIntent().getExtras();
            id = bundle.getString("rowId");
            loadCountryInfo();

        }

    }


    // base on the rowid get the get all information from content provider
    // about that county




    private void loadCountryInfo() {

             String[] projection = {

                     CountriesDb.KEY_ROWID,
                     CountriesDb.KEY_CODE,
                     CountriesDb.KEY_NAME,
                     CountriesDb.KEY_CONTINENT
             };

        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            String myCode = cursor.getString(cursor.getColumnIndexOrThrow(CountriesDb.KEY_CODE));
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(CountriesDb.KEY_NAME));
            String myContinent = cursor.getString(cursor.getColumnIndexOrThrow(CountriesDb.KEY_CONTINENT));
            code.setText(myCode);
            name.setText(myName);
            continentList.setSelection(getIndex(continentList, myContinent));
        }
    }

    // this sets the spiner selection based on the value



    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for(int i = 0 ;i<spinner.getCount();i++){
            if(spinner.getItemAtPosition(i).equals(myString)) {
                index = 1;
            }
        }
        return index;
    }



    @Override
    public void onClick(View view) {


        // get the value from the spineer and input text fields

        String myContinent = continentList.getSelectedItem().toString();
        String myName = name.getText().toString();
        String myCode = name.getText().toString();



        // check for blanks

        if(myCode.trim().equalsIgnoreCase("")){
            Toast.makeText(this, "Please fill the code field", Toast.LENGTH_SHORT).show();
        }
        if(myName.trim().equalsIgnoreCase("")){
            Toast.makeText(this, "Please fill the name field", Toast.LENGTH_SHORT).show();
        }



        switch (view.getId()){
            case R.id.save:

                ContentValues values = new ContentValues();
                values.put(CountriesDb.KEY_CODE,myCode);
                values.put(CountriesDb.KEY_NAME,myName);
                values.put(CountriesDb.KEY_CONTINENT,myContinent);

                // insert a record

                if(mode.trim().equalsIgnoreCase("add")){
                    getContentResolver().insert(MyContentProvider.CONTENT_URI,values);
                }
                // update a record

                else{
                    Uri uri = Uri.parse(MyContentProvider.CONTENT_URI+"/"+id);

                    getContentResolver().update(uri,values,null,null);

                }

                finish();
                break;

            case R.id.delete:
                // delete a record

                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI+"/"+id);
                getContentResolver().delete(uri,null,null);
                finish();
                break;


        }

    }
}
