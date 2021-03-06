package com.blogspot.e_kanivets.moneytracker.ui.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.report.record.IRecordReport;
import com.blogspot.e_kanivets.moneytracker.ui.presenter.base.BaseSummaryPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Util class to create and manage summary header view for .
 * Created on 2/26/16.
 *
 * @author Evgenii Kanivets
 */
public class ShortSummaryPresenter extends BaseSummaryPresenter {

    @Inject
    FormatController formatController;

    private int red;
    private int green;
    private View view;

    @SuppressWarnings("deprecation")
    public ShortSummaryPresenter(Context context) {
        this.context = context;
        MtApp.get().getAppComponent().inject(ShortSummaryPresenter.this);

        layoutInflater = LayoutInflater.from(context);
        red = context.getResources().getColor(R.color.red);
        green = context.getResources().getColor(R.color.green);
    }

    public View create(boolean shortSummary) {
        view = layoutInflater.inflate(R.layout.view_summary_records, null);
        view.findViewById(R.id.iv_more).setVisibility(shortSummary ? View.VISIBLE : View.INVISIBLE);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    public void update(IRecordReport report, String currency, List<String> ratesNeeded) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if (report == null) {
            viewHolder.tvTotalIncome.setText("");
            viewHolder.tvTotalExpense.setText("");

            viewHolder.tvTotal.setTextColor(red);
            viewHolder.tvTotal.setText(createRatesNeededList(currency, ratesNeeded));
        } else {
            viewHolder.tvPeriod.setText(context.getString(R.string.period_from_to,
                    report.getPeriod().getFirstDay(), report.getPeriod().getLastDay()));

            viewHolder.tvTotalIncome.setTextColor(report.getTotalIncome() >= 0 ? green : red);
            viewHolder.tvTotalIncome.setText(formatController.formatIncome(report.getTotalIncome(),
                    report.getCurrency()));

            viewHolder.tvTotalExpense.setTextColor(report.getTotalExpense() > 0 ? green : red);
            viewHolder.tvTotalExpense.setText(formatController.formatExpense(report.getTotalExpense(),
                    report.getCurrency()));

            viewHolder.tvTotal.setTextColor(report.getTotal() >= 0 ? green : red);
            viewHolder.tvTotal.setText(formatController.formatIncome(report.getTotal(),
                    report.getCurrency()));
        }
    }

    public static class ViewHolder {
        @Bind(R.id.tv_period)
        TextView tvPeriod;
        @Bind(R.id.tv_total_income)
        TextView tvTotalIncome;
        @Bind(R.id.tv_total_expense)
        TextView tvTotalExpense;
        @Bind(R.id.tv_total)
        TextView tvTotal;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
