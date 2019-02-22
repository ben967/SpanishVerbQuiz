package com.example.ben.spanishverbquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.String;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity{

    // Global vars
    int randInt;
    int numVerbs;
    int numPronouns;
    int numConjugations;
    List verbList, conjugationData;
    String correctConjugation;
    String currentTense = "";

    // Set up the verb conjugations list
    String[] pronounsList = {"yo","tu", "el/ella/usted","nosotros/nosotras", "vosotros/vosotras", "ellos/ellas/ustedes"};
    String[] conjugationEndings = {"o", "as", "a", "amos", "áis", "an"};

    // Set up the random number generator
    Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load the verblist from the CSV file here
        InputStream inputStream = getResources().openRawResource(R.raw.top100regverbs);
        csvFile csvFile = new csvFile(inputStream);
        verbList = csvFile.read();
        numVerbs = verbList.size();
        numConjugations = conjugationEndings.length;
        numPronouns = pronounsList.length;

        // Load the conjugation list(s)
        loadConjugations();

        // Update the UI on load
        setData();
    }

    public void loadConjugations(){
        InputStream inputStream = getResources().openRawResource(R.raw.el_presente_del_indicativo);
        csvFile csvFile = new csvFile(inputStream);
        conjugationData = csvFile.read();
        currentTense = conjugationData.get(0).toString();
    }

    public void setData(){

        // Set the button colors
        ((Button) findViewById(R.id.button1)).setTextColor(0xffffffff);
        ((Button) findViewById(R.id.button2)).setTextColor(0xffffffff);
        ((Button) findViewById(R.id.button3)).setTextColor(0xffffffff);
        ((Button) findViewById(R.id.button4)).setTextColor(0xffffffff);
        ((Button) findViewById(R.id.button5)).setTextColor(0xffffffff);
        ((Button) findViewById(R.id.button6)).setTextColor(0xffffffff);

        // Set the text and buttons to loaded verb data
        randInt = r.nextInt((numVerbs - 0) - 1);
        String verbLine = verbList.get(randInt).toString();
        String[] verbData = verbLine.split(":");
        randInt = r.nextInt((numPronouns - 0) - 1);
        String randomPronoun = pronounsList[randInt];
        correctConjugation = verbData[1].substring(0, verbData[1].length() - 2) + conjugationEndings[randInt];

        // Update the text fields
        ((TextView) findViewById(R.id.questionText1)).setText(verbData[1]);
        ((TextView) findViewById(R.id.questionText2)).setText(verbData[2]);
        ((TextView) findViewById(R.id.questionText4)).setText(randomPronoun);
        ((TextView) findViewById(R.id.questionText3)).setText(currentTense);

        // Update the button texts
        String[] verbConjugations = getConjugations(verbData[1]);
        ((Button) findViewById(R.id.button1)).setText(verbConjugations[0]);
        ((Button) findViewById(R.id.button2)).setText(verbConjugations[1]);
        ((Button) findViewById(R.id.button3)).setText(verbConjugations[2]);
        ((Button) findViewById(R.id.button4)).setText(verbConjugations[3]);
        ((Button) findViewById(R.id.button5)).setText(verbConjugations[4]);
        ((Button) findViewById(R.id.button6)).setText(verbConjugations[5]);
    }

    public String[] getConjugations(String verb){
        String verbStem = verb.substring(0, verb.length() - 2);
        String verbEnding = verb.substring(verb.length() -2, verb.length());
        String [] conjugations = conjugationEndings.clone();
        for( int i = 0; i <= conjugationEndings.length - 1; i++)
        {
            conjugations[i] = verbStem + conjugationEndings[i];
        }
        Collections.shuffle(Arrays.asList(conjugations));
        return conjugations;
    }

    public void buttonPressed(View view){
        Button tempButton = (Button)view;
        String buttonText = tempButton.getText().toString();

        if (buttonText.equals(correctConjugation)){
            tempButton.setTextColor(0xff00ff00);
            setData();
        }
        else
        {
            tempButton.setTextColor(0xffff0000);
        }
    }
}