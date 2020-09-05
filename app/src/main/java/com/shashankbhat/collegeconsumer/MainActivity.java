package com.shashankbhat.collegeconsumer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.shashankbhat.collegeconsumer.databinding.ActivityMainBinding;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        updateUI();
    }

    private void updateUI() {
        new ShowSearchHistory(getApplicationContext()).execute();
    }

    public class ShowSearchHistory extends AsyncTask<Void, Void, String> {
        private final WeakReference<Context> weakReference;

        public ShowSearchHistory(Context applicationContext) {
            this.weakReference = new WeakReference<>(applicationContext);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            binding.searchTable.setText(result);
        }

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder builder = new StringBuilder();

            builder.append("Searches made in College app appears here").append("\n");

            Cursor cursor = weakReference.get().getContentResolver()
                    .query(Uri.parse("content://com.shashankbhat.studenttable.provider/search_history_table"), null,
                            null, null, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                builder.append(cursor.getString(cursor.getColumnIndex("searchString")))
                        .append("\n");
                cursor.moveToNext();
            }

            return builder.toString();

        }
    }
}