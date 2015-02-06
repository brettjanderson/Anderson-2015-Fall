package com.brettjamesanderson.designpatterns;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;


public class DescriptionActivity extends ActionBarActivity {

    private DesignPattern currentPattern;
    private CheckBox favoriteCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        if(savedInstanceState != null){
            currentPattern = savedInstanceState.getParcelable("currentPattern");
        } else {
            currentPattern = getIntent().getExtras().getParcelable("designPattern");
        }

        TextView designName = (TextView) findViewById(R.id.designNameDetailsTextView);
        TextView designDescription = (TextView) findViewById(R.id.designDescription);
        favoriteCheckBox = (CheckBox) findViewById(R.id.favoriteCheckBox);

        designName.setText(currentPattern.getDesignPatternName());
        designDescription.setText(currentPattern.getDesignPatternDescription());
        favoriteCheckBox.setChecked(currentPattern.getFavorite());

        favoriteCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkBoxValue = favoriteCheckBox.isChecked();
                if(checkBoxValue) currentPattern.setFavorite(true);
                else currentPattern.setFavorite(false);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("currentPattern", currentPattern);
    }

    @Override
    public void onBackPressed() {
        DesignPatternsController.putDesignPatternBack(currentPattern);
        finish();
    }
}
