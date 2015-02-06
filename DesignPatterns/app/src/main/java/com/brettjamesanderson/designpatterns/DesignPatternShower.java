package com.brettjamesanderson.designpatterns;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;


public class DesignPatternShower extends ActionBarActivity {

    private DesignPattern currentPattern;
    private static final int SHOW_DESIGN_PATTERN_REQUEST = 1;
    private TextView designName;
    private Button showDescriptionButton;
    private Button nextDescriptionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentPattern = DesignPatternsController.getNewDesignPattern(getBaseContext());

        setContentView(R.layout.activity_design_pattern_shower);

        designName = (TextView) findViewById(R.id.designPatternNameTextView);
        designName.setText(currentPattern.getDesignPatternName());

        showDescriptionButton = (Button) findViewById(R.id.showDescriptionButton);
        nextDescriptionButton = (Button) findViewById(R.id.nextPattenButton);

        showDescriptionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent descriptionActIntent = new Intent(DesignPatternShower.this, DescriptionActivity.class);
                descriptionActIntent.putExtra("designPattern", currentPattern);
                startActivityForResult(descriptionActIntent, SHOW_DESIGN_PATTERN_REQUEST);
            }
        });

        nextDescriptionButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                DesignPatternsController.putDesignPatternBack(currentPattern);
                currentPattern = DesignPatternsController.getNewDesignPattern(getBaseContext());
                designName.setText(currentPattern.getDesignPatternName());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_design_pattern_shower, menu);
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
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelable("currentPattern",currentPattern);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    }
}
