package com.blogspot.e_kanivets.moneytracker.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.RecordController;
import com.blogspot.e_kanivets.moneytracker.helper.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Account;
import com.blogspot.e_kanivets.moneytracker.model.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AddExpenseActivity";

    public static final String KEY_RECORD = "key_record";
    public static final String KEY_MODE = "key_mode";

    private Record record;
    private Mode mode;

    private EditText etTitle;
    private EditText etCategory;
    private EditText etPrice;
    private Spinner spinnerAccount;

    private RecordController recordController;
    private AccountController accountController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        recordController = new RecordController(new DbHelper(AddExpenseActivity.this));
        accountController = new AccountController(new DbHelper(AddExpenseActivity.this));

        if (getIntent() != null) {
            record = (Record) getIntent().getSerializableExtra(KEY_RECORD);
            mode = (Mode) getIntent().getSerializableExtra(KEY_MODE);
        }

        initViews();
        initActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                String title = etTitle.getText().toString();
                String category = etCategory.getText().toString();

                //Check if price is valid
                //noinspection UnusedAssignment
                int price = 0;
                try {
                    price = Integer.parseInt(etPrice.getText().toString());
                    if (price >= 0 && price <= 1000000000) {
                        Account account = accountController.getAccounts().get(spinnerAccount.getSelectedItemPosition());

                        if (mode == Mode.MODE_ADD) recordController.addRecord(new Date().getTime(),
                                1, title, category, price, account.getId(), -price);
                        if (mode == Mode.MODE_EDIT)
                            recordController.updateRecordById(record.getId(),
                                    title, category, price, account.getId(), -(price - record.getPrice()));
                    } else throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    Toast.makeText(AddExpenseActivity.this, getResources().getString(R.string.wrong_number_text),
                            Toast.LENGTH_SHORT).show();
                }

                setResult(RESULT_OK);
                finish();
                return true;

            case R.id.action_close:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews() {
        etTitle = (EditText) findViewById(R.id.et_title);
        etCategory = (EditText) findViewById(R.id.et_category);
        etPrice = (EditText) findViewById(R.id.et_price);

        List<Account> accountList = accountController.getAccounts();

        List<String> accounts = new ArrayList<>();
        for (Account account : accountList) {
            accounts.add(account.getTitle());
        }

        spinnerAccount = (Spinner) findViewById(R.id.spinner_account);
        spinnerAccount.setAdapter(new ArrayAdapter<>(AddExpenseActivity.this,
                android.R.layout.simple_list_item_1, accounts));

        //Add texts to dialog if it's edit dialog
        if (mode == Mode.MODE_EDIT) {
            etTitle.setText(record.getTitle());
            etCategory.setText(record.getCategory());
            etPrice.setText(Integer.toString(record.getPrice()));

            for (int i = 0; i < accountList.size(); i++) {
                Account account = accountList.get(i);
                if (account.getId() == record.getAccountId()) {
                    spinnerAccount.setSelection(i);
                }
            }
        }
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(null);
        }
    }

    public enum Mode {MODE_ADD, MODE_EDIT}
}