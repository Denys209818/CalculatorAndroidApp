package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.calculator.dto.ActionData;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
    }

    public void onClickHandler(View view)
    {
        Button btn = (Button)view;

        String text = btn.getText().toString();
        if(editText.getText().toString().matches("^.*\\+|-|\\*|/$")
                && btn.getText().toString().matches("^\\+|\\*|-|/$"))
        {
            return;
        }
            editText.setText(editText.getText() + text,
                   TextView.BufferType.EDITABLE);

            editText.setSelection(editText.getText().length());


    }

    public void onClickGetRes(View view)
    {
        String inputText = editText.getText().toString();

        if(inputText.length() > 1 && inputText.matches("^[1-9].*[1-9]$"))
        {
            Double result = getResult(inputText);
            double a = Math.round(result*100);
            editText.setText(new Double(a/100).toString());
        }
    }

    public void onClickClear(View view)
    {
        editText.setText("");
    }

    public Double getResult(String str)
    {
        String strRez = str;
        //int res = 0;
        List<Double> arr = Arrays.stream(str.split("\\*|\\+|-|/"))
                .map(e -> Double.parseDouble(e)).collect(Collectors.toList());
        AtomicInteger ind = new AtomicInteger(0);

        List<ActionData> anchors = Arrays.stream(str.split("[0-9]"))
                .filter(e -> e.length() > 0).map(e -> createActionData(e, ind.getAndIncrement()))
                .sorted(Comparator.comparingInt(ActionData::getPriority).reversed())
                .collect(Collectors.toList());

        for(ActionData data : anchors)
        {
           String prev = arr.get(data.position).toString()+data.data+arr.get(data.position+1).toString();
           prev = prev.replace(".0", "");
            Double rez =0.0;
            switch(data.data)
            {
               case "*":
                   {
                       rez = arr.get(data.position)*arr.get(data.position+1);
                       break;
                   }
               case "/":
               {
                   rez = arr.get(data.position)/arr.get(data.position+1);
                   break;
               }
               case "+":
               {
                   rez = arr.get(data.position)+arr.get(data.position+1);
                   break;
               }
               case "-":
               {
                   rez = arr.get(data.position)-arr.get(data.position+1);
                   break;
               }
           }

            strRez = strRez.replace(prev, rez.toString().replace(".0", ""));

            arr.set(data.position, rez);

            for(int i = data.position+1; i < anchors.stream().count(); i++)
            {
                arr.set(i, arr.get(i+1));
            }

            anchors.stream().filter(e -> e.position > data.position)
                    .forEach(e -> e.position = e.position-1);

            int a = 0;
        }

        return Double.parseDouble(strRez);
    }

    public ActionData createActionData(String arg, int ind)
    {
        int priority = arg.contains("*") || arg.contains("/") ? 1 : 0;
        return new ActionData(arg, ind, priority);
    }
}