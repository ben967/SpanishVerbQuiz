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
    int randVerb, randTense;
    int numVerbs, numTenses;
    int numPronouns;
    List verbList, conjugationData;
    String correctConjugation;
    String currentTense = "";

    // Set up the verb conjugations list
    String[] pronounsList = {"yo","tu", "el/ella/usted","nosotros/nosotras", "vosotros/vosotras", "ellos/ellas/ustedes"};
    String[] conjugationEndings;

    // Set up the random number generator
    Random randGen = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load the verblist from the CSV file here
        InputStream inputStream = getResources().openRawResource(R.raw.top100regverbs);
        csvFile csvFile = new csvFile(inputStream);
        verbList = csvFile.read();
        numVerbs = verbList.size();
        numPronouns = pronounsList.length;

        // Load the conjugations
        getTense();

        // Update the UI on load
        setData();
    }

    public void getTense(){

        InputStream inputStream = getResources().openRawResource(R.raw.tenseconjugationtable);
        csvFile csvFile = new csvFile(inputStream);
        conjugationData = csvFile.read();
        numTenses = conjugationData.size();
        randTense = randGen.nextInt(numTenses);
        currentTense = conjugationData.get(randTense).toString().split(",")[0];
        conjugationEndings = conjugationData.get(randTense).toString().split(",");
        conjugationEndings = Arrays.copyOfRange(conjugationEndings, 1, 19);

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
        randVerb = randGen.nextInt(numVerbs);
        String verbLine = verbList.get(randVerb).toString();
        String[] verbData = verbLine.split(":");
        randVerb = randGen.nextInt(numPronouns);
        String randomPronoun = pronounsList[randVerb];
        int index = 0;
        String verbEnding = verbData[1].substring(verbData[1].length() - 2);
        if (verbEnding.equals("ar")){
            index = 0;
        }
        else if (verbEnding.equals("er")){
            index = 6;
        }
        else {
            index = 12;
        }
        correctConjugation = verbData[1].substring(0, verbData[1].length() - 2) + conjugationEndings[randVerb+index];

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
        String verbEnding = verb.substring(verb.length() -2);
        int index = 0;
        if (verbEnding.equals("ar")){
            index = 0;
        }
        else if (verbEnding.equals("er")){
            index = 6;
        }
        else {
            index = 12;
        }
        String [] conjugations = Arrays.copyOfRange(conjugationEndings, index, index+6);
        for( int i = 0; i <= conjugations.length - 1; i++)
        {
            conjugations[i] = verbStem + conjugationEndings[index+i];
        }
        Collections.shuffle(Arrays.asList(conjugations));
        return conjugations;
    }

    public void buttonPressed(View view){
        Button tempButton = (Button)view;
        String buttonText = tempButton.getText().toString();

        if (buttonText.equals(correctConjugation)){
            tempButton.setTextColor(0xff00ff00);
            getTense();
            setData();
        }
        else
        {
            tempButton.setTextColor(0xffff0000);
        }
    }
}