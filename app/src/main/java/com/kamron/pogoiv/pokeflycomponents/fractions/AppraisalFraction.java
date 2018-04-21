package com.kamron.pogoiv.pokeflycomponents.fractions;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.kamron.pogoiv.GoIVSettings;
import com.kamron.pogoiv.Pokefly;
import com.kamron.pogoiv.R;
import com.kamron.pogoiv.pokeflycomponents.AutoAppraisal;
import com.kamron.pogoiv.utils.fractions.MovableFraction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTouch;

import static com.kamron.pogoiv.GoIVSettings.APPRAISAL_WINDOW_POSITION;


public class AppraisalFraction extends MovableFraction implements AutoAppraisal.OnAppraisalEventListener {

    @BindView(R.id.appraisalIVRangeGroup)
    RadioGroup appraisalIVRangeGroup;

    @BindView(R.id.appraisalIVRange4)
    RadioButton appraisalIVRange4;
    @BindView(R.id.appraisalIVRange3)
    RadioButton appraisalIVRange3;
    @BindView(R.id.appraisalIVRange2)
    RadioButton appraisalIVRange2;
    @BindView(R.id.appraisalIVRange1)
    RadioButton appraisalIVRange1;

    @BindView(R.id.attDefStaLayout)
    LinearLayout attDefStaLayout;

    @BindView(R.id.attCheckbox)
    CheckBox attCheckbox;
    @BindView(R.id.defCheckbox)
    CheckBox defCheckbox;
    @BindView(R.id.staCheckbox)
    CheckBox staCheckbox;

    @BindView(R.id.appraisalStatsGroup)
    RadioGroup appraisalStatsGroup;

    @BindView(R.id.appraisalStat4)
    RadioButton appraisalStat4;
    @BindView(R.id.appraisalStat3)
    RadioButton appraisalStat3;
    @BindView(R.id.appraisalStat2)
    RadioButton appraisalStat2;
    @BindView(R.id.appraisalStat1)
    RadioButton appraisalStat1;

    @BindView(R.id.eggRaidSwitch)
    Switch eggRaidSwitch;


    private Pokefly pokefly;
    private AutoAppraisal autoAppraisal;


    public AppraisalFraction(@NonNull Pokefly pokefly,
                             @NonNull SharedPreferences sharedPrefs,
                             @NonNull AutoAppraisal autoAppraisal) {
        super(sharedPrefs);
        this.pokefly = pokefly;
        this.autoAppraisal = autoAppraisal;
    }

    @Override
    protected @Nullable String getVerticalOffsetSharedPreferencesKey() {
        return APPRAISAL_WINDOW_POSITION;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fraction_appraisal;
    }

    @Override
    public void onCreate(@NonNull View rootView) {
        ButterKnife.bind(this, rootView);

        // Restore any previously selected appraisal info
        selectIVSumRange(autoAppraisal.appraisalIVSumRange);
        for (AutoAppraisal.HighestStat highestStat : autoAppraisal.highestStats) {
            selectHighestStat(highestStat);
        }
        selectIVValueRange(autoAppraisal.appraisalHighestStatValueRange);
        for (AutoAppraisal.StatModifier statModifier : autoAppraisal.statModifiers) {
            selectStatModifier(statModifier);
        }

        // Listen for new appraisal info
        autoAppraisal.addOnAppraisalEventListener(this);

        // Load the correct phrases from the text resources depending on what team is stored in app settings
        switch (GoIVSettings.getInstance(pokefly).playerTeam()) {
            case 0: // Mystic
                appraisalIVRange4.setText(R.string.mv4);
                appraisalIVRange3.setText(R.string.mv3);
                appraisalIVRange2.setText(R.string.mv2);
                appraisalIVRange1.setText(R.string.mv1);
                appraisalStat1.setText(R.string.ms1);
                appraisalStat2.setText(R.string.ms2);
                appraisalStat3.setText(R.string.ms3);
                appraisalStat4.setText(R.string.ms4);
                break;

            case 1: // Valor
                appraisalIVRange4.setText(R.string.vv4);
                appraisalIVRange3.setText(R.string.vv3);
                appraisalIVRange2.setText(R.string.vv2);
                appraisalIVRange1.setText(R.string.vv1);
                appraisalStat1.setText(R.string.vs1);
                appraisalStat2.setText(R.string.vs2);
                appraisalStat3.setText(R.string.vs3);
                appraisalStat4.setText(R.string.vs4);
                break;

            default:
            case 2: // Instinct
                appraisalIVRange4.setText(R.string.iv4);
                appraisalIVRange3.setText(R.string.iv3);
                appraisalIVRange2.setText(R.string.iv2);
                appraisalIVRange1.setText(R.string.iv1);
                appraisalStat1.setText(R.string.is1);
                appraisalStat2.setText(R.string.is2);
                appraisalStat3.setText(R.string.is3);
                appraisalStat4.setText(R.string.is4);
                break;
        }
    }

    @Override
    public void onDestroy() {
        autoAppraisal.removeOnAppraisalEventListener(this);
    }

    @Override
    public Anchor getAnchor() {
        return Anchor.TOP;
    }

    @Override
    public int getDefaultVerticalOffset(DisplayMetrics displayMetrics) {
        return 0;
    }

    @Override
    public void selectIVSumRange(AutoAppraisal.IVSumRange range) {
        switch (range) {
            case RANGE_37_45:
                appraisalIVRange1.setChecked(true);
                break;
            case RANGE_30_36:
                appraisalIVRange2.setChecked(true);
                break;
            case RANGE_23_29:
                appraisalIVRange3.setChecked(true);
                break;
            case RANGE_0_22:
                appraisalIVRange4.setChecked(true);
                break;
            default:
            case UNKNOWN:
                appraisalIVRangeGroup.clearCheck();
                break;
        }
    }

    @Override
    public void selectHighestStat(AutoAppraisal.HighestStat stat) {
        switch (stat) {
            case ATK:
                attCheckbox.setChecked(true);
                break;
            case DEF:
                defCheckbox.setChecked(true);
                break;
            case STA:
                staCheckbox.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void selectIVValueRange(AutoAppraisal.IVValueRange range) {
        switch (range) {
            case RANGE_15:
                appraisalStat1.setChecked(true);
                break;
            case RANGE_13_14:
                appraisalStat2.setChecked(true);
                break;
            case RANGE_8_12:
                appraisalStat3.setChecked(true);
                break;
            case RANGE_0_7:
                appraisalStat4.setChecked(true);
                break;
            default:
            case UNKNOWN:
                appraisalStatsGroup.clearCheck();
                break;
        }
    }

    private void selectStatModifier(AutoAppraisal.StatModifier modifier) {
        switch (modifier) {
            case EGG_OR_RAID:
                eggRaidSwitch.setChecked(true);
                break;
            case WEATHER_BOOST:
                // TODO weatherSwitch.setChecked(true);
                break;
            default:
                break;
        }
    }

    /**
     * Sets the background for the appropriate checkbox group depending on where we are at in the appraisal process.
     */
    @Override
    public void highlightActiveUserInterface() {
        resetActivatedUserInterface();
        if (autoAppraisal.appraisalIVSumRange == AutoAppraisal.IVSumRange.UNKNOWN) {
            // Percent range not completed yet
            appraisalIVRangeGroup.setBackgroundResource(R.drawable.highlight_rectangle);
        } else if (autoAppraisal.appraisalHighestStatValueRange == AutoAppraisal.IVValueRange.UNKNOWN) {
            // Highest stat IV value range not completed yet
            attDefStaLayout.setBackgroundResource(R.drawable.highlight_rectangle);
        } else {
            // Highest stat IV value range completed
            appraisalStatsGroup.setBackgroundResource(R.drawable.highlight_rectangle);
        }
    }

    /**
     * Disables any background highlight that had previously been set.  This method is used as a quick way to remove
     * all backgrounds prior to setting again or when the auto appraisal process has completed.
     */
    @Override
    public void resetActivatedUserInterface() {
        appraisalIVRangeGroup.setBackground(null);
        attDefStaLayout.setBackground(null);
        appraisalStatsGroup.setBackground(null);
    }

    @OnTouch({R.id.positionHandler, R.id.additionalRefiningHeader})
    boolean positionHandlerTouchEvent(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    @OnCheckedChanged(R.id.appraisalIVRange1)
    void onIVRange1(boolean checked) {
        if (checked) {
            autoAppraisal.appraisalIVSumRange = AutoAppraisal.IVSumRange.RANGE_37_45;
        }
    }

    @OnCheckedChanged(R.id.appraisalIVRange2)
    void onIVRange2(boolean checked) {
        if (checked) {
            autoAppraisal.appraisalIVSumRange = AutoAppraisal.IVSumRange.RANGE_30_36;
        }
    }

    @OnCheckedChanged(R.id.appraisalIVRange3)
    void onIVRange3(boolean checked) {
        if (checked) {
            autoAppraisal.appraisalIVSumRange = AutoAppraisal.IVSumRange.RANGE_23_29;
        }
    }

    @OnCheckedChanged(R.id.appraisalIVRange4)
    void onIVRange4(boolean checked) {
        if (checked) {
            autoAppraisal.appraisalIVSumRange = AutoAppraisal.IVSumRange.RANGE_0_22;
        }
    }

    @OnCheckedChanged(R.id.attCheckbox)
    void attChecked(boolean checked) {
        if (checked) {
            autoAppraisal.highestStats.add(AutoAppraisal.HighestStat.ATK);
        } else {
            autoAppraisal.highestStats.remove(AutoAppraisal.HighestStat.ATK);
        }
    }

    @OnCheckedChanged(R.id.defCheckbox)
    void defChecked(boolean checked) {
        if (checked) {
            autoAppraisal.highestStats.add(AutoAppraisal.HighestStat.DEF);
        } else {
            autoAppraisal.highestStats.remove(AutoAppraisal.HighestStat.DEF);
        }
    }

    @OnCheckedChanged(R.id.staCheckbox)
    void staChecked(boolean checked) {
        if (checked) {
            autoAppraisal.highestStats.add(AutoAppraisal.HighestStat.STA);
        } else {
            autoAppraisal.highestStats.remove(AutoAppraisal.HighestStat.STA);
        }
    }

    @OnCheckedChanged(R.id.appraisalStat1)
    void onStatRange1(boolean checked) {
        if (checked) {
            autoAppraisal.appraisalHighestStatValueRange = AutoAppraisal.IVValueRange.RANGE_15;
        }
    }

    @OnCheckedChanged(R.id.appraisalStat2)
    void onStatRange2(boolean checked) {
        if (checked) {
            autoAppraisal.appraisalHighestStatValueRange = AutoAppraisal.IVValueRange.RANGE_13_14;
        }
    }

    @OnCheckedChanged(R.id.appraisalStat3)
    void onStatRange3(boolean checked) {
        if (checked) {
            autoAppraisal.appraisalHighestStatValueRange = AutoAppraisal.IVValueRange.RANGE_8_12;
        }
    }

    @OnCheckedChanged(R.id.appraisalStat4)
    void onStatRange4(boolean checked) {
        if (checked) {
            autoAppraisal.appraisalHighestStatValueRange = AutoAppraisal.IVValueRange.RANGE_0_7;
        }
    }

    @OnClick(R.id.eggRaidText)
    void toggleRaidSwitch() {
        eggRaidSwitch.toggle();
    }

    @OnCheckedChanged(R.id.eggRaidSwitch)
    void onEggOrRaid(boolean checked) {
        if (checked) {
            autoAppraisal.statModifiers.add(AutoAppraisal.StatModifier.EGG_OR_RAID);
        } else {
            autoAppraisal.statModifiers.remove(AutoAppraisal.StatModifier.EGG_OR_RAID);
        }
    }

    @OnClick(R.id.statsButton)
    void onStats() {
        pokefly.navigateToInputFraction();
    }

    @OnClick(R.id.btnClose)
    void onClose() {
        pokefly.closeInfoDialog();
    }

    @OnClick(R.id.btnCheckIv)
    void checkIv() {
        pokefly.computeIv();
    }

}
