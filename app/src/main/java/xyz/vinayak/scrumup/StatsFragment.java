package xyz.vinayak.scrumup;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

public class StatsFragment extends Fragment {

    public Typeface font, fontBold;

    TextView tvProjectName, tvTotalValue, tvCompletedValue, tvPendingValue, tvUrgImpValue, tvUrgentValue, tvImpValue, tvDefaultValue;
    RoundCornerProgressBar progressTotal, progressCompleted, progressPending, progressUrgImp, progressUrgent, progressImp, progressDeafult;

    public StatsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        tvProjectName = rootView.findViewById(R.id.tvProjectName);
        tvProjectName.setText("Scrum Up Project");

        tvTotalValue = rootView.findViewById(R.id.tvTotalValue);
        tvCompletedValue = rootView.findViewById(R.id.tvCompletedValue);
        tvPendingValue = rootView.findViewById(R.id.tvPendingValue);
        tvUrgImpValue = rootView.findViewById(R.id.tvUrgImpValue);
        tvUrgentValue = rootView.findViewById(R.id.tvUrgentValue);
        tvImpValue = rootView.findViewById(R.id.tvImpValue);
        tvDefaultValue = rootView.findViewById(R.id.tvDefaultValue);

        progressTotal = rootView.findViewById(R.id.progressTotal);
        progressCompleted = rootView.findViewById(R.id.progressCompleted);
        progressPending = rootView.findViewById(R.id.progressPending);
        progressUrgImp = rootView.findViewById(R.id.progressUrgImp);
        progressUrgent = rootView.findViewById(R.id.progressUrgent);
        progressImp = rootView.findViewById(R.id.progressImp);
        progressDeafult = rootView.findViewById(R.id.progressDeafult);


//        font = Typeface.createFromAsset(this.getAssets(), "fonts/Montserrat.otf");
//        fontBold = Typeface.createFromAsset(this.getAssets(), "fonts/Montserrat-SemiBold.otf");
//
//        tvPokemonNameRank.setTypeface(fontBold);
//        tvPokemonHeightLabel.setTypeface(fontBold);
//        tvPokemonWeightLabel.setTypeface(fontBold);
//        tvMovesLabel.setTypeface(fontBold);
//        tvTypesLabel.setTypeface(fontBold);
//        tvPokemonStatsLabel.setTypeface(fontBold);

        update();
        return rootView;
    }

    public static StatsFragment newInstance(String text) {

        StatsFragment sf = new StatsFragment();
//        Bundle b = new Bundle();
//        b.putString("msg", text);
//
//        sf.setArguments(b);

        return sf;
    }

    @Override
    public void setUserVisibleHint(boolean visible){
        super.setUserVisibleHint(visible);
        if (visible && isResumed()){
            update();
        }
    }

    public void update(){

        progressTotal.setProgress(MainActivity.TOTAL_TASKS);
        tvTotalValue.setText(String.valueOf(MainActivity.TOTAL_TASKS));

        progressCompleted.setProgress(MainActivity.COMPLETED_TASKS);
        tvCompletedValue.setText(String.valueOf(MainActivity.COMPLETED_TASKS));

        progressPending.setProgress(MainActivity.PENDING_TASKS);
        tvPendingValue.setText(String.valueOf(MainActivity.PENDING_TASKS));

        progressUrgImp.setProgress(MainActivity.URGENT_IMPORTANT_TASKS);
        tvUrgImpValue.setText(String.valueOf(MainActivity.URGENT_IMPORTANT_TASKS));

        progressUrgent.setProgress(MainActivity.URGENT_TASKS);
        tvUrgentValue.setText(String.valueOf(MainActivity.URGENT_TASKS));

        progressImp.setProgress(MainActivity.IMPORTANT_TASKS);
        tvImpValue.setText(String.valueOf(MainActivity.IMPORTANT_TASKS));

        progressDeafult.setProgress(MainActivity.DEFAULT_TASKS);
        tvDefaultValue.setText(String.valueOf(MainActivity.DEFAULT_TASKS));
    }
}