package dcash.loanschedulecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class mainActivity extends AppCompatActivity {

    private EditText mML;
    private EditText mSD;
    private EditText mYears;
    private EditText mCPY;
    private Spinner mPF;
    private EditText mNom;
    private EditText mFPD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amortization_schedule);

        mML = (EditText) findViewById(R.id.textView);
        mSD = (EditText) findViewById(R.id.textView3);
        mYears = (EditText) findViewById(R.id.textView5);
        mCPY = (EditText) findViewById(R.id.textView7);
        mPF = (Spinner) findViewById(R.id.textView9);
        mNom = (EditText) findViewById(R.id.textView11);
        mFPD = (EditText) findViewById(R.id.textView13);

    }

    public void buttonPressed(View v) {
        Calculate();
    }

    public void SpinnerM() {

        // Creates a spinner with a list of choices
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payFreq, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPF.setAdapter(adapter);
    }

    public void Calculate() {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        BigDecimal balance = new BigDecimal("0.0");
        Date SD = new Date();
        Integer Years = 0;
        BigDecimal CPY = new BigDecimal("0");
        BigDecimal Nom = new BigDecimal("0.0");
        Date fPayDate = new Date();

        // Input error handling
        try {
            balance = BigDecimal.valueOf(Double.valueOf(mML.getText().toString()));
        } catch (NumberFormatException n) {
            System.out.println("Could not parse " + n);
        }

        try {
            SD = sdf.parse(mSD.getText().toString());
        } catch (ParseException e) {
            System.out.println("Could not parse " + e);
        }

        try {
            Years = Integer.parseInt(mYears.getText().toString());
            System.out.println(Years);
        } catch (NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        try {
            CPY = BigDecimal.valueOf(Integer.parseInt(mCPY.getText().toString()));
        } catch (NumberFormatException n) {
            System.out.println("Could not parse " + n);
        }

        try {
            fPayDate = sdf.parse(mFPD.getText().toString());
        } catch (ParseException e) {
            System.out.println("Could not parse " + e);
        }

        try {
            Nom = BigDecimal.valueOf(Double.valueOf(mNom.getText().toString()));
        } catch (NumberFormatException n) {
            System.out.println("Could not parse " + n);
        }

        String PF = mPF.getSelectedItem().toString();

        if (fPayDate.compareTo(SD) < 0){
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this.getApplicationContext(), "First payment date cannot be before the start date.", duration);
            toast.show();
        } else if (balance.compareTo(new BigDecimal("99999999999999")) > 0){
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this.getApplicationContext(), "Loan amount too large.", duration);
            toast.show();
        } else {
            new asyncCalc(balance, SD, Years, CPY, fPayDate, Nom, PF, getApplicationContext()).execute();
        }

    }


}
