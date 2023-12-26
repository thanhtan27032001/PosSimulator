package com.example.creditcardsimulator2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.creditcardsimulator2.api_instance.PayApiInstance;
import com.example.creditcardsimulator2.model.Transaction;
import com.example.creditcardsimulator2.model.TransactionResponse;
import com.example.creditcardsimulator2.utils.FormatMoney;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    TextView textView;
    EditText edtDisFromHome, edtDisFromLastTrans, edtRatioToMedianPrice;
    Switch swRepeatRetailer, swUsedChip, swUsedPin, swOnlineOrder;
    Button btnGetRandomTransaction;

    private static Transaction transaction = new Transaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        setEvent();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "Device not support NFC", Toast.LENGTH_SHORT).show();
        }
        readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_IMMUTABLE);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
    }

    private void setView() {
        textView = findViewById(R.id.textView);
        btnGetRandomTransaction = findViewById(R.id.btnGetRandomTransaction);
        edtDisFromHome = findViewById(R.id.edtDisFromHome);
        edtDisFromLastTrans = findViewById(R.id.edtDisFromLastTrans);
        edtRatioToMedianPrice = findViewById(R.id.edtRatioToMedianPrice);
        swRepeatRetailer = findViewById(R.id.swRepeatRetailer);
        swUsedChip = findViewById(R.id.swUsedChip);
        swUsedPin = findViewById(R.id.swUsedPin);
        swOnlineOrder = findViewById(R.id.swOnlineOrder);
        if (transaction != null) {
            setRandomTransaction();
        }
    }

    private void setEvent() {
        btnGetRandomTransaction.setOnClickListener(v -> {
            transaction = Transaction.getRandomTransaction();
            setRandomTransaction();
        });

        edtDisFromHome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    transaction.setDistanceFromHome(Double.parseDouble(s.toString()));
                }
                else {
                    transaction.setDistanceFromHome(1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtDisFromLastTrans.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    transaction.setDistanceFromLastTransaction(Double.parseDouble(s.toString()));
                }
                else {
                    transaction.setDistanceFromLastTransaction(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtRatioToMedianPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    transaction.setRatioToMedianPurchasePrice(Double.parseDouble(s.toString()));
                }
                else {
                    transaction.setRatioToMedianPurchasePrice(1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        swRepeatRetailer.setOnCheckedChangeListener((buttonView, isChecked) -> {
            transaction.setRepeatRetailer(isChecked ? 1 : 0);
        });

        swUsedChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            transaction.setUsedChip(isChecked ? 1 : 0);
        });

        swUsedPin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            transaction.setUsedPinNumber(isChecked ? 1 : 0);
        });

        swOnlineOrder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            transaction.setOnlineOrder(isChecked ? 1 : 0);
        });
    }

    private void setRandomTransaction() {
        edtDisFromHome.setText(String.valueOf(transaction.getDistanceFromHome()));
        edtDisFromLastTrans.setText(String.valueOf(transaction.getDistanceFromLastTransaction()));
        edtRatioToMedianPrice.setText(String.valueOf(transaction.getRatioToMedianPurchasePrice()));
        swRepeatRetailer.setChecked(transaction.getRepeatRetailer() == 1);
        swUsedChip.setChecked(transaction.getUsedChip() == 1);
        swUsedPin.setChecked(transaction.getUsedPinNumber() == 1);
        swOnlineOrder.setChecked(transaction.getOnlineOrder() == 1);
    }

    @SuppressLint("SetTextI18n")
    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
//        Toast.makeText(this, action != null ? action : "action null", Toast.LENGTH_SHORT).show();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Toast.makeText(this, "read ok", Toast.LENGTH_SHORT).show();
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
                Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (rawMessages != null) {
                    NdefMessage message = (NdefMessage) rawMessages[0];
                    NdefRecord record = message.getRecords()[0];
                    if (record != null) {
                        byte[] payload = record.getPayload();
                        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
                        int languageCodeLength = payload[0] & 51;

                        try {
                            String text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
                            textView.setText("Balance: ".concat(FormatMoney.format(Double.parseDouble(text))));
                            if (transactionValid()){
                                if (Double.parseDouble(text) > 0){
                                    payTransaction();
                                }
                                else {
                                    showDialog(
                                            "Insufficient balance",
                                            "Please add more money to your account then execute transaction again",
                                            R.drawable.ic_deny);
                                }
                            }
                            else {
                                showDialog(
                                        "Require features",
                                        "Please enter all features before place NFC card in",
                                        R.drawable.ic_deny);
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        readFromIntent(intent);
    }

    private void payTransaction() {

        transaction.setDistanceFromHome(Double.parseDouble(edtDisFromHome.getText().toString()));
        transaction.setDistanceFromLastTransaction(Double.parseDouble(edtDisFromLastTrans.getText().toString()));
        transaction.setRatioToMedianPurchasePrice(Double.parseDouble(edtRatioToMedianPrice.getText().toString()));
        transaction.setRepeatRetailer(swRepeatRetailer.isChecked() ? 1 : 0);
        transaction.setUsedChip(swUsedChip.isChecked() ? 1 : 0);
        transaction.setUsedPinNumber(swUsedPin.isChecked() ? 1 : 0);
        transaction.setOnlineOrder(swOnlineOrder.isChecked() ? 1 : 0);

        Log.e("transaction", transaction.toString());

        Call<TransactionResponse> call = PayApiInstance.getInstance().payTransaction(transaction);
        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                if (response.isSuccessful()) {
                    showDialog(
                            "Transaction success",
                            "Congratulations, your transaction has been completed successfully",
                            R.drawable.ic_success);
                }
                else if (response.code() == 423) {
                    showDialog(
                            "Transaction failed!!!",
                            "Credit card fraud detected!!!",
                            R.drawable.ic_deny);
                }
                else if (response.code() == 500) {
                    showDialog(
                            "Transaction failed",
                            "Server error",
                            R.drawable.ic_deny);
                }
                else {
                    try {
                        assert response.errorBody() != null;
                        showDialog(
                                "Something went wrong",
                                response.errorBody().string(),
                                R.drawable.ic_deny);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                t.printStackTrace();
                showDialog(
                        "Something went wrong",
                        t.getMessage(),
                        R.drawable.ic_deny);
            }
        });

//        // test
//        Call<Integer> call = PayApiInstance.getInstance().payTransaction();
//        call.enqueue(new Callback<Integer>() {
//            @Override
//            public void onResponse(Call<Integer> call, Response<Integer> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(MainActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
//                } else {
//                    try {
//                        Toast.makeText(MainActivity.this, "error body " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Integer> call, Throwable t) {
//                t.printStackTrace();
//                Toast.makeText(MainActivity.this, "failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private boolean transactionValid(){
        return true;
    }

    private void showDialog(String title, String body, int dialogRes) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_rounded_corner);

        ImageView imgDialog = dialog.findViewById(R.id.imgDialog);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvBody = dialog.findViewById(R.id.tvBody);
        Button btnOk = dialog.findViewById(R.id.btnOk);

        imgDialog.setImageResource(dialogRes);
        tvTitle.setText(title);
        tvBody.setText(body);
        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // Set the width of the dialog to 90% of the screen width
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            window.setAttributes(layoutParams);

            window.setBackgroundDrawableResource(R.drawable.rounded_corners);
        }

        dialog.show();
    }
}