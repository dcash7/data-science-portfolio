package dcash.loanschedulecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by student on 1/29/2018.
 */

  public class asyncCalc extends AsyncTask<String, Void, Void> {


        BigDecimal balance = new BigDecimal("0.0");
        Date SD = new Date();
        Integer Years = 0;
        BigDecimal CPY = new BigDecimal("0");
        BigDecimal Nom = new BigDecimal("0.0");
        Date fPayDate = new Date();
        BigDecimal cintr = new BigDecimal("0.0");
        Calendar c = Calendar.getInstance();
        String PF;
        private Context context;
        Integer hold = 0;
        Integer amount = 0;

        ArrayList<String> dateAr = new ArrayList<>();
        ArrayList<BigDecimal> payAr = new ArrayList<BigDecimal>();
        ArrayList<BigDecimal> intAr = new ArrayList<BigDecimal>();
        ArrayList<BigDecimal> prinAr = new ArrayList<BigDecimal>();
        ArrayList<BigDecimal> balAr = new ArrayList<BigDecimal>();

        BigDecimal expE;
        BigDecimal Pay;
        Double core;
        BigDecimal equation;
        BigDecimal last;

        //async task as computations need to be done on a separate thread
        asyncCalc(BigDecimal balance, Date SD, Integer Years, BigDecimal CPY, Date fPayDate, BigDecimal Nom, String PF, Context context) {

            this.balance = balance;
            this.SD = SD;
            this.Years = Years;
            this.CPY = CPY;
            this.fPayDate = fPayDate;
            this.Nom = Nom;
            this.PF = PF;
            this.context = context;
        }

        @Override
        protected Void doInBackground(String... strings) {

            int i;
            int weeks = 0;
            BigDecimal wAvg;

            //Initialize and calculate starting values and number of payments
            if (PF.equals("Monthly")) {
                Pay = Nom.divide(CPY, 10, BigDecimal.ROUND_HALF_UP).add(BigDecimal.valueOf(1.0));
                expE = CPY.divide(BigDecimal.valueOf(12.0), 10, BigDecimal.ROUND_HALF_UP);
                //Can't raise a Big Decimal to another so doubles are next best thing
                core = Math.pow(Pay.doubleValue(), expE.doubleValue());
                //[1 - 1/(((1+nom/CPY)^(CPY/pmtFreq))^(pmtFreq*years))]  --- breakdown of ((1+nom/CPY)^(CPY/pmtFreq)) =  1 + mthly interest rate
                last = BigDecimal.valueOf(1.0).subtract(BigDecimal.valueOf(1.0).divide(BigDecimal.valueOf(Math.pow(core, (Years * 12))), 10, BigDecimal.ROUND_HALF_UP));
                //((core-1)*balance)/last
                equation = (BigDecimal.valueOf(core).subtract(BigDecimal.valueOf(1.0))).multiply(balance).divide(last, 2, BigDecimal.ROUND_HALF_UP);
                Years = Years * 12;
                cintr = BigDecimal.valueOf(core).subtract(BigDecimal.valueOf(1.0));
                hold = 1;
                amount = 1;

            } else if (PF.equals("Bi-Monthly"))

            {

                Pay = Nom.divide(CPY, 10, BigDecimal.ROUND_HALF_UP).add(BigDecimal.valueOf(1.0));
                expE = CPY.divide(BigDecimal.valueOf(6.0), 10, BigDecimal.ROUND_HALF_UP);
                core = Math.pow(Pay.doubleValue(), expE.doubleValue());
                last = BigDecimal.valueOf(1.0).subtract(BigDecimal.valueOf(1.0).divide(BigDecimal.valueOf(Math.pow(core, (Years * 6))), 10, BigDecimal.ROUND_HALF_UP));
                equation = (BigDecimal.valueOf(core).subtract(BigDecimal.valueOf(1.0))).multiply(balance).divide(last, 2, BigDecimal.ROUND_HALF_UP);
                Years = Years * 6;
                cintr = BigDecimal.valueOf(core).subtract(BigDecimal.valueOf(1.0));
                hold = 1;
                amount = 2;

            } else if (PF.equals("Weekly"))

            {
                //Total # of weeks (52/53)
                c.setTime(SD);
                for (i = 0; i < Years; i++) {
                    weeks += c.getActualMaximum(Calendar.WEEK_OF_YEAR);
                    c.add(Calendar.YEAR, 1);
                }
                wAvg = BigDecimal.valueOf(weeks).divide(BigDecimal.valueOf(Years), 10, BigDecimal.ROUND_HALF_UP);
                Pay = Nom.divide(CPY, 10, BigDecimal.ROUND_HALF_UP).add(BigDecimal.valueOf(1.0));
                expE = CPY.divide(wAvg, 10, BigDecimal.ROUND_HALF_UP);
                core = Math.pow(Pay.doubleValue(), expE.doubleValue());
                //52
                last = BigDecimal.valueOf(1.0).subtract(BigDecimal.valueOf(1.0).divide(BigDecimal.valueOf(Math.pow(core, weeks)), 10, BigDecimal.ROUND_HALF_UP));
                equation = (BigDecimal.valueOf(core).subtract(BigDecimal.valueOf(1.0))).multiply(balance).divide(last, 2, BigDecimal.ROUND_HALF_UP);
                Years = weeks;
                cintr = BigDecimal.valueOf(core).subtract(BigDecimal.valueOf(1.0));
                hold = 0;
                amount = 7;

            } else

            {
                c.setTime(SD);
                for (i = 0; i < Years; i++) {
                    weeks += c.getActualMaximum(Calendar.WEEK_OF_YEAR);
                    c.add(Calendar.YEAR, 1);
                }
                wAvg = BigDecimal.valueOf(weeks).divide(BigDecimal.valueOf(2), 10, BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(Years), 10, BigDecimal.ROUND_HALF_UP);
                Pay = Nom.divide(CPY, 10, BigDecimal.ROUND_HALF_UP).add(BigDecimal.valueOf(1.0));
                expE = CPY.divide(wAvg, 10, BigDecimal.ROUND_HALF_UP);
                core = Math.pow(Pay.doubleValue(), expE.doubleValue());
                last = BigDecimal.valueOf(1.0).subtract(BigDecimal.valueOf(1.0).divide(BigDecimal.valueOf(Math.pow(core, (BigDecimal.valueOf(weeks).divide(BigDecimal.valueOf(2), 10, BigDecimal.ROUND_HALF_UP).doubleValue()))), 10, BigDecimal.ROUND_HALF_UP));
                equation = (BigDecimal.valueOf(core).subtract(BigDecimal.valueOf(1.0))).multiply(balance).divide(last, 2, BigDecimal.ROUND_HALF_UP);
                //need to use big decimal as regular division will result in intiger division and drop any decimals
                Years = BigDecimal.valueOf(weeks).divide(BigDecimal.valueOf(2), 0, BigDecimal.ROUND_HALF_UP).intValueExact();
                cintr = BigDecimal.valueOf(core).subtract(BigDecimal.valueOf(1.0)); //.divide(BigDecimal.valueOf(1), 2, BigDecimal.ROUND_HALF_UP);
                hold = 0;
                amount = 14;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
            SimpleDateFormat intdate = new SimpleDateFormat("yyyyMMdd");

            //Loop to create amortization table
            for (i = 0; i < Years + 1; i++)

            {

                if ((i == 0) && (intdate.format(fPayDate).compareTo(intdate.format(SD)) != 0)) {
                    c.setTime(SD);
                    dateAr.add(dateFormat.format(c.getTime()));
                    balAr.add(balance);
                    payAr.add(BigDecimal.valueOf(0));
                    intAr.add(BigDecimal.valueOf(0));
                    prinAr.add(BigDecimal.valueOf(0));
                } else {
                    if (i == 0) {
                        c.setTime(SD);
                    }else if (hold == 0) {
                        c.add(Calendar.DATE, amount);
                    }else {
                        c.add(Calendar.MONTH, amount);
                    }
                    if ((i == 1) && (intdate.format(fPayDate).compareTo(intdate.format(SD)) != 0)) {
                        c.setTime(fPayDate);
                    }
                    //Pay off balance if last iteration or if balance + next period interest falls below monthly payment
                    if ((balance.multiply(cintr.add(BigDecimal.ONE)).compareTo(equation) < 0) || (i == Years)) {
                        payAr.add(balance.multiply(cintr.add(BigDecimal.ONE)).setScale(2, RoundingMode.HALF_UP));
                    } else {
                        payAr.add(equation);
                    }

                    intAr.add(balance.multiply(cintr).setScale(2, RoundingMode.HALF_UP));

                    //In payoff case, principal is the remaining last period balance
                    if ((balance.subtract(payAr.get(i)).subtract(balance.multiply(cintr)).compareTo(BigDecimal.valueOf(0)) < 1) || (i == Years)) {
                        prinAr.add(balance);
                    } else {
                        prinAr.add(equation.subtract(balance.multiply(cintr).setScale(2, RoundingMode.HALF_UP)));
                    }

                    balAr.add((balance.subtract(prinAr.get(i))));
                    balance = balAr.get(i);
                    dateAr.add(dateFormat.format(c.getTime()));
                    //Stop loop when balance reaches 0 for immediate repayment
                    if (balance.compareTo(BigDecimal.ZERO) == 0) {
                        i = Years + 1;
                    }
                }
                }

            //store the results so they can be retrieved in the main thread to draw to screen
            SQLiteDatabaseHandler mDbHelper = new SQLiteDatabaseHandler(context);

            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            mDbHelper.refresh(db);

            Gson gson = new Gson();
            String balString= gson.toJson(balAr);
            String dateString= gson.toJson(dateAr);
            String prinString= gson.toJson(prinAr);
            String intString= gson.toJson(intAr);
            String payString= gson.toJson(payAr);

            ContentValues values = new ContentValues();
            values.put(dataContract.DataEntry.COLUMN_DATE, dateString);
            values.put(dataContract.DataEntry.COLUMN_Pay, payString);
            values.put(dataContract.DataEntry.COLUMN_Int, intString);
            values.put(dataContract.DataEntry.COLUMN_Prin, prinString);
            values.put(dataContract.DataEntry.COLUMN_Bal, balString);

            db.insert(dataContract.DataEntry.TABLE_NAME, null, values);
            db.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void Void) {
            super.onPostExecute(Void);

            Intent intent = new Intent(context,scrollTable.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);


        }

    }

